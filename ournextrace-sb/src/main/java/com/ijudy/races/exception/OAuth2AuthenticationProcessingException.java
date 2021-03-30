package com.ijudy.races.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "oAuth2 processing failed")
public class OAuth2AuthenticationProcessingException extends AuthenticationException {

	private static final long serialVersionUID = -1588102429618579847L;
    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }

}
