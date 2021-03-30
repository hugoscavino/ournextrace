package com.ijudy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Value( "${cors.url}")
	private String corsUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	log.info("setting cors : " + corsUrl);
    	
        registry.addMapping("/**")
        .allowedOrigins(corsUrl)
        .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("origin, content-type, accept, x-requested-with")
        .maxAge(3600)
        ;
    }

}
