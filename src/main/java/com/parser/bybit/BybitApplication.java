package com.parser.bybit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BybitApplication {

	public static void main(String[] args) {
		SpringApplication.run(BybitApplication.class, args);
	}

}
