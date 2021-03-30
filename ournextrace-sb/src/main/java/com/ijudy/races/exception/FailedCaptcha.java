package com.ijudy.races.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Captcha Failed for User")
public class FailedCaptcha extends RuntimeException  {

	private static final long serialVersionUID = -56786946333397681L;

	public FailedCaptcha(String messages) {
		super(messages);
    }

}