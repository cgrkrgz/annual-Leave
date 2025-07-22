package com.annualLeave.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@EntityScan(basePackages = "com.annualLeave")
@ComponentScan(basePackages = "com.annualLeave")
@EnableJpaRepositories(basePackages = "com.annualLeave")
@SpringBootApplication
public class AnnualLeaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnnualLeaveApplication.class, args);
	}

}
