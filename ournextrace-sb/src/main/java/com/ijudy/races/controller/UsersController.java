package com.ijudy.races.controller;

import com.ijudy.races.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/api/v2")
@Slf4j
public class UsersController extends BaseController{

    @GetMapping(value ="/users")
    @ResponseBody
    public List<UserDTO> getAllUsers() {
        return userService.findAllUsers();
    }


}
