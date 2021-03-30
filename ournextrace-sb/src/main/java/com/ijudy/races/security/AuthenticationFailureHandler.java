package com.ijudy.races.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
/**
 * We make sure we clean up any authentication attributes left in the HttpServletRequest instance, 
 * and by default the method will trigger a 200 status instead of a 301 normally 
 * sent by a Spring Security form login.
 * @see https://www.codesandnotes.be/2014/10/31/restful-authentication-using-spring-security-on-spring-boot-and-jquery-as-a-web-client/
 * @author hugo
 *
 */
@Slf4j
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,	HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		log.error("AuthenticationFailureHandler : " + exception.getMessage());
	    super.onAuthenticationFailure(request, response, exception);
	
	}
}
