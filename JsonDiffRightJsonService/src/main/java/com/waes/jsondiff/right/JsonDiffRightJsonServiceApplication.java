package com.waes.jsondiff.right;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Application that exposes and endpoint for storing the right part of a json
 * pair to be compared.
 */
@SpringBootApplication
public class JsonDiffRightJsonServiceApplication {

	private static final int TIMEOUT = 30000;
	
	public static void main(String[] args) {
		SpringApplication.run(JsonDiffRightJsonServiceApplication.class, args);
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
