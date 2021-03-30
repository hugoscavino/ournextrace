package com.ijudy;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = { "com.ijudy" })
@EnableWebSecurity
@PropertySource(ignoreResourceNotFound = true, value = "classpath:db.properties")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:captcha.properties")
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return configureApplication(builder);
	}

	public static void main(String[] args) {
		// https://www.baeldung.com/spring-boot-shutdown
		SpringApplicationBuilder app = new SpringApplicationBuilder(Application.class);
		app.build().addListeners(new ApplicationPidFileWriter("./bin/shutdown.pid"));
		configureApplication(app).run(args);
	}

	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
		return builder.sources(Application.class).bannerMode(Banner.Mode.CONSOLE);
	}

	@Bean
	AmazonSimpleEmailService getAmazonSimpleEmailService(){
		// Replace region with the AWS Region you're using for Amazon SES.
		// Acceptable values are EUWest1, USEast1, and USWest.
		Regions regions = Regions.US_EAST_1;

		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(regions).build();
		return client;
	};


}