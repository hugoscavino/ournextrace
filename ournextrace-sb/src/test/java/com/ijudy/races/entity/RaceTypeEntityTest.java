package com.ijudy.races.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RaceTypeEntityTest {

    @Test
    void testClone() {
        final String name = "10K";
        final String desc = "10K or 6.2 Miles road race";
        final String shortDesc = "10K";
        RaceTypeEntity original = new RaceTypeEntity();
        original.setName(name);
        original.setDescription(desc);
        original.setShortDesc(shortDesc);

        RaceTypeEntity clone = RaceTypeEntity.clone(original);
        assertTrue(clone.getName().equalsIgnoreCase(original.getName()));
    }

}