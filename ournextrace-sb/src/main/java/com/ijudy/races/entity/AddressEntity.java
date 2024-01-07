package com.ijudy.races.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "address")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

    @Id()
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String location;

    private String street;

    private String city;

    private String state;

    private String zip;

    private String country;

    private String phone;

    private String notes;

    private Long authorId;

    private LocalDateTime modDate;
}
