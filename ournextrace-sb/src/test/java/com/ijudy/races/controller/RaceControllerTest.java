package com.ijudy.races.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijudy.races.dto.*;
import com.ijudy.races.enums.MyRaceStatus;
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
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {RacesController.class})
@WebMvcTest
@WithMockUser(username = "hugo@scavino.org", authorities={"ADMIN"})
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private RaceDTO raceDTO = null;
    private UserDTO userDTO = null;

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
        userDTO = UserDTO.builder()
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
    void updateRaceLocation() throws Exception {

        mockMvc.perform(
                put("/api/v2/race/{raceId}/{addressId}", RACE_ID, ADDRESS_ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.address.id", CoreMatchers.is(ADDRESS_ID.intValue())))
                .andExpect(jsonPath("$.id", CoreMatchers.is(RACE_ID.intValue())))
                .andReturn();
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

    @Test
    @WithMockUser(username = "hugo@scavino.org", authorities={"USER"})
    void saveMyRace() throws Exception {
        MyRaceDTO myRaceDTO = MyRaceDTO.builder()
                .raceDTO(raceDTO)
                .userDTO(userDTO)
                .myRaceStatus(MyRaceStatus.INTERESTED)
                .cost(300F)
                .isPaid(true)
                .notes("Saved Notes")
                .registrationDate(LocalDateTime.now())
                .hotelName("Saved Hotel")
                .raceTypes(raceTypeDTOSet)
                .build();

        String json = objectMapper.writeValueAsString(myRaceDTO);

        ResultActions actions = mockMvc.perform(
                post("/api/v2/myrace")
                        .content(json)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        );

        actions.andExpect(
                status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.race.id", CoreMatchers.is(RACE_ID.intValue())))
                .andExpect(jsonPath("$.race.address.id", CoreMatchers.is(ADDRESS_ID.intValue())))
                .andExpect(jsonPath("$.user.id", CoreMatchers.is(USER_ID.intValue())))
                .andExpect(jsonPath("$.user.name", CoreMatchers.is(USER_NAME)));
    }

    @Test
    @WithMockUser(username = "hugo@scavino.org", authorities={"USER"})
    void deleteMyRace() throws Exception {
        MyRaceDTO myRaceDTO = MyRaceDTO.builder()
                .raceDTO(raceDTO)
                .userDTO(userDTO)
                .myRaceStatus(MyRaceStatus.INTERESTED)
                .cost(300F)
                .isPaid(true)
                .notes("Saved Notes")
                .registrationDate(LocalDateTime.now())
                .hotelName("Saved Hotel")
                .raceTypes(raceTypeDTOSet)
                .build();

        // String json = objectMapper.writeValueAsString(myRaceDTO);

        ResultActions actions = mockMvc.perform(
                delete("/api/v2/myrace/" + myRaceDTO.getRaceDTO().getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "hugo@scavino.org", authorities={"USER"})
    void saveRace() throws Exception {

        final String RACE_NAME = "Some Race Name";
        final String RACE_DESC = "Some Race Description";
        RaceDTO raceDTO = RaceDTO.builder()
                .id(RACE_ID)
                .name(RACE_NAME)
                .description(RACE_DESC)
                .date(now)
                .raceTypes(raceTypeDTOSet)
                .build();

        Mockito.when(raceService.save(any())).thenReturn(raceDTO);

        String json = objectMapper.writeValueAsString(raceDTO);

        ResultActions actions = mockMvc.perform(
                post("/api/v2/race")
                        .content(json)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(
                status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", CoreMatchers.is(RACE_NAME)))
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.id", CoreMatchers.is(RACE_ID.intValue())));

    }
}
