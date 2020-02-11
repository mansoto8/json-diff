package com.waes.jsondiff.comparator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class JsonDiffComparatorServiceApplication {

	private static final int TIMEOUT = 30000;
	
	public static void main(String[] args) {
		SpringApplication.run(JsonDiffComparatorServiceApplication.class, args);
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
