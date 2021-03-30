package com.ijudy.races.security;

import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.service.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserUtilsTest {

    @Test
    void getSocialUserKeyOAuth2AuthenticationToken() {
        final String socialProvider = "google";
        final String email = "test@aol.com";
        OAuth2AuthenticationToken mockToken = mock(OAuth2AuthenticationToken.class);

        UserDTO userDTO = UserDTO.builder().isUser(true).socialProvider(socialProvider).email(email).password("password").build();
        UserPrincipal up = new UserPrincipal(userDTO);
        when(mockToken.getPrincipal()).thenReturn(up);

        final SocialUserKey socialUserKey = UserUtils.getSocialUserKey(mockToken);
        assertThat(socialUserKey).isNotNull();
        assertThat(socialUserKey.email).isEqualTo(email);
        assertThat(socialUserKey.socialProvider).isEqualTo(socialProvider);
    }

    @Test
    void getSocialUserKeyUsernamePasswordAuthenticationToken() {
        final String socialProvider = "ijudy";
        final String email = "test@aol.com";
        UsernamePasswordAuthenticationToken mockToken = mock(UsernamePasswordAuthenticationToken.class);

        UserDTO userDTO = UserDTO.builder().isUser(true).email(email).password("password").build();
        UserPrincipal up = new UserPrincipal(userDTO);
        when(mockToken.getPrincipal()).thenReturn(up);

        final SocialUserKey socialUserKey = UserUtils.getSocialUserKey(mockToken);
        assertThat(socialUserKey).isNotNull();
        assertThat(socialUserKey.email).isEqualTo(email);
        assertThat(socialUserKey.socialProvider).isEqualTo(socialProvider);
    }

    @Test
    void getSocialUserKeyUsernameNotFoundException() {
        Exception exception1 = assertThrows( UsernameNotFoundException.class, () -> {
            UsernamePasswordAuthenticationToken mockToken = null;
            UserUtils.getSocialUserKey(mockToken);
        });
        assertThat(exception1.getMessage()).isEqualTo("principal was null");

        String principalName = "Nae";
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn(principalName);

        Exception exception2 = assertThrows( UsernameNotFoundException.class, () -> {
            UserUtils.getSocialUserKey(mockPrincipal);
        });

        assertThat(exception2.getMessage()).isEqualTo("principal " + mockPrincipal.getName() + " was not of a known type");
    }

}