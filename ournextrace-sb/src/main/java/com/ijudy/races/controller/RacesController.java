package com.ijudy.races.controller;

import com.ijudy.races.exception.UpdateException;
import com.ijudy.races.dto.*;
import com.ijudy.races.entity.MyRaceCompKey;
import com.ijudy.races.exception.CreationException;
import com.ijudy.races.exception.NoAuthorizedException;
import com.ijudy.races.exception.NotFoundException;
import com.ijudy.races.security.SocialUserKey;
import com.ijudy.races.security.UserUtils;
import com.ijudy.races.service.race.AddressService;
import com.ijudy.races.service.race.MyRaceService;
import com.ijudy.races.service.race.RaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value="/api/v2")
@Slf4j
public class RacesController extends BaseController{

    static final Long anonymousUserId = 0L;

    public RacesController(RaceService raceService, AddressService addressService, MyRaceService myRaceService) {
        this.raceService = raceService;
        this.addressService = addressService;
        this.myRaceService = myRaceService;
    }

    static class ClonePayload {
        public Long raceId;
    }

    private final RaceService raceService;
    private final AddressService addressService;
    private final MyRaceService myRaceService;

    @GetMapping("/races")
    @ResponseBody
    public List<RaceDTO> getAllEvents() {
        return raceService.getAllRaces();
    }

    @GetMapping("/race/{raceId}")
    @ResponseBody
    public Optional<RaceDTO> getRaceById(@PathVariable Long raceId) {
        return raceService.findById(raceId);
    }

    @PutMapping("/race/{raceId}/{addressId}")
    @ResponseBody
    public RaceDTO updateRaceLocation(@PathVariable Long raceId, @PathVariable Long addressId) {
        return raceService.updateRaceLocation(raceId, addressId);
    }
    @PostMapping("/clone")
    @ResponseBody
    public RaceDTO cloneRace(@RequestBody ClonePayload payload, Principal principal){

        // Take form Principal and not the user's request
        SocialUserKey key = UserUtils.getSocialUserKey(principal);
        Optional<UserDTO> userDTO = userService.findByEmailAndSocialProvider(key);
        if (userDTO.isEmpty()) {
            log.error("Principal not found with email " + key.email + " | " + key.socialProvider);
            throw new NotFoundException("User is Not Found Will Not Save Race");
        }

        if (payload.raceId != null){
            RaceDTO nextYearsRace = raceService.clone(payload.raceId);
            return nextYearsRace;
        }
        return null;
    }
    @PostMapping("/race")
    @ResponseBody
    public RaceDTO saveRace(@NotNull @RequestBody RaceDTO raceDTO, Principal principal){

        // Take form Principal and not the user's request
        SocialUserKey key = UserUtils.getSocialUserKey(principal);
        Optional<UserDTO> optionalUserDTO = userService.findByEmailAndSocialProvider(key);

        if (optionalUserDTO.isEmpty()) {
            log.error("Principal not found with SocialUserKey " + key);
            throw new NotFoundException("User is Not Found Will Not Save Race");
        }

        raceDTO.setAuthor(optionalUserDTO.get());

        raceDTO.setCancelled(false); // Not cancelled by default

        if (optionalUserDTO.get().isAdmin()){
            raceDTO.setPublic(true);
        } else {
            raceDTO.setPublic(false);
        }

        // H2 will not save a default CURRENT TIME
        raceDTO.setModDate(LocalDateTime.now());
        if (raceDTO.getAddress().getId() == null){
            raceDTO.setAddress(null);
        }

        RaceDTO savedDTO = raceService.save(raceDTO);
        if (savedDTO != null){
            String msg = "Saved public(" + raceDTO.isPublic() + ") Race " + savedDTO.getName();
                   msg = msg + " with ID [" + savedDTO.getId() + "]";
            log.debug(msg);
        } else {
            final String msg = "Could not save Race " + raceDTO.getName();
            log.error(msg);
            throw new CreationException(msg);
        }

        return savedDTO;
    }

