package com.ijudy.races.respository;

import com.ijudy.races.entity.*;
import com.ijudy.races.enums.MyRaceStatus;
import com.ijudy.races.repository.MyRaceRepository;
import com.ijudy.races.repository.RaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("MyRaceEntity Repo Test")
public class MyRaceEntityRepositoryTest {

    @Autowired
    private MyRaceRepository myRaceRepository;

    @Autowired
    private RaceRepository raceRepository;

    private final Long USER_ID      = 1L;
    private final LocalDateTime NOW = LocalDateTime.now();

    @Test
    @DisplayName("Test Finding One MyRace by Id")
    void getById()  {
        String RACE_NAME = "Event at Al Lopez";
        Long RACE_ID = 201L;
        UserEntity user = UserEntity.builder().id(1L).email("hugo@scavino.org").build();

        MyRaceCompKey pk = new MyRaceCompKey();
        pk.setUserId(1L);
        pk.setRaceId(RACE_ID);

        Optional<MyRaceEntity> myRaceEntity = myRaceRepository.findById(pk);

        assertNotNull(myRaceEntity);
        assertFalse(myRaceEntity.isEmpty());
        MyRaceCompKey foundPk = myRaceEntity.get().getId();
        Long raceId = foundPk.getRaceId();
        assertTrue(RACE_ID.intValue() == raceId );

        Optional<RaceEntity> raceEntity = raceRepository.findById(raceId);
        RaceEntity entity = raceEntity.get();
        assertNotNull(entity);
        String raceName = entity.getName();
        assertTrue( RACE_NAME.equalsIgnoreCase(raceName));
    }

    @Test
    @DisplayName("Test Updating One MyRace")
    void updateMyRace()  {
        final String HOTEL_NAME = "Test Hotel";
        Long RACE_ID = 1L;
        Long RACE_TYPE_ID = 40L;
        UserEntity user = UserEntity.builder().id(USER_ID).email("hugo@scavino.org").build();

        MyRaceCompKey pk = new MyRaceCompKey();
        pk.setUserId(USER_ID);
        pk.setRaceId(RACE_ID);

        RaceTypeEntity raceTypeEntity = new RaceTypeEntity();
        raceTypeEntity.setId(RACE_TYPE_ID);

        List<RaceTypeEntity> raceTypeEntities = Arrays.asList(raceTypeEntity);
        MyRaceCompKey PK = new MyRaceCompKey(USER_ID, RACE_ID);
        MyRaceEntity entity = new MyRaceEntity(PK);
        entity.setHotelName(HOTEL_NAME);
        entity.setRaceTypeEntities(raceTypeEntities.stream().collect(Collectors.toSet()));
        entity.setPaid(true);
        entity.setNotes("This is a note");
        entity.setMyRaceStatus(MyRaceStatus.INTERESTED);
        final Float COST = 100F;
        entity.setCost(COST);
        entity.setModifiedDate(NOW);
        entity.setRegistrationDate(NOW.toLocalDate());

        final MyRaceEntity savedMyRace = myRaceRepository.save(entity);
        assertNotNull(savedMyRace);
        assertEquals(USER_ID, savedMyRace.getId().getUserId());
        assertEquals(RACE_ID, savedMyRace.getId().getRaceId());
        assertEquals(COST, savedMyRace.getCost());

        savedMyRace.setMyRaceStatus(MyRaceStatus.GOING);

        myRaceRepository.save(savedMyRace);

        final Optional<MyRaceEntity> foundEntity = myRaceRepository.findById(PK);
        foundEntity.ifPresent(
                myRaceEntity -> {
                        assertEquals(COST, myRaceEntity.getCost());
                        assertEquals(MyRaceStatus.GOING.getKey(), myRaceEntity.getMyRaceStatus().getKey());
                    }
                );

    }
}
