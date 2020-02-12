package com.waes.jsondiff.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Server application that enables the cloud functionility acting as a
 * orchestrator for the registering and communication of the microservices.
 */
@SpringBootApplication
@EnableEurekaServer
public class JsonDiffEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsonDiffEurekaServerApplication.class, args);
	}

}
