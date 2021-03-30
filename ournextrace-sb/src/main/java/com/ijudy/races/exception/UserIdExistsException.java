package com.ijudy.races.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "records already exists")
public class UserIdExistsException extends RuntimeException {

	private static final long serialVersionUID = 1083710449142107081L;

	public UserIdExistsException(String email) {
		super(email);
    }
}