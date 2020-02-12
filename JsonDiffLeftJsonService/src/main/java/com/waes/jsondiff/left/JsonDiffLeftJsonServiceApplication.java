package com.waes.jsondiff.left;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Application that exposes and endpoint for storing the left part of a json
 * pair to be compared.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class JsonDiffLeftJsonServiceApplication {

	private static final int TIMEOUT = 30000;
	
	public static void main(String[] args) {
		SpringApplication.run(JsonDiffLeftJsonServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		//Configure rest template for the whole application that allows PATCH requests
		RestTemplate restTemplate = new RestTemplate();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(TIMEOUT);
		requestFactory.setReadTimeout(TIMEOUT);
		restTemplate.setRequestFactory(requestFactory);

		return restTemplate;
	}
}
