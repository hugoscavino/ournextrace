package com.ijudy.races.controller;

import com.ijudy.races.dto.AddressDTO;
import com.ijudy.races.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value="/api/v2")
@Slf4j
public class AddressController extends BaseController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(value = "/addresses")
    @ResponseBody
    public List<AddressDTO> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @PostMapping(value = "/address")
    @ResponseBody
    public AddressDTO save(@RequestBody AddressDTO dto) {
        return this.addressService.save(dto);
    }

    @PutMapping(value = "/address")
    @ResponseBody
    public AddressDTO update(@RequestBody AddressDTO dto) {
        return this.addressService.save(dto);
    }

}
