package com.ijudy.races.controller;

import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.dto.RaceDTO;
import com.ijudy.races.dto.RaceTypeDTO;
import com.ijudy.races.entity.MyRaceCompKey;
import com.ijudy.races.service.race.MyRaceService;
import com.ijudy.races.service.race.RaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value="/api/v2")
@Slf4j
public class RacesController extends BaseController{

    static final Long ANONYMOUS_USER_ID = 0L;

    public RacesController(RaceService raceService,MyRaceService myRaceService) {
        this.raceService = raceService;
        this.myRaceService = myRaceService;
    }


    private final RaceService raceService;
    private final MyRaceService myRaceService;

    @GetMapping("/races")
    @ResponseBody
    public List<RaceDTO> getAllEvents() {
        return raceService.getAllRaces();
    }

    @GetMapping("/race/{raceId}")
    @ResponseBody
    public Optional<RaceDTO> getRaceById(@PathVariable("raceId") Long raceId) {
        return raceService.findById(raceId);
    }


    /**
     * Today's Local date: 2014-01-14
     *<br>
     * beginDate in ISO format
     * endDate in ISO format
     */
    @GetMapping("/myRaces")
    @ResponseBody
    public List<MyRaceDTO> publicRaces(@RequestParam(name = "beginDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
                                            @RequestParam(name = "endDate", required = false)   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        boolean hasDates = beginDate != null  && endDate != null;
        List<MyRaceDTO> races;

        if (hasDates){
            races = myRaceService.getPublicAndMyRacesRaces(beginDate, endDate, ANONYMOUS_USER_ID );
        } else {
            races = myRaceService.getPublicAndMyRacesRaces(ANONYMOUS_USER_ID);
        }

        return races;

    }

    @GetMapping("/myRace/{raceId}")
    @ResponseBody
    public Optional<MyRaceDTO> getOneMyRace(@PathVariable Long raceId){

            MyRaceCompKey pk = new MyRaceCompKey(MyRaceCompKey.DEFAULT_USER, raceId);
            final Optional<MyRaceDTO> myRace = myRaceService.getMyRace(pk);
            if (myRace.isEmpty()){
                throw new RuntimeException("Did not find a MyRace for {}" + raceId);
            }
            return myRace;

    }

    @GetMapping("/race-types")
    @ResponseBody
    public List<RaceTypeDTO> allRaceTypes(){
        final List<RaceTypeDTO> allRaceTypes = raceService.getAllRaceTypes();
        log.error("Loaded " + allRaceTypes.size() + " race types");
        return allRaceTypes;
    }


}
