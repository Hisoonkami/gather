package com.adev.gather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableEurekaClient
@EnableJpaAuditing
@EnableMongoAuditing
@ComponentScan
public class GatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatherApplication.class, args);
	}

}
