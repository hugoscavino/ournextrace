package com.ijudy.races.respository;

import com.ijudy.races.entity.RaceTypeEntity;
import com.ijudy.races.repository.RaceTypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("RaceType Repository Test")
public class RaceTypeEntityRepositoryTest {

    @Autowired
    private RaceTypeRepository repo;

    @Test
    @DisplayName("Test if find all returns all the RaceType Entities")
    public void getAllTest() {
        Iterable<RaceTypeEntity> iter = repo.findAll();
        assertNotNull(iter);

        assertTrue(iter.iterator().hasNext());
    }

    @Test
    @DisplayName("Test Finding One RaceType")
    public void getById()  {

        String EVENT_NAME = "IronMan 140.6";
        String SHORT_DESC = "140.6";
        Long pk = 80L;

        RaceTypeEntity ironMan = RaceTypeEntity.builder().
                                    id(pk).
                                    name(EVENT_NAME).
                                    description("140.6").
                                    shortDesc(SHORT_DESC).
                                    build();
        repo.save(ironMan);
        Optional<RaceTypeEntity> foundRaceType = repo.findById(pk);
        assertNotNull(foundRaceType);

        assertFalse(foundRaceType.isEmpty());

        assertEquals(pk, foundRaceType.get().getId());

        assertTrue(foundRaceType.get().getName().equalsIgnoreCase(EVENT_NAME));

        assertTrue(foundRaceType.get().getShortDesc().equalsIgnoreCase(SHORT_DESC));
    }
}
