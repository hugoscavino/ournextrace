package com.ijudy.races.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Could not update record")
public class UpdateException extends RuntimeException  {

	private static final long serialVersionUID = -56786946333397681L;

	public UpdateException(String core) {
		super(core);
    }
}