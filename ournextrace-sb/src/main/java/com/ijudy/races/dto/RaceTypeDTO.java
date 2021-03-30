package com.ijudy.races.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RaceTypeDTO {

    @NotNull
    private Long id;

    @NotBlank
    @EqualsAndHashCode.Exclude
    private String name;

    @NotBlank
    @EqualsAndHashCode.Exclude
    private String shortDesc;

    @NotBlank
    @EqualsAndHashCode.Exclude
    private String desc;
}
