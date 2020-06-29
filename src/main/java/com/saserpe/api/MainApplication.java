package com.saserpe.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MainApplication {
	private static final Logger logger = LogManager.getLogger(MainApplication.class);

	public static void main(String[] args) {
		logger.debug("MAIN YEI log");
		SpringApplication.run(MainApplication.class, args);
	}

}
