package com.ijudy.races.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "race_type")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaceTypeEntity {

    @Id
    private Long id;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    @Column(name = "short_desc")
    private String shortDesc;

    @EqualsAndHashCode.Exclude
    @Column(name = "description")
    private String description;

    public static RaceTypeEntity clone(RaceTypeEntity raceTypeEntity){
        RaceTypeEntity clone = new RaceTypeEntity();
        clone.setId(raceTypeEntity.getId());
        clone.setDescription(raceTypeEntity.getDescription());
        clone.setShortDesc(raceTypeEntity.getShortDesc());
        clone.setName(raceTypeEntity.getName());
        return clone;
    }
}
