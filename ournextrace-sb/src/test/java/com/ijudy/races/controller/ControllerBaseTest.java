package com.ijudy.races.controller;

import com.ijudy.races.dto.RaceDTO;
import com.ijudy.races.dto.RaceTypeDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ControllerBaseTest {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @MockBean
    UserService userService;

     RaceDTO raceDTO = null;
     UserDTO userDTO = null;

     RaceTypeDTO raceTypeDTO = null;
     Set<RaceTypeDTO> raceTypeDTOSet = new HashSet<>(3);
     List<RaceTypeDTO> raceTypeList = new ArrayList<>(3);

     final Long RACE_ID = 1L;
     final Long USER_ID = 1L;
     final Long ADDRESS_ID = 1L;
     final String EMAIL = "jdoe@company.com";
     final String HOTEL_NAME = "Some Hotel";
     final String FULL_NAME = "John Doe";
     final String raceName = "Some Race";
     final String desc = "This Race is Great";
     final LocalDate now = LocalDate.now();
     final String url = "www.google.com";
}
