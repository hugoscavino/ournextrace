package com.ijudy.races.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Used as a composite primary key for the MyRaceEntity
 * USER_ID and RACE_ID
 */
@Data
@NoArgsConstructor
@Embeddable
public class MyRaceCompKey implements Serializable {

    /**
     * Create a key with USER_ID and RACE_ID
     *
     * @param userId The user's Id
     * @param raceId The race id
     */
    public MyRaceCompKey(Long userId, Long raceId){
        this.userId = userId;
        this.raceId = raceId;
    }
    /**
     * Primary Key to the MyRaceEntity
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Primary Key to the MyRaceEntity
     */
    @Column(name = "race_id", nullable = false)
    private Long raceId;
}
