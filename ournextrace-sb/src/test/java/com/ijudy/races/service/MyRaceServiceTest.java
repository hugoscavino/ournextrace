package com.ijudy.races.service;

import com.ijudy.races.exception.NotFoundException;
import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.dto.RaceDTO;
import com.ijudy.races.dto.RaceTypeDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.entity.MyRaceCompKey;
import com.ijudy.races.enums.MyRaceStatus;
import com.ijudy.races.service.race.MyRaceService;
import com.ijudy.races.service.race.RaceService;
import com.ijudy.races.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MyRace Service Test")
class MyRaceServiceTest extends BaseIntegrationTest {

    @Autowired
    private MyRaceService myRaceService;

    @Autowired
    private RaceService raceService;

    @Autowired
    private UserService userService;

    private Float COST = 100F;
    private Long USER_ID = 1L;
    private LocalDateTime REG_DATE = LocalDateTime.now();
    private LocalDateTime MOD_DATE = LocalDateTime.now();

    @BeforeEach
    void setMockOutput() {
    }

    @Test
    void saveMyRace() {
        final Long RACE_ID = 1L;
        UserDTO userDTO = UserDTO.builder().id(USER_ID).build();
        String RACE_NAME = "Zooma Florida Half Marathon 13.1";
        RaceDTO raceDTO = RaceDTO.builder().id(RACE_ID).name(RACE_NAME).build();
        MyRaceDTO dto = MyRaceDTO.builder().raceDTO(raceDTO).userDTO(userDTO).build();
        dto.setCost(COST);
        final String HOTEL_NAME = "Some Hotel";
        dto.setHotelName(HOTEL_NAME);
        dto.setMyRaceStatus(MyRaceStatus.GOING);
        dto.setRegistrationDate(REG_DATE);

        RaceTypeDTO fiveKraceType = RaceTypeDTO.builder().id(40L).build();
        RaceTypeDTO tenKraceType = RaceTypeDTO.builder().id(50L).build();

        Set<RaceTypeDTO> raceTypes = of(fiveKraceType, tenKraceType).collect(Collectors.toSet());
        dto.setRaceTypes(raceTypes);

        MyRaceDTO savedDTO = myRaceService.saveMyRace(dto);

        assertNotNull(savedDTO);
        assertEquals(RACE_NAME, dto.getRaceDTO().getName());
    }

    @Test
    void deleteMyRace() {
        final Long RACE_ID = 1L;
        UserDTO userDTO = UserDTO.builder().id(USER_ID).build();
        String RACE_NAME = "Zooma Florida Half Marathon 13.1";
        RaceDTO raceDTO = RaceDTO.builder().id(RACE_ID).name(RACE_NAME).build();
        MyRaceDTO dto = MyRaceDTO.builder().raceDTO(raceDTO).userDTO(userDTO).build();
        dto.setCost(COST);

        final String HOTEL_NAME = "Some Hotel";
        dto.setHotelName(HOTEL_NAME);
        dto.setMyRaceStatus(MyRaceStatus.GOING);
        dto.setRegistrationDate(REG_DATE);
        MyRaceDTO savedDTO = myRaceService.saveMyRace(dto);
        assertNotNull(savedDTO);

        myRaceService.deleteMyRace(dto);

        assertThrows(NotFoundException.class,
                () -> myRaceService.getMyRace(new MyRaceCompKey(USER_ID, raceDTO.getId()))
            );
    }

