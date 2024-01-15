package com.ijudy.races.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ijudy.races.enums.MyRaceStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Models the Race Registration between a user and a Race
 *
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyRaceDTO {

    /**
     * Associated Race
     */
    @NonNull
    @JsonProperty("race")
    private RaceDTO raceDTO;

    /**
     * Associated User
     */
    @NonNull
    @JsonProperty("user")
    private UserDTO userDTO;

    /**
     *
     * What is the status for my event
     * Expect NOT_GOING only when user
     * changes their mind
     *
     */
    @EqualsAndHashCode.Exclude
    private MyRaceStatus myRaceStatus;

    /**
     * Subset of all the available race types
     */
    @EqualsAndHashCode.Exclude
    private Set<RaceTypeDTO> raceTypes;

    /**
     * Have I paid for this event?
     */
    @EqualsAndHashCode.Exclude
    private boolean isPaid;

    /**
     * How much the user paid for the event
     */
    @EqualsAndHashCode.Exclude
    private Float cost;

    /**
     * Date the user registered for the event
     */
    @EqualsAndHashCode.Exclude
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime registrationDate;

    /**
     * Name of the hotel where we all are staying
     * */
    @EqualsAndHashCode.Exclude
    private String 	hotelName;

    /**
     * Notes taken about your event
     */
    @EqualsAndHashCode.Exclude
    private String notes;

    /**
     * Date the event was modified
     */
    @EqualsAndHashCode.Exclude
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime modifiedDate;
}
