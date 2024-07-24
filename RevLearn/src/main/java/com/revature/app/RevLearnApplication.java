package com.revature.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication (scanBasePackages = "com.revature")
@EntityScan("com.revature.models")
@EnableJpaRepositories("com.revature.repositories")
public class RevLearnApplication {

	private static final Logger logger= LoggerFactory.getLogger(RevLearnApplication.class);

	public static void main(String[] args) {

		logger.info("We can use a logger and an H2 in memory database to save money on rds costs");

		SpringApplication.run(RevLearnApplication.class, args);
	}
}