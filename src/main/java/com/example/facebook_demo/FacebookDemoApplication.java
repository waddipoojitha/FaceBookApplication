package com.example.facebook_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FacebookDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacebookDemoApplication.class, args);
	}

}