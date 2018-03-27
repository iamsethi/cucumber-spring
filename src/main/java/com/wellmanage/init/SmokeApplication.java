package com.wellmanage.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class SmokeApplication  {
	
	public static void main(String[] args) {
		SpringApplication.run(SmokeApplication.class, args);
	}
}
