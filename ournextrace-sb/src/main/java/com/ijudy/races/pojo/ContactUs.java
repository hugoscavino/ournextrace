package com.ijudy.races.pojo;

import com.github.ankurpathak.password.bean.constraints.NotContainWhitespace;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public @Data class ContactUs {

	@NotNull
	@Size(min=8, max=128)
    @NotContainWhitespace
	private String email;
	
	@NotNull
	@Size(min=4, max=1024)
	private String message;

	@NotNull
	private String captchaResponse;
	
}
