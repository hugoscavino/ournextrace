package com.ijudy.races.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RaceDTO {

    private Long id;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    private String description;

    @EqualsAndHashCode.Exclude
    private String url;

    @EqualsAndHashCode.Exclude
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @EqualsAndHashCode.Exclude
    protected AddressDTO address;

    @EqualsAndHashCode.Exclude
    private boolean isPublic;

    @EqualsAndHashCode.Exclude
    private boolean isActive;

    @EqualsAndHashCode.Exclude
    private boolean isCancelled;

    @EqualsAndHashCode.Exclude
    private String couponCode;

    @EqualsAndHashCode.Exclude
    private UserDTO author;

    @EqualsAndHashCode.Exclude
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime modDate;

    @EqualsAndHashCode.Exclude
    private Set<RaceTypeDTO> raceTypes;
}
