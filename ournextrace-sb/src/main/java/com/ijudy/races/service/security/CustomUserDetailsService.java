package com.ijudy.races.service.security;

import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.enums.SocialProvider;
import com.ijudy.races.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    public CustomUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        Optional<UserDTO> optionalUserDTO = userService.findByEmailAndSocialProvider(email, SocialProvider.ijudy.toString());

        if (optionalUserDTO.isEmpty()) {
            log.error("Could not find the user with email " + email + " and local provider " + SocialProvider.ijudy.toString());
            throw new UsernameNotFoundException(email);
        } else {
            UserDTO userDTO = optionalUserDTO.get();
            log.info("loaded user " + userDTO.getName() + " with email " + userDTO.getEmail());
            return new UserPrincipal(userDTO);
        }

    }

}
