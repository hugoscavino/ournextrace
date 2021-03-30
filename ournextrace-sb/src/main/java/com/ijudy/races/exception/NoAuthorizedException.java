package com.ijudy.races.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Could not perform action")
public class NoAuthorizedException extends RuntimeException{

     public NoAuthorizedException(String message) {
        super(message);
    }
}
