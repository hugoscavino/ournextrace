package com.ijudy.races.respository;

import com.ijudy.races.entity.*;
import com.ijudy.races.repository.AddressRepository;
import com.ijudy.races.repository.MyRaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("MyRaceEntity Repo Test")
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("Test Finding One Address by Id")
    void getById()  {
        String LOCATION_NAME = "Al Lopez";
        Long AUTHOR_ID = 1L;

        AddressEntity addressEntity = AddressEntity.builder()
                                    .authorId(AUTHOR_ID)
                                    .state("FL")
                                    .city("Wesley Chapel")
                                    .country("USA")
                                    .modDate(LocalDateTime.now())
                                    .location(LOCATION_NAME).build();
        assertNotNull(addressEntity, "Did not Build a Row of type " + AddressEntity.class.getSimpleName());

        addressRepository.save(addressEntity);
        Long generatedId = addressEntity.getId();
        System.out.println("generatedId : " + generatedId);
        assertNotNull(addressEntity, "Did not Insert a Row of type " + AddressEntity.class.getSimpleName());

        Optional<AddressEntity> foundAddressEntityIterator = addressRepository.findById(generatedId);
        assertNotNull(foundAddressEntityIterator);

        foundAddressEntityIterator.ifPresent(
                    addressEntity1 -> {
                            System.out.println("Found ID " + addressEntity1.getId());
                            assertNotNull(addressEntity1, "Did not Find a Row of type " + AddressEntity.class.getSimpleName() + "using " + generatedId);
                            Long addressEntity1Id = addressEntity1.getId();
                            assertTrue(generatedId == addressEntity1Id, "Found Entity ID no match Expected [" + generatedId + "] instead found [" + addressEntity1Id +"]");
                    }
                );


    }

}
