package com.ijudy.races.service.security;

import com.ijudy.races.exception.OAuth2AuthenticationProcessingException;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.enums.SocialProvider;
import com.ijudy.races.security.GoogleOAuth2UserInfo;
import com.ijudy.races.security.OAuth2UserInfo;
import com.ijudy.races.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    public UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
 
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        final String providerName = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        final OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(providerName, oAuth2User.getAttributes());
        final String email = oAuth2UserInfo.getEmail();
        		
        Optional<UserDTO> optionalUserDTO = userService.findByEmailAndSocialProvider(email, providerName);
        UserPrincipal userPrincipal = null;
        boolean foundUserInRepoWithSameEmail = optionalUserDTO.isPresent();
        if(foundUserInRepoWithSameEmail) {
            // Found the user in the repo so just use that one
            userPrincipal = new UserPrincipal(optionalUserDTO.get());
        } else {
            UserDTO newUser = registerNewUser(providerName, oAuth2UserInfo);
            // TODO Update the user in the repo with new attributes
            // user = updateExistingUser(user, oAuth2UserInfo, provider);
            log.info("Created a new user with email " + email + " and id " + newUser.getId());
            userPrincipal =  new UserPrincipal(newUser, oAuth2User.getAttributes());
        }

        return userPrincipal;

    }

	private UserDTO registerNewUser(String providerName, OAuth2UserInfo oAuth2UserInfo) {

        final String email = oAuth2UserInfo.getEmail();
        final String name = oAuth2UserInfo.getName();

        UserDTO userDTO = UserDTO.builder()
                .email(email)
                .active(true)
                .isUser(true)
                .lastLogin(LocalDateTime.now())
                .socialProvider(providerName)
                .name(name).build();

        return userService.registerSocialUser(userDTO);
    }

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(SocialProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }

}
