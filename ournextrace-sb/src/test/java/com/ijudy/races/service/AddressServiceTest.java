package com.ijudy.races.service;

import com.ijudy.races.dto.AddressDTO;
import com.ijudy.races.service.race.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Address Service Test")
public class AddressServiceTest extends BaseIntegrationTest{

    private static final String LOCATION = "Some Location";
    public static final String CITY = "Tampa";
    public static final Long AUTHOR_ID = 0L;

    @Autowired
    private AddressService addressService;

    @BeforeEach
    void setMockOutput() {
    }

    @Test
    @DisplayName("Save Address Test")
    void saveAddress(){
        AddressDTO dto = AddressDTO.builder()
                .authorId(AUTHOR_ID)
                .location(LOCATION)
                .street("Some Street")
                .city(CITY)
                .state("FL")
                .zip("33710")
                .country("USA")
                .phone("(813) 555-1212")
                .notes("Some Notes").build();
        AddressDTO savedDTO = addressService.save(dto);

        assertNotNull(dto);
        assertEquals(LOCATION, savedDTO.getLocation());
        assertEquals(CITY, savedDTO.getCity());
    }
    @Test
    @DisplayName("Save Partial Address Test")
    void savePartialAddress(){
        AddressDTO dto = AddressDTO.builder()
                .authorId(AUTHOR_ID)
                .location(LOCATION)
                .street("Some Street")
                .city(CITY)
                .state("FL")
                .zip("33710")
                .country("USA").build();
        AddressDTO savedDTO = addressService.save(dto);

        assertNotNull(dto);
        assertEquals(LOCATION, savedDTO.getLocation());
        assertEquals(CITY, savedDTO.getCity());
    }

    @Test
    @DisplayName("Get All Addresses Test")
    void getAllAddressTest(){
        AddressDTO dto = AddressDTO.builder()
                .location(LOCATION)
                .street("Some Street")
                .city(CITY)
                .state("FL")
                .zip("33710")
                .country("USA")
                .phone("(813) 555-1212")
                .notes("Some Notes").build();
        AddressDTO savedDTO = addressService.save(dto);

        List<AddressDTO> addresses = addressService.getAllAddresses();
        assertNotNull(addresses);
        assertTrue(addresses.size() > 0);

        int index = addresses.indexOf(savedDTO);

        assertNotNull(addresses.get(index));
        assertEquals(LOCATION, addresses.get(index).getLocation());
        assertEquals(CITY, addresses.get(index).getCity());

    }
}
