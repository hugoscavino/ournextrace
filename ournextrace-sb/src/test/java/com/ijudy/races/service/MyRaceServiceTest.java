package com.ijudy.races.service;

import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.service.race.MyRaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("MyRace Service Test")
class MyRaceServiceTest extends BaseIntegrationTest {

    @Autowired
    private MyRaceService myRaceService;


    @BeforeEach
    void setMockOutput() {
    }

    @Test
    void getMyRacesUserId() {

        Long USER_ID = 1L;
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
}
