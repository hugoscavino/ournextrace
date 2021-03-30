package com.ijudy.races.service.race.impl;

import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.dto.RaceDTO;
import com.ijudy.races.dto.RaceTypeDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.entity.MyRaceCompKey;
import com.ijudy.races.entity.MyRaceEntity;
import com.ijudy.races.entity.RaceEntity;
import com.ijudy.races.entity.RaceTypeEntity;
import com.ijudy.races.repository.MyRaceRepository;
import com.ijudy.races.repository.RaceRepository;
import com.ijudy.races.service.race.MyRaceService;
import com.ijudy.races.service.race.RaceService;
import com.ijudy.races.service.user.UserService;
import com.ijudy.races.util.RaceConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class MyRaceServiceImpl implements MyRaceService {

    public static final Long ANONYMOUS_USER_ID = 0L;

    private final MyRaceRepository myRaceRepository;
    private final RaceRepository raceRepository;
    private final RaceService raceService;
    private final UserService userService;

    public MyRaceServiceImpl(MyRaceRepository myRaceRepository, RaceRepository raceRepository, RaceService raceService, UserService userService) {
        this.myRaceRepository = myRaceRepository;
        this.raceRepository = raceRepository;
        this.raceService = raceService;
        this.userService = userService;
    }

    @Override
    public MyRaceDTO saveMyRace(final MyRaceDTO dto) {

        MyRaceEntity entity = RaceConverterUtil.toEntity(dto);
        MyRaceEntity savedEntity = myRaceRepository.save(entity);

        // Set the RaceTypes from the Saved Entity
        dto.setRaceTypes(RaceConverterUtil.toDTO(savedEntity.getRaceTypeEntities()));

        // Set Race
        // TODO Find a way for JPA to get this relationship
        Optional<RaceEntity> raceEntity = raceRepository.findById(savedEntity.getId().getRaceId());
        raceEntity.ifPresent(
                foundRaceEntity -> {
                    RaceDTO raceDTO = RaceConverterUtil.toDTO(foundRaceEntity);
                    dto.setRaceDTO(raceDTO);
                }
        );
        log.info("Saved " + dto.getRaceDTO().getId() + " for the user's list ");
        return dto;
    }

    @Override
    public void deleteMyRace(MyRaceDTO dto) {
        // MyRaceCompKey(Long userId, Long raceId){
        MyRaceCompKey pk = new MyRaceCompKey(dto.getUserDTO().getId(), dto.getRaceDTO().getId());
        myRaceRepository.deleteById(pk);
        log.info("deleted " + dto.getRaceDTO().getId() + " from the user's list ");
    }

    @Override
    public List<MyRaceDTO> getMyRaces(final Long userId) {

        if (userId != ANONYMOUS_USER_ID) {
            List<MyRaceEntity> myRaceEntities = myRaceRepository.findByIdUserId(userId);
            log.info("Found " + myRaceEntities.size() + " race entities");
            Optional<UserDTO> user = userService.findById(userId);
            List<MyRaceDTO> myRaceDTOs = new ArrayList<>();

            if (user.isPresent()){
                myRaceEntities.forEach(
                        myRaceEntity -> {
                            Optional<MyRaceDTO> myRaceDTO = toDTO(myRaceEntity, user.get());
                            if (myRaceDTO.isPresent()){
                                myRaceDTOs.add(myRaceDTO.get());
                            } else {
                                String msg = "Null myRaceDTO for Entity [" + myRaceEntity.getId() + "] because the race "
                                              + myRaceEntity.getId() + " was not fond in database but in the list for the user " + userId;
                                log.warn(msg);
                            }
                        }
                );
                log.info("Loaded " + myRaceDTOs.size() + " Race DTO's");
                if (myRaceDTOs.size() != myRaceEntities.size()){
                    log.warn("Loaded " + myRaceDTOs.size() + " Race DTO's but found " + myRaceEntities.size() + " entities");
                }
            }
            return myRaceDTOs;

        } else {
            log.info("No My Race Loaded for Anonymous User");
            return new ArrayList<MyRaceDTO>();
        }

    }

    private Optional<MyRaceDTO> toDTO(MyRaceEntity myRaceEntity, UserDTO user){

        Optional<RaceDTO> raceDTO = raceService.findById(myRaceEntity.getId().getRaceId());

        if (raceDTO.isEmpty()){
            return Optional.empty();
        } else {
            Set<RaceTypeEntity> raceTypesEntities = myRaceEntity.getRaceTypeEntities();
            Set<RaceTypeDTO> raceTypesSet = new HashSet<>(raceTypesEntities.size());

            raceTypesEntities.forEach(
                    raceTypeEntity -> raceTypesSet.add(RaceConverterUtil.toDTO(raceTypeEntity))
            );

            return Optional.of(MyRaceDTO.builder()
                    .myRaceStatus(myRaceEntity.getMyRaceStatus())
                    .raceDTO(raceDTO.get())
                    .userDTO(user)
                    .hotelName(myRaceEntity.getHotelName())
                    .registrationDate(myRaceEntity.getRegistrationDate().atStartOfDay())
                    .notes(myRaceEntity.getNotes())
                    .isPaid(myRaceEntity.isPaid())
                    .cost(myRaceEntity.getCost())
                    .modifiedDate(myRaceEntity.getModifiedDate())
                    .raceTypes(raceTypesSet)
                    .build());
        }
    }

    @Override
    public List<MyRaceDTO> getPublicAndMyRacesRaces(Long userId) {
        LocalDate begin = LocalDate.now().minusWeeks(1);
        LocalDate end = LocalDate.now().plusYears(1);
        return getPublicAndMyRacesRaces(begin, end, userId);
    }

    @Override
    public List<MyRaceDTO> getPublicAndMyRacesRaces(final LocalDate begin, final LocalDate end, final Long userId) {

        List<MyRaceDTO> pubicRaces = raceService.getPublicRaces(begin, end);

        final List<MyRaceDTO> myRaces = getMyRaces(userId);
        List<MyRaceDTO> mergedRaces = new ArrayList<>();

        pubicRaces.forEach(
                aPublicRace -> {
                    boolean mergeThisRace = true;
                    final RaceDTO dto = aPublicRace.getRaceDTO();

                    for (MyRaceDTO myRaceDTO : myRaces) {
                        boolean matched = dto.getId().equals(myRaceDTO.getRaceDTO().getId());
                        if (matched) {
                            mergedRaces.add(myRaceDTO);
                            mergeThisRace = false;
                        }
                    }

                    if (mergeThisRace){
                        boolean isPublic     = dto.isPublic();
                        boolean iAmTheAuthor = dto.getAuthor().getId() == userId;
                        if (isPublic) {
                            mergedRaces.add(aPublicRace);
                        } else if (iAmTheAuthor){
                            mergedRaces.add(aPublicRace);
                        }
                    }
                }
        );
        log.info("Loaded a combined " + mergedRaces.size() + " races");

        return mergedRaces;
    }

    @Override
    public Optional<MyRaceDTO> getMyRace(final MyRaceCompKey pk) {

        Optional<MyRaceEntity> optionalMyRaceEntity = myRaceRepository.findById(pk);

        if (optionalMyRaceEntity.isPresent()){
            Optional<MyRaceDTO> dto = toDTO(optionalMyRaceEntity.get());
            return dto;
        } else {
            log.warn("There was no myRace for pk : " + pk);
            return Optional.empty();
        }

    }

    private Optional<MyRaceDTO> toDTO(MyRaceEntity myRaceEntity) {

        Set<RaceTypeEntity> raceTypesEntities = myRaceEntity.getRaceTypeEntities();
        Set<RaceTypeDTO> raceTypesSet = new HashSet<>(raceTypesEntities.size());

        raceTypesEntities.forEach(
                raceTypeEntity -> raceTypesSet.add(RaceConverterUtil.toDTO(raceTypeEntity))
        );

        Long userId = myRaceEntity.getId().getUserId();
        Optional<UserDTO> userDTO = userService.findById(userId);

        Long raceId = myRaceEntity.getId().getRaceId();
        Optional<RaceDTO> raceDTO = raceService.findById(raceId);

        if (raceDTO.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(MyRaceDTO.builder()
                    .raceTypes(raceTypesSet)
                    .modifiedDate(myRaceEntity.getModifiedDate())
                    .cost(myRaceEntity.getCost())
                    .hotelName(myRaceEntity.getHotelName())
                    .isPaid(myRaceEntity.isPaid())
                    .notes(myRaceEntity.getNotes())
                    .registrationDate(myRaceEntity.getRegistrationDate().atStartOfDay())
                    .myRaceStatus(myRaceEntity.getMyRaceStatus())
                    .raceDTO(raceDTO.get())
                    .userDTO(userDTO.get())
                    .build());
        }
    }
}
