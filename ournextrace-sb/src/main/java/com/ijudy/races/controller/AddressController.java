package com.ijudy.races.controller;

import com.ijudy.races.dto.AddressDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.service.race.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    public Optional<AddressDTO> saveAddress(@NotNull @RequestBody  AddressDTO address, Principal principal){
        // Take form Principal and not the user's request
        Optional<UserDTO> userDTO = getUserDTO(principal);
        if (userDTO.isPresent()){
            address.setAuthorId(userDTO.get().getId());
            return Optional.of(addressService.save(address));
        } else {
            return Optional.empty();
        }
    }
}
