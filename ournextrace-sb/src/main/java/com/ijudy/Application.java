package com.ijudy;


import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication(scanBasePackages = { "com.ijudy" })
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
}
