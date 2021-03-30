package com.ijudy.races.service;

import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.entity.UserEntity;
import com.ijudy.races.repository.UserRepository;
import com.ijudy.races.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@DisplayName("UserService Test")
public class UserServiceTest extends BaseIntegrationTest{

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private static final String email = "test1@aol.com";

    @BeforeEach
    void setMockOutput() {
        Collection<? extends GrantedAuthority > authorities = new ArrayList<GrantedAuthority>();
        User user = new User(email, "password", true, false, false, false, authorities);
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(user);

        UserEntity entity = UserEntity.builder().active(true).email(email).build();
        List<UserEntity> list = Arrays.asList(entity);
        when(userRepository.findByEmail(any())).thenReturn(list);
    }

    @Test
    void userServiceGetEmailTest() {
        final Optional<UserDTO> user = userService.findByEmailAndSocialProvider(email);
        assertEquals(email, user.get().getEmail());
    }

    @Test
    void saveInitialUserTest() {
        final String name = "Hugo Scavino";

        UserDTO userDTO = UserDTO.builder().email(email).name(name).password("password").isUser(true).build();

        UserDTO savedUserDTO = userService.registerLocalUser(userDTO);
        assertNotNull(savedUserDTO);

        assertEquals(email, savedUserDTO.getEmail());
        assertEquals(name, savedUserDTO.getName());

        assertTrue(savedUserDTO.isUser());

        assertFalse(savedUserDTO.isPowerUser());

        assertFalse(savedUserDTO.isAdmin());
    }

}
