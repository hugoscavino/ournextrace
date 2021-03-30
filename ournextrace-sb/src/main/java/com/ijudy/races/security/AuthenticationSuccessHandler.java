package com.ijudy.races.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.service.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * We make sure we clean up any authentication attributes left in the
 * HttpServletRequest instance, and by default the method will trigger a 200
 * status instead of a 301 normally sent by a Spring Security form login.
 *
 * See below for more details
 * https://www.codesandnotes.be/2014/10/31/restful-authentication-using-spring-security-on-spring-boot-and-jquery-as-a-web-client/
 * @author hugo
 *
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Value( "${login.postsignin.url}")
	private String socialPostSignInUrl;

    @Value( "${login.postsignin.page}")
    private String socialPostsigninPage;

    @Autowired
    private final ObjectMapper objectMapper;

    public AuthenticationSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Object p = authentication.getPrincipal();
        if (p instanceof UserPrincipal){
            UserPrincipal up = (UserPrincipal)p;
            UserDTO userDTO = up.getUserDTO();
            userDTO.setPassword("");
        } else {
            log.debug("Not an instanceOf UserPrincipal. Loaded " + p.getClass().getSimpleName());
        }

        String targetUrl = socialPostSignInUrl + socialPostsigninPage;
        redirectStrategy.sendRedirect(request, response, targetUrl);
        clearAuthenticationAttributes(request);

    }


}
