package com.ijudy.races.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Record Not Found")
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6528033894104778446L;

	public NotFoundException(String message) {
		super(message);
    }
}