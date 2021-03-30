package com.ijudy.races.service;

import com.ijudy.races.repository.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class BaseServiceTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    RaceRepository raceRepository;

    @MockBean
    AddressRepository addressRepository;

    @MockBean
    RaceTypeRepository raceTypeRepository;

    @MockBean
    MyRaceRepository myRaceRepository;

}
