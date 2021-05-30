package com.jhop.callers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CallersApplication {

	private static final Logger logger = LoggerFactory.getLogger(CallersApplication.class);

	public static void main(String[] args) {

		logger.info("Strating Callers Application");

		SpringApplication.run(CallersApplication.class, args);
	}

}
