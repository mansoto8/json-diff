package com.waes.jsondiff.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class JsonDiffEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsonDiffEurekaServerApplication.class, args);
	}

}
