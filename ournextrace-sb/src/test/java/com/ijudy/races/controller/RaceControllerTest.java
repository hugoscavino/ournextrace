package com.ijudy.races.controller;

import com.ijudy.races.dto.*;
import com.ijudy.races.MyRaceStatus;
import com.ijudy.races.service.race.AddressService;
import com.ijudy.races.service.race.MyRaceService;
import com.ijudy.races.service.race.RaceService;
import com.ijudy.races.service.user.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {RacesController.class})
@WebMvcTest
@ActiveProfiles("local")
class RaceControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RaceService raceService;

    @MockBean
    UserService userService;

    @MockBean
    MyRaceService myRaceService;

    @MockBean
    AddressService addressService;

    private final Set<RaceTypeDTO> raceTypeDTOSet = new HashSet<>(3);
    private final List<RaceTypeDTO> raceTypeList = new ArrayList<>(3);

    private final Long RACE_ID = 1L;
    private final Long USER_ID = 1L;
    private final Long ADDRESS_ID = 1L;
    private final String HOTEL_NAME = "Some Hotel";
    private final String USER_NAME = "Test User";
    private final LocalDate now = LocalDate.now();

    /**
     * <a href="https://stackoverflow.com/questions/21495296/spring-mvc-controller-test-print-the-result-json-string">...</a>
     */
    @BeforeEach
    public void setUp() {

        RaceTypeDTO raceTypeDTO = RaceTypeDTO.builder().id(40L).desc("ironman").shortDesc("140.6").name("Ironman 140.6").build();
        raceTypeDTOSet.add(raceTypeDTO);
        raceTypeList.add(raceTypeDTO);
        UserDTO userDTO = UserDTO.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .firstName("Test")
                .lastName("User")
                .build();

        List<MyRaceDTO> set = new ArrayList<>(1);
        List<MyRaceDTO> list = new ArrayList<>(1);
        String url = "www.google.com";
        String desc = "This Race is Great";
        String raceName = "Marathon Race";
        RaceDTO raceDTO = RaceDTO.builder()
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

        // Race Service
        Mockito.when(raceService.getPublicRaces()).thenReturn(list);
        Mockito.when(raceService.findById(any())).thenReturn(Optional.of(raceDTO));
        Mockito.when(raceService.getAllRaceTypes()).thenReturn(raceTypeList);

        // MyRace Service
        Mockito.when(myRaceService.getPublicAndMyRacesRaces(RACE_ID)).thenReturn(set);
        Mockito.when(myRaceService.getMyRace(any())).thenReturn(Optional.of(myRaceDTO));

    }

    @Test
    void getPublicEvents() throws Exception {

        ResultActions actions = mockMvc.perform(
                get("/api/v2/public_races")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].race.id", CoreMatchers.is(RACE_ID.intValue())))
               .andExpect(jsonPath("$[0].race.address.id", CoreMatchers.is(ADDRESS_ID.intValue())))
               .andExpect(jsonPath("$[0].user.id", CoreMatchers.is(USER_ID.intValue())))
               .andExpect(jsonPath("$[0].user.name", CoreMatchers.is(USER_NAME)));

        actions.andExpect(
                status().isOk());
    }

    @Test
    void getPublicAndMyEvents() throws Exception {

        ResultActions actions = mockMvc.perform(
                get("/api/v2/myraces")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$[0].race.id", CoreMatchers.is(RACE_ID.intValue())))
                        .andExpect(jsonPath("$[0].race.address.id", CoreMatchers.is(ADDRESS_ID.intValue())))
                        .andExpect(jsonPath("$[0].user.id", CoreMatchers.is(USER_ID.intValue())))
                        .andExpect(jsonPath("$[0].user.name", CoreMatchers.is(USER_NAME)));

        actions.andExpect(
                status().isOk());
    }

    @Test
    void getAllRaceTypes() throws Exception {

        mockMvc.perform(
                get("/api/v2/racetypes")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()",is(raceTypeList.size()))
                );

    }

    @Test
    void getOneRace() throws Exception {

        mockMvc.perform(
                        get("/api/v2/race/{raceId}", RACE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(jsonPath("$.id", CoreMatchers.is(RACE_ID.intValue())))
                        .andExpect(status().isOk());

    }

    @Test
    void getOneMyRace() throws Exception {

        ResultActions actions = mockMvc.perform(
                get("/api/v2/myrace/{raceId}", RACE_ID)
                        .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(
                status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.myRaceStatus", CoreMatchers.is(MyRaceStatus.GOING.getKey())))
                .andExpect(jsonPath("$.hotelName", CoreMatchers.is(HOTEL_NAME)))
                .andExpect(jsonPath("$.race.id", CoreMatchers.is(RACE_ID.intValue())));

    }

}
