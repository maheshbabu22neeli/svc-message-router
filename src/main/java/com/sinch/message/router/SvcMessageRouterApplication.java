package com.sinch.message.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SvcMessageRouterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SvcMessageRouterApplication.class, args);
	}

}
