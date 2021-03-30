package com.ijudy.races.entity;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RaceEntityTest {
    @Test
    void testClone() {

        final String name = "10K";
        final String desc = "10K or 6.2 Miles road race";
        RaceTypeEntity raceTypeEntity = new RaceTypeEntity();
        raceTypeEntity.setName(name);
        raceTypeEntity.setDescription(desc);

        final String raceName = "Big Race";
        final String raceDesc = "Big Race Description";
        RaceEntity original = new RaceEntity();
        original.setName(raceName);
        original.setDescription(raceDesc);
        original.setRaceTypeEntities(new HashSet<RaceTypeEntity>(Collections.singleton(raceTypeEntity)));

        RaceEntity clone = RaceEntity.clone(original);
        assertTrue(clone.getName().equalsIgnoreCase(original.getName()));
        //assertTrue(clone.getRaceTypeEntities().contains(raceTypeEntity));
        assertTrue(clone.getDescription().equalsIgnoreCase(original.getDescription()));

    }


}