    @Test
    void getMyRaces() {
        final Long RACE_ID = 1L;
        UserDTO userDTO = UserDTO.builder().id(USER_ID).build();
        RaceDTO raceDTO = RaceDTO.builder().id(RACE_ID).build();
        MyRaceDTO dto = MyRaceDTO.builder().raceDTO(raceDTO).userDTO(userDTO).build();
        dto.setCost(COST);
        final String HOTEL_NAME = "Some Hotel";
        dto.setHotelName(HOTEL_NAME);
        dto.setMyRaceStatus(MyRaceStatus.GOING);
        dto.setRegistrationDate(REG_DATE);

        RaceTypeDTO fiveKraceType = RaceTypeDTO.builder().id(40L).build();
        RaceTypeDTO tenKraceType = RaceTypeDTO.builder().id(50L).build();

        Set<RaceTypeDTO> raceTypes = of(fiveKraceType, tenKraceType).collect(Collectors.toSet());
        dto.setRaceTypes(raceTypes);
        MyRaceDTO savedDTO = myRaceService.saveMyRace(dto);
        assertNotNull(savedDTO);

        List<MyRaceDTO> myRaces = myRaceService.getMyRaces(USER_ID);

        assertNotNull(myRaces);
        assertEquals(2, myRaces.size());

        MyRaceDTO[] myRacesArray = new MyRaceDTO[myRaces.size()];
        myRaces.toArray(myRacesArray);

        assertEquals(HOTEL_NAME, myRacesArray[0].getHotelName());
        assertEquals(COST, myRacesArray[0].getCost());

    }

    @Test
    void getMyRacesUserId() {

        List<MyRaceDTO> myRaces = myRaceService.getMyRaces(USER_ID);
        assertNotNull(myRaces);
        assertEquals(2, myRaces.size());

        MyRaceDTO[] myRacesArray = new MyRaceDTO[myRaces.size()];
        myRaces.toArray(myRacesArray);

        assertEquals("Zooma Florida Half Marathon 13.1", myRacesArray[0].getRaceDTO().getName());
        assertEquals(100F, myRacesArray[0].getCost().floatValue());
        assertEquals("Hotel Name for ID 1-1", myRacesArray[0].getHotelName());

        assertEquals("Event at Al Lopez", myRacesArray[1].getRaceDTO().getName());
        assertEquals(123F, myRacesArray[1].getCost().floatValue());
        assertEquals("Hotel Name for ID 201-1", myRacesArray[1].getHotelName());

    }

    @Test
    void getOneMyRace() {
        Optional<UserDTO> userDTO = userService.findById(USER_ID);

        //final Long RACE_ID = 500L;
        final String RACE_NAME = "Some Half Marathon";
        final String RACE_DESC = "Super Duper Half Marathon Description";
        RaceDTO raceDTO = RaceDTO.builder()
                            .name(RACE_NAME)
                            .description(RACE_DESC)
                            .modDate(MOD_DATE)
                            .build();

        RaceDTO savedRaceDTO = raceService.save(raceDTO);
        assertNotNull(savedRaceDTO);

        final Float RACE_COST = 10F;
        final String HOTEL_NAME = "One Hotel";
        RaceTypeDTO fiveKiloRaceType = RaceTypeDTO.builder().id(40L).build();
        RaceTypeDTO tenKiloRaceType = RaceTypeDTO.builder().id(50L).build();
        Set<RaceTypeDTO> raceTypes = of(fiveKiloRaceType, tenKiloRaceType).collect(Collectors.toSet());

        MyRaceDTO dto = MyRaceDTO.builder()
                        .raceDTO(savedRaceDTO)
                        .userDTO(userDTO.get())
                        .cost(RACE_COST)
                        .hotelName(HOTEL_NAME)
                        .myRaceStatus(MyRaceStatus.GOING)
                        .registrationDate(REG_DATE)
                        .raceTypes(raceTypes)
                        .build();

        MyRaceDTO savedDTO = myRaceService.saveMyRace(dto);
        assertNotNull(savedDTO);

        MyRaceDTO myRace = myRaceService.getMyRace(new MyRaceCompKey(USER_ID, savedRaceDTO.getId())).get();
        assertNotNull(myRace);

        assertEquals(RACE_NAME, myRace.getRaceDTO().getName());
        assertEquals(RACE_COST, myRace.getCost());
        assertEquals(HOTEL_NAME, myRace.getHotelName());

    }
}
