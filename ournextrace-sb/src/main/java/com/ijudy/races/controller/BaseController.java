package com.ijudy.races.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ijudy.races.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

	@Autowired
	protected UserService userService;

	private final ObjectMapper mapper =  new ObjectMapper();

	protected ObjectMapper getMapper() {
	    
		// Don't throw an exception when json has extra fields you are
	    // not serializing on. This is useful when you want to use a pojo
	    // for deserialization and only care about a portion of the JSON
	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    
	    // Ignore null values when writing json.
	    mapper.setDefaultPropertyInclusion(JsonInclude.Value.construct(Include.ALWAYS, Include.NON_NULL));
	    mapper.setSerializationInclusion(Include.NON_NULL);
	    
	    // Write times as a String instead of a Long so its human-readable.
	    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	    mapper.registerModule(new JavaTimeModule());
	    
	    mapper.enable(SerializationFeature.INDENT_OUTPUT);
	    
		return mapper;
	}


}
