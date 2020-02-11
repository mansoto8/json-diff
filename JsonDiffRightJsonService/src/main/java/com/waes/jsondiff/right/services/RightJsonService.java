package com.waes.jsondiff.right.services;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.waes.jsondiff.right.dto.RightJsonText;

@Service
public class RightJsonService {
	
	private StoreClient storeClient;
	private RestTemplate restTemplate;	

	@Autowired
	public RightJsonService(StoreClient storeClient, RestTemplate restTemplate) {
		this.storeClient = storeClient;
		this.restTemplate = restTemplate;
	}

	public ResponseEntity<Void> persistLeftJson(long id, String jsonText) throws RestClientException, URISyntaxException {
		RightJsonText rightJson = new RightJsonText(id, jsonText);
		URI storeUri = this.storeClient.getProfileUri();
		
		ResponseEntity<Boolean> idExists = restTemplate.getForEntity(storeUri + "/exists/" + id, Boolean.class);		
		ResponseEntity<Void> response = null;
		final HttpEntity<RightJsonText> requestEntity = new HttpEntity<>(rightJson);
		
		if(idExists.getBody()) {
			response = restTemplate.exchange(new URI(storeUri + "/" + id), HttpMethod.PATCH, requestEntity, Void.class);
		} else {
			response = restTemplate.exchange(storeUri, HttpMethod.POST, requestEntity, Void.class);
		}
		
		return response;
	}

}
