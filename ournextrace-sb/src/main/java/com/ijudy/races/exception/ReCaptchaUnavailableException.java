package com.ijudy.races.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "captcha unavailable failed")
public final class ReCaptchaUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public ReCaptchaUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