    @PatchMapping("/race")
    @ResponseBody
    public RaceDTO updateRace(@NotNull @RequestBody RaceDTO raceDTO, Principal principal){

        // Take form Principal and not the user's request
        Optional<UserDTO> userDTO = getUserDTO(principal);
        if (userDTO.isPresent()) {
            boolean isAdmin = (raceDTO.getId() != null) && userDTO.get().isAdmin();
            boolean powerUser = (raceDTO.getId() != null) && userDTO.get().isPowerUser();
            boolean myRace = (raceDTO.getId() != null) && (userDTO.get().getId().equals(raceDTO.getId()));

            if (myRace || isAdmin || powerUser) {
                // H2 will not save a default CURRENT TIME
                raceDTO.setModDate(LocalDateTime.now());
                RaceDTO savedDTO = raceService.save(raceDTO);

                if (savedDTO != null) {
                    log.debug("Saved Race " + savedDTO.getName() + " with ID [" + savedDTO.getId() + "]");
                } else {
                    final String msg = "Could not save Race " + raceDTO.getName();
                    log.error(msg);
                    throw new UpdateException(msg);
                }
                return savedDTO;
            } else {
                String msg = "This is not your race. You can only update your own races. The Race to be updated did not have a primary key";
                log.error(msg);
                throw new UpdateException(msg);
            }
        } else {
            String msg = "User Not Found when trying to update race : " + raceDTO.getName();
            log.error(msg);
            throw new NotFoundException(msg);
        }

    }

    @PatchMapping("/race-location")
    @ResponseBody
    public RaceDTO updateRaceLocation(@NotNull @RequestBody RaceDTO raceDTO){

        Optional<RaceDTO> foundRace = raceService.findById(raceDTO.getId());

        if (foundRace.isPresent() && raceDTO.getAddress() != null){
            AddressDTO addressDTO = addressService.getAddress(raceDTO.getAddress().getId());
            foundRace.get().setAddress(addressDTO);
            // H2 will not save a default CURRENT TIME
            foundRace.get().setModDate(LocalDateTime.now());
            RaceDTO savedRace =raceService.save(foundRace.get());
            log.debug("Updated Race " + savedRace.getName() + " [" + savedRace.getId() + "] with Location " + addressDTO.getLocation());
            return foundRace.get();
        } else {
            final String msg = "Could not save Race id [" + raceDTO.getId() + "]";
            log.error(msg);
            throw new UpdateException(msg);
        }


    }

    @PostMapping("/myRace")
    @ResponseBody
    public MyRaceDTO saveMyRace(@NotNull @RequestBody MyRaceDTO myRaceDTO, Principal principal){

        // Take from Principal and not the user's request
        SocialUserKey key = UserUtils.getSocialUserKey(principal);

        Long raceId = myRaceDTO.getRaceDTO().getId();
        log.debug("Saving  MyRace " + raceId  + " : for : " + key);

        Optional<UserDTO> optionalUserDTO = userService.findByEmailAndSocialProvider(key);

        if (optionalUserDTO.isEmpty()){
            log.error("Principal not found with SocialUserKey " + key);
            throw new NotFoundException("User is Not Found");
        }

        myRaceDTO.setUserDTO(optionalUserDTO.get());

        MyRaceDTO savedDTO = myRaceService.saveMyRace(myRaceDTO);
        log.debug("Saved MyRace for " + myRaceDTO.getRaceDTO().getName());

        return savedDTO;
    }

    @PatchMapping("/race-status")
    @ResponseBody
    public Optional<MyRaceDTO> updateMyRaceRaceStatus(@NotNull @RequestBody MyRaceDTO myRaceDto, Principal principal){

        // Take from Principal and not the user's request
        SocialUserKey key = UserUtils.getSocialUserKey(principal);
        Optional<UserDTO> optionalUserDTO = userService.findByEmailAndSocialProvider(key);
        if (optionalUserDTO.isEmpty()){
            log.error("Principal not found with SocialUserKey " + key);
            throw new NotFoundException("User is Not Found");
        }

        Long raceId = myRaceDto.getRaceDTO().getId();
        MyRaceCompKey pk = new MyRaceCompKey(optionalUserDTO.get().getId(), raceId);
        Optional<MyRaceDTO> myFoundRace = myRaceService.getMyRace(pk);
        if (myFoundRace.isPresent()){
            myFoundRace.get().setMyRaceStatus(myRaceDto.getMyRaceStatus());
            MyRaceDTO savedDTO = myRaceService.saveMyRace(myFoundRace.get());
            log.debug("Saved MyRace RaceStatus for " + myFoundRace.get().getRaceDTO().getName() + " with new status of " + myRaceDto.getMyRaceStatus());
            return Optional.of(savedDTO);
        } else {
            return Optional.empty();
        }

    }

    @DeleteMapping("/myRace/{raceId}")
    @ResponseBody
    public MyRaceDTO deleteMyRace(@NotNull @PathVariable Long raceId, Principal principal){

        // Take form Principal and not the user's request
        SocialUserKey key = UserUtils.getSocialUserKey(principal);

        log.debug("Deleting  MyRace id : {} : for : {}", raceId, key);

        Optional<UserDTO> userDTO = userService.findByEmailAndSocialProvider(key);

        if (userDTO.isEmpty()){
            log.error("Principal not found with email {}", key);
            throw new NotFoundException("User is Not Found");
        }

        MyRaceCompKey compKey = new MyRaceCompKey(userDTO.get().getId(), raceId);
        Optional<MyRaceDTO> myRaceOp = myRaceService.getMyRace(compKey);

        if (myRaceOp.isEmpty()) {
            final String msg ="Race with id " + raceId + " not found";
            log.error(msg);
            throw new NotFoundException(msg);
        } else {
            final MyRaceDTO raceDTO = myRaceOp.get();
            myRaceService.deleteMyRace(raceDTO);
            log.debug("Deleted MyRace with id [{}]", raceId);
            return raceDTO;
        }

    }

    @DeleteMapping("/race/{raceId}")
    @ResponseBody
    public Optional<RaceDTO> deleteRace(@NotNull @PathVariable Long raceId, Principal principal){

       // Take form Principal and not the user's request
        SocialUserKey key = UserUtils.getSocialUserKey(principal);
        Optional<UserDTO> userDTO = userService.findByEmailAndSocialProvider(key);
        if (userDTO.isPresent()) {
            Optional<RaceDTO> raceDTO = raceService.findById(raceId);
                if (raceDTO.isPresent()){
                    if (userDTO.get().isAdmin()) {
                        raceService.delete(raceId);
                        log.debug("Admin Deleted Race with id [{}]", raceId);
                    } else if (Objects.equals(userDTO.get().getId(), raceDTO.get().getAuthor().getId())) {
                        raceService.delete(raceId);
                        log.debug("Owner of Race Deleted Race with id [{}]", raceId);
                    } else {
                        throw new NoAuthorizedException("Attempting to Delete a Race which you did not create!");
                    }
                    return raceDTO;
                } else {
                    return Optional.empty();
                }
        } else {
            String msg = "User Not Found when trying to delete race with id : " + raceId;
            log.error(msg);
            throw new NotFoundException(msg);
        }
    }

    /**
     * Today's Local date : 2014-01-14
     *
     * beginDate in ISO format
     * endDate in ISO format
     */
    @GetMapping("/myRaces")
    @ResponseBody
    public List<MyRaceDTO> publicAndMyRaces(@RequestParam("beginDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
                                            @RequestParam("endDate")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            Principal principal) {

        boolean hasDates = beginDate != null  && endDate != null;
        List<MyRaceDTO> races = null;
        Long userIdToSearchWIth = anonymousUserId;
        if (principal != null) {
            SocialUserKey key = UserUtils.getSocialUserKey(principal);
            Optional<UserDTO> optionalUserDTO = userService.findByEmailAndSocialProvider(key);
            if (optionalUserDTO.isPresent()){
                userIdToSearchWIth = optionalUserDTO.get().getId();
            } else {
                throw new NotFoundException("User Not Found when trying to search for publicAndMyRaces");
            }
        }
        if (hasDates){
            races = myRaceService.getPublicAndMyRacesRaces(beginDate, endDate, userIdToSearchWIth );
        } else {
            races = myRaceService.getPublicAndMyRacesRaces(userIdToSearchWIth);
        }

        return races;

    }

    @GetMapping("/myRace/{raceId}")
    @ResponseBody
    public Optional<MyRaceDTO> getOneMyRace(@PathVariable Long raceId, Principal principal){

        // Take form Principal and not the user's request
        SocialUserKey key = UserUtils.getSocialUserKey(principal);
        Optional<UserDTO> optionalUserDTO = userService.findByEmailAndSocialProvider(key);
        if (optionalUserDTO.isPresent()){
            Long userId = optionalUserDTO.get().getId();
            MyRaceCompKey pk = new MyRaceCompKey(userId, raceId);
            final Optional<MyRaceDTO> myRace = myRaceService.getMyRace(pk);
            if (myRace.isEmpty()){
                throw new NotFoundException("Did not find a MyRace for {}" + raceId);
            }
            return myRace;
        } else {
            throw new NotFoundException("User Not Found when trying to delete race with id : " + raceId);
        }

    }

    @GetMapping("/race-types")
    @ResponseBody
    public List<RaceTypeDTO> allRaceTypes(){
        // TODO Cache this

        final List<RaceTypeDTO> allRaceTypes = raceService.getAllRaceTypes();
        log.error("Loaded " + allRaceTypes.size() + " race types");
        return allRaceTypes;
    }


}
