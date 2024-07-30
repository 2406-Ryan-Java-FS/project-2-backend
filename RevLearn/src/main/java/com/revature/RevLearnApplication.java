package com.revature;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringUtils;

import java.util.Arrays;

//This is in com.revature it will now scan everything
@SpringBootApplication
public class RevLearnApplication {

	private static final Logger logger= LoggerFactory.getLogger(RevLearnApplication.class);

	public static void main(String[] args) {
		logger.info("We can use a logger and an H2 in memory database to save money on rds costs");
		logger.info("args="+Arrays.asList(args));
		SpringApplication.run(RevLearnApplication.class, args);
	}

	//WATCH OUT!!! Must prepend with "spring." to bring in properties
	//correctly when using external properties files not bundled with the jar
	@Value("${JWT_TOKEN:null1}") String JWT_TOKEN;
	@Value("${spring.jwt.secret:null1}") String jwtSecretSpringProfile;
	@Value("${spring.datasource.username:null1}") String dataSource;

	@PostConstruct
	public void postBeansExist(){
		logger.info("JWT_TOKEN="+StringUtils.truncate(JWT_TOKEN,33));
		logger.info("jwtSpringProfile length="+jwtSecretSpringProfile.length());
		logger.info("jwtSpringProfile="+StringUtils.truncate(jwtSecretSpringProfile,33));
		logger.info("dataSource="+dataSource);
	}
}