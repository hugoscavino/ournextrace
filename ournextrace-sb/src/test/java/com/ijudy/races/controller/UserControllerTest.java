package com.ijudy.races.controller;

import com.ijudy.races.dto.*;
import com.ijudy.races.enums.MyRaceStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UsersController.class, useDefaultFilters = false)
@WithMockUser(username = "hugo@scavino.org", authorities={"ADMIN"})
public class UserControllerTest extends ControllerBaseTest {

    /**
     * <a href="https://stackoverflow.com/questions/21495296/spring-mvc-controller-test-print-the-result-json-string">...</a>
     */
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(MockMvcResultHandlers.print())
                .apply(springSecurity())
                .build();

        raceTypeDTO = RaceTypeDTO.builder().id(40L).build();
        raceTypeDTOSet.add(raceTypeDTO);
        raceTypeList.add(raceTypeDTO);
        userDTO = UserDTO.builder()
                .id(USER_ID)
                .email(EMAIL)
                .name(FULL_NAME)
                .active(true)
                .isAdmin(false)
                .isPowerUser(false)
                .firstName("Test")
                .lastName("User")
                .build();

        List<MyRaceDTO> set = new ArrayList<>(1);
        List<MyRaceDTO> list = new ArrayList<>(1);
        raceDTO = RaceDTO.builder()
                .id(RACE_ID)
                .name(raceName)
                .date(now)
                .description(desc)
                .isPublic(true)
                .url(url).build();

        AddressDTO addressDTO = AddressDTO.builder().id(ADDRESS_ID).location("Some Location").build();
        raceDTO.setAddress(addressDTO);


        MyRaceDTO myRaceDTO = MyRaceDTO.builder()
                .raceDTO(raceDTO)
                .userDTO(userDTO)
                .myRaceStatus(MyRaceStatus.GOING)
                .cost(100F)
                .isPaid(false)
                .notes("Some Notes")
                .registrationDate(LocalDateTime.now())
                .hotelName(HOTEL_NAME)
                .raceTypes(raceTypeDTOSet)
                .build();

        set.add(myRaceDTO);
        list.add(myRaceDTO);


    }

    @Test
    void getPrincipal() throws Exception {
        ResultActions actions = mockMvc.perform(
                get("/api/v2/principal")
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

        actions.andExpect(status().isOk())
       .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
       .andExpect(jsonPath("$.active", CoreMatchers.is(true)))
       .andExpect(jsonPath("$.email", CoreMatchers.is(EMAIL)));
    }
}
