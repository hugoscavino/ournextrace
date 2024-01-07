package com.ijudy.races.service;

import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.dto.RaceDTO;
import com.ijudy.races.dto.RaceTypeDTO;
import com.ijudy.races.entity.AddressEntity;
import com.ijudy.races.entity.RaceEntity;
import com.ijudy.races.entity.RaceTypeEntity;
import com.ijudy.races.repository.AddressRepository;
import com.ijudy.races.repository.RaceRepository;
import com.ijudy.races.repository.RaceTypeRepository;
import com.ijudy.races.service.race.MyRaceService;
import com.ijudy.races.service.race.RaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Race Service Test")
class RaceServiceTest extends BaseIntegrationTest{

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private RaceTypeRepository raceTypeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RaceService raceService;

    @Autowired
    private MyRaceService myRaceService;

    private final String LOCATION = "Some Location";
    private final String raceName = "Marthon Race";
    private final String desc = "This Race is Great";
    private final LocalDate today = LocalDate.now();
    private final LocalDateTime now = LocalDateTime.now();
    private final String url = "www.google.com";
    private RaceEntity mockRaceEntity = null;
    private Set<RaceTypeEntity> raceTypeEntities = null;

    @BeforeEach
    void setMockOutput() {

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setLocation(LOCATION);
        addressEntity.setState("FL");
        addressEntity.setCountry("USA");
        addressEntity.setAuthorId(0L);
        addressEntity.setModDate(LocalDateTime.now());
        addressRepository.save(addressEntity);

        RaceTypeEntity raceTypeEntity10K = RaceTypeEntity.builder().id(30L).name("10K").description("Run 6.2 Miles").build();
        RaceTypeEntity raceTypeEntity5K = RaceTypeEntity.builder().id(40L).name("5K").description("Run 3.1 Miles").build();
        raceTypeRepository.save(raceTypeEntity5K);
        raceTypeRepository.save(raceTypeEntity10K);

        raceTypeEntities = of(raceTypeEntity5K, raceTypeEntity10K).collect(Collectors.toSet());

        RaceEntity raceEntity = new RaceEntity();
        raceEntity.setName(raceName);
        raceEntity.setDescription(desc);
        raceEntity.setDate(today);
        raceEntity.setIsPublic(true);
        raceEntity.setUrl(url);
        raceEntity.setModDate(now);
        raceEntity.setAddress(addressEntity);
        raceEntity.setRaceTypeEntities(raceTypeEntities);

        mockRaceEntity = raceRepository.save(raceEntity);

    }

    @Test
    void getPublicRacesTest(){

        assertTrue(mockRaceEntity.getId() > 0);

        List<MyRaceDTO> publicRaces = raceService.getPublicRaces();

        assertNotNull(publicRaces);
        assertEquals(1, publicRaces.size());
        publicRaces.forEach(
                dto -> {
                    assertEquals(raceName, dto.getRaceDTO().getName());
                    assertEquals(today, dto.getRaceDTO().getDate());
                    assertEquals(desc, dto.getRaceDTO().getDescription());
                    assertTrue(dto.getRaceDTO().isPublic());
                    assertEquals(url, dto.getRaceDTO().getUrl());
                    assertEquals(now, dto.getRaceDTO().getModDate());
                    assertNotNull(dto.getRaceDTO().getAddress());
                    assertEquals(LOCATION, dto.getRaceDTO().getAddress().getLocation());
                    }
                );


    }

    @Test
    void getOneRaceTest(){
        Long raceId = mockRaceEntity.getId();
        assertTrue(mockRaceEntity.getId() > 0);

        RaceDTO raceDTO = raceService.findById(raceId).get();
        assertNotNull(raceDTO);

        assertEquals(raceName, raceDTO.getName());
        assertEquals(today, raceDTO.getDate());
        assertEquals(desc, raceDTO.getDescription());
        assertTrue(raceDTO.isPublic());
        assertEquals(url, raceDTO.getUrl());
        assertEquals(now, raceDTO.getModDate());

        assertNotNull(raceDTO.getAddress());
        assertEquals(LOCATION, raceDTO.getAddress().getLocation());

        assertNotNull(mockRaceEntity.getRaceTypeEntities());
        assertEquals(raceTypeEntities.size(), mockRaceEntity.getRaceTypeEntities().size());
        assertEquals(raceTypeEntities.size(), raceDTO.getRaceTypes().size());
    }

    @Test
    void getPublicAndMyRacesRacesTest() {
        assertTrue(mockRaceEntity.getId() > 0);

        Long USER_ID = 1L;
        List<MyRaceDTO> publicRaces = myRaceService.getPublicAndMyRacesRaces(USER_ID);

        assertNotNull(publicRaces);
        assertEquals(1, publicRaces.size());
    }


    @Test
    void getAllRaceTypesTest() {
        Long RaceTypeID = 100L;
        RaceTypeEntity raceTypeEntity = RaceTypeEntity.builder()
                                        .id(RaceTypeID)
                                        .name("RaceType100")
                                        .description("Description for Race 100").build();

        raceTypeRepository.save(raceTypeEntity);

        List<RaceTypeDTO> allRaceTypes = raceService.getAllRaceTypes();

        assertNotNull(allRaceTypes);
        assertFalse(allRaceTypes.isEmpty());

        boolean found = false;
        for (RaceTypeDTO raceTypeDTO : allRaceTypes) {
            if (Objects.equals(raceTypeDTO.getId(), RaceTypeID)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }
}
