package com.revature.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication (scanBasePackages = "com.revature")
@EntityScan("com.revature.models")
@EnableJpaRepositories("com.revature.repositories")
public class RevLearnApplication {

	public static void main(String[] args) {
		SpringApplication.run(RevLearnApplication.class, args);
        System.out.println("\n------------------------------------");
        System.out.println("    Welcome to RevLearn");
        System.out.println("------------------------------------");
	}
}