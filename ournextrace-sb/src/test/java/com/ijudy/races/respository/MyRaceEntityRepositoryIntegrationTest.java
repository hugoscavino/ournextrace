package com.ijudy.races.respository;

import com.ijudy.SpringIntegrationTest;
import com.ijudy.races.entity.MyRaceEntity;
import com.ijudy.races.repository.MyRaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MyRaceEntityRepositoryIntegrationTest extends SpringIntegrationTest {

    @Autowired
    private MyRaceRepository myRaceRepository;

    @Test
    @DisplayName("Test Finding One MyRace by User Id for Postgres")
    void getMyRaces() {
        List<MyRaceEntity> myRaceEntities = myRaceRepository.findByIdUserId(1L);
        assertNotNull(myRaceEntities);
    }
}
