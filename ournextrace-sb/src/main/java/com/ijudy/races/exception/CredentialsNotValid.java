package com.ijudy.races.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "user id, email or password is not a valid format")
public class CredentialsNotValid extends RuntimeException  {

    private static final long serialVersionUID = -56786946333397681L;

    public CredentialsNotValid(String messages) {
        super(messages);
    }

}