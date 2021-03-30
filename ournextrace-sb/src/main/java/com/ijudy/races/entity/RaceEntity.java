package com.ijudy.races.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "races", schema = DatabaseMetadata.SCHEMA_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaceEntity {

    @Id()
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    private String name;

    private LocalDate date;

    private String description;
    private String url;

    @Column(name = "public")
    private Boolean isPublic;

    @Column(name = "active")
    private Boolean isActive;

    @Column(name = "cancelled")
    private Boolean isCancelled;

    private String couponCode;
    private LocalDateTime modDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name="address_id")
    private AddressEntity address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="race_race_type", schema = DatabaseMetadata.SCHEMA_NAME,
            joinColumns         = @JoinColumn(name = "race_id"),
            inverseJoinColumns  = @JoinColumn(name = "race_type_id"))
    private Set<RaceTypeEntity> raceTypeEntities;


    public static RaceEntity clone(RaceEntity raceEntity){

        RaceEntity clone = new RaceEntity();
        clone.setName(raceEntity.getName());
        clone.setDate(raceEntity.getDate());
        clone.setDescription(raceEntity.getDescription());
        clone.setUrl(raceEntity.getUrl());
        clone.setIsPublic(raceEntity.isPublic);
        clone.setIsActive(raceEntity.isActive);
        clone.setAuthor(raceEntity.getAuthor());
        clone.setCouponCode(raceEntity.getCouponCode());
        clone.setModDate(LocalDateTime.now());
        clone.setAddress(raceEntity.getAddress());
        return clone;
    }

    public static Set<RaceTypeEntity> cloneRaceTypes(Set<RaceTypeEntity> set){
        Set<RaceTypeEntity> clone = new HashSet<>(set.size());
        for (RaceTypeEntity entity: set) {
            clone.add(RaceTypeEntity.clone(entity));
        }
        return clone;
    }


}
