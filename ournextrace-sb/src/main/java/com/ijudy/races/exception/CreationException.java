package com.ijudy.races.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Could not create record")
public class CreationException extends RuntimeException  {

	private static final long serialVersionUID = 6528033894104778446L;

	public CreationException(String core) {
		super(core);
    }
}