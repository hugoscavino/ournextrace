package com.ijudy.races.service.security;

import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.enums.SocialProvider;
import com.ijudy.races.service.BaseServiceTest;
import com.ijudy.races.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@DisplayName("UserPrincipalServiceTest Test")
public class UserPrincipalServiceTest extends BaseServiceTest {

    @MockBean
    private UserService userService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    final static String email = "test1@aol.com";
    final static String fullName = "Hugp Scavino";

    @BeforeEach
    void setup() {
        UserDTO user = UserDTO.builder().isUser(true).isPowerUser(false).active(true).id(1L).email(email).name(fullName).build();
        when(userService.findByEmailAndSocialProvider(email, SocialProvider.ijudy.toString())).thenReturn(Optional.of(user));
    }

    @Test
    void loadUserByUsernameTest() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);

        assertTrue(authorities.contains(CustomGrantedAuthority.USER));
    }
}
