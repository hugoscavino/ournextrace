package com.ijudy.races.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressDTO {

    private Long id;

    @EqualsAndHashCode.Exclude
    @NotNull
    private String location;

    @EqualsAndHashCode.Exclude
    private String street;

    @EqualsAndHashCode.Exclude
    private String city;

    @EqualsAndHashCode.Exclude
    private String state;

    @EqualsAndHashCode.Exclude
    private String zip;

    @EqualsAndHashCode.Exclude
    private String country;

    @EqualsAndHashCode.Exclude
    private String phone;

    @EqualsAndHashCode.Exclude
    private String notes;

    @EqualsAndHashCode.Exclude
    @NotNull
    private Long authorId;

    @EqualsAndHashCode.Exclude
    private LocalDateTime modDate;

    public static AddressDTO getDefaultAddress() {
        AddressDTO defaultAddress = AddressDTO.builder().id(0L).build();
        return defaultAddress;
    }

}
