package com.waes.jsondiff.left.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.waes.jsondiff.left.dto.LeftJsonText;

@Service
public class LeftJsonService {
	
	private StoreClient storeClient;
	private RestTemplate restTemplate;	

	@Autowired
	public LeftJsonService(StoreClient storeClient, RestTemplate restTemplate) {
		this.storeClient = storeClient;
		this.restTemplate = restTemplate;
	}

	public ResponseEntity<Void> persistLeftJson(long id, String jsonText) {
		LeftJsonText leftJson = new LeftJsonText(id, jsonText);
		URI storeUri = this.storeClient.getProfileUri();
		
		ResponseEntity<Boolean> idExists = restTemplate.getForEntity(storeUri + "/exists/" + id, Boolean.class);		
		ResponseEntity<Void> response = null;
		final HttpEntity<LeftJsonText> requestEntity = new HttpEntity<>(leftJson);
		
		if(idExists.getBody()) {
			response = restTemplate.exchange(storeUri.resolve("/" + id), HttpMethod.PATCH, requestEntity, Void.class);
		} else {
			response = restTemplate.exchange(storeUri, HttpMethod.POST, requestEntity, Void.class);
		}
		
		return response;
	}

}
