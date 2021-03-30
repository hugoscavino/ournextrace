package com.ijudy.races.security;

import com.ijudy.races.enums.SocialProvider;
import com.ijudy.races.service.security.UserPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.security.Principal;

@UtilityClass
public class UserUtils {

    /**
     * Utility to convert Principal to its email
     * @param principal from Spring Security
     * @return The user's email
     */
    public static SocialUserKey getSocialUserKey(Principal principal) {

        SocialUserKey socialUserKey = new SocialUserKey();

        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            UserPrincipal up = (UserPrincipal) oAuth2AuthenticationToken.getPrincipal();
            socialUserKey.email = up.getUsername();
            socialUserKey.socialProvider = up.getSocialProvider();

        } else if (principal instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
            Object user = usernamePasswordAuthenticationToken.getPrincipal();
            if (user instanceof User) {
                socialUserKey.email = ((User) user).getUsername();
                socialUserKey.socialProvider = SocialProvider.ijudy.toString();
            } else if (user instanceof UserPrincipal) {
                socialUserKey.email = ((UserPrincipal)user).getUsername();
                socialUserKey.socialProvider = SocialProvider.ijudy.toString();
            } else {
                throw new UsernameNotFoundException("User was not of a known type was " + user.getClass().getSimpleName());
            }
        } else if(principal == null) {
            throw new UsernameNotFoundException("principal was null");
        } else {
            throw new UsernameNotFoundException("principal " + principal.getName() + " was not of a known type");
        }

        return socialUserKey;

    }

}

