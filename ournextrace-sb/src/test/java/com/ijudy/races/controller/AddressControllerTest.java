package com.ijudy.races.controller;

import com.ijudy.races.dto.AddressDTO;
import com.ijudy.races.service.race.AddressService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AddressController.class, useDefaultFilters = false)
public class AddressControllerTest {


    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AddressService addressService;

    private MockMvc mockMvc;

    private static final String LOCATION = "Some Location";
    private final Long ADDRESS_ID = 1L;

   @BeforeEach
   public void setUp() {
            mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .alwaysDo(MockMvcResultHandlers.print())
                    .build();

       AddressDTO addressDTO = AddressDTO.builder().id(ADDRESS_ID).location(LOCATION).build();
       List<AddressDTO> list = Collections.singletonList(addressDTO);

       // Address Service
       Mockito.when(addressService.getAllAddresses()).thenReturn(list);
   }

    @Test
    void getAllAddresses() throws Exception {

        ResultActions actions = mockMvc.perform(
                get("/api/v2/addresses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", CoreMatchers.is(ADDRESS_ID.intValue())))
                .andExpect(jsonPath("$[0].location", CoreMatchers.is(LOCATION)));

        actions.andExpect(
                status().isOk());
    }
}
