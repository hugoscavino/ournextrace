package com.ijudy;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.ijudy.races.repository", "com.ijudy.races.entity" })
public class IntegrationConfig {
}
