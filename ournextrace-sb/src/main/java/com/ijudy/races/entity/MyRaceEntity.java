package com.ijudy.races.entity;

import com.ijudy.races.enums.MyRaceStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "my_race", schema = DatabaseMetadata.SCHEMA_NAME)
@Data
@NoArgsConstructor
public class MyRaceEntity {

    @EmbeddedId
    private MyRaceCompKey id;

    public MyRaceEntity(Long userId, Long raceId){
        this.id = new MyRaceCompKey(userId,raceId);
    }

    public MyRaceEntity(MyRaceCompKey id){
        this.id = id;
    }

    /**
     * What is the status for my event
     * Expect NOT_ASSIGNED only when one changes their
     * mind
     */
    @Enumerated(EnumType.STRING)
    private MyRaceStatus myRaceStatus;

    /**
     * Subset of all the available race types
     */
    @OneToMany
    @JoinTable
        (name="my_race_race_type", schema = DatabaseMetadata.SCHEMA_NAME,
        joinColumns         = {
                @JoinColumn(name = "user_id"),
                @JoinColumn(name = "race_id"),
        },
        inverseJoinColumns  = @JoinColumn(name = "race_type_id")
        )
    private Set<RaceTypeEntity> raceTypeEntities;

    /**
     * Have I paid for this event?
     */
    @Column(name = "paid")
    private boolean isPaid;

    /**
     * How much the user paid for the event
     */
    private Float cost;

    /**
     * Date the user registered for the event
     */
    private LocalDate registrationDate;

    /**
     * Name of the hotel where we all are staying
     * */
    private String 	hotelName;

    /**
     * Notes taken about your event
     */
    private String notes;

    /**
     * Date the event was modified
     */
    private LocalDateTime modifiedDate;
}
