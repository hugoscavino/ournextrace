package com.ijudy.races.respository;

import com.ijudy.IJudyConstants;
import com.ijudy.SpringIntegrationTest;
import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.dto.RaceDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.entity.RaceEntity;
import com.ijudy.races.enums.MyRaceStatus;
import com.ijudy.races.repository.RaceRepository;
import com.ijudy.races.util.RaceConverterUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RaceEntityRepositoryIntegrationTest extends SpringIntegrationTest {
    public static final Long ANONYMOUS_USER_ID = 0L;
    @Autowired
    private RaceRepository raceRepository;

    @Test
    @DisplayName("Test Finding One Race by Id for Postgres")
    void getById()  {
        RaceEntity raceEntity = RaceEntity.builder()
                                .date(LocalDate.now())
                                .name("Race")
                                .description("Some Race")
                                .isActive(true)
                                .isPublic(true)
                                .modDate(LocalDateTime.now())
                                .url("www.race.com")
                                .build();

        RaceEntity entity = raceRepository.save(raceEntity);
        Long pk = entity.getId();
        Optional<RaceEntity> foundRace = raceRepository.findById(pk);
        assertNotNull(foundRace);
        assertFalse(foundRace.isEmpty());

        // Clean Up
        raceRepository.delete(entity);
        foundRace = raceRepository.findById(pk);
        assertFalse(foundRace.isPresent());
    }

    @Test
    @DisplayName("Test Finding All Public Races for Postgres")
    void getPublicRaces(){
        Iterable<RaceEntity> raceEntities = raceRepository.findAllByOrderByDate();
        assertNotNull(raceEntities);

        assertTrue(raceEntities.iterator().hasNext());

        LocalDate lastWeek = LocalDate.now().minusDays(7);
        final List<MyRaceDTO> myRaces = new ArrayList<>();
        // This is the public user.
        UserDTO PUBLIC_USER = UserDTO.builder().id(ANONYMOUS_USER_ID).build();
        raceEntities.forEach(
                raceEntity ->{
                    if (raceEntity.getDate() != null && raceEntity.getDate().isAfter(lastWeek)){
                        RaceDTO raceDTO = RaceConverterUtil.toDTO(raceEntity);
                        MyRaceDTO myRaceDTO = MyRaceDTO.builder()
                                .raceDTO(raceDTO)
                                .userDTO(PUBLIC_USER)
                                .myRaceStatus(MyRaceStatus.NOT_ASSIGNED)
                                .build();
                        myRaces.add(myRaceDTO);
                    }
                }
        );

        assertNotNull(myRaces);
        assertFalse(myRaces.isEmpty());

    }
}
