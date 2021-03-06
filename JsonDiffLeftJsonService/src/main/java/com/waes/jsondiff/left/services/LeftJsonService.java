package com.waes.jsondiff.left.services;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.waes.jsondiff.left.dto.LeftJsonText;

/**
 * Service that contains the logic for the storing in database the left part of
 * the json object pair to be compared.
 */
@Service
public class LeftJsonService {

	private StoreClient storeClient;
	private RestTemplate restTemplate;

	@Autowired
	public LeftJsonService(StoreClient storeClient, RestTemplate restTemplate) {
		this.storeClient = storeClient;
		this.restTemplate = restTemplate;
	}

	/**
	 * Stores in database the left part of the json object passed as parameter. If there is
	 * already an object with the same id a patch is executed so that only the left part is
	 * updated otherwise a post is executed to create a new json pair in the database. 
	 * 
	 * @param id Represents the pair of json objects that are going to be compared.
	 * @param jsonText Json text to be stored as the left part 
	 * @return A response entity with the HTTP status of the operation
	 * @throws RestClientException If an error happens while accessing the store.
	 * @throws URISyntaxException If an error occurs when generating the uri for accessing the store
	 */
	public ResponseEntity<Void> persistLeftJson(long id, String jsonText)
			throws RestClientException, URISyntaxException {
		LeftJsonText leftJson = new LeftJsonText(id, jsonText);
		URI storeUri = this.storeClient.getProfileUri();

		ResponseEntity<Boolean> idExists = restTemplate.getForEntity(storeUri + "/exists/" + id, Boolean.class);
		ResponseEntity<Void> response = null;
		final HttpEntity<LeftJsonText> requestEntity = new HttpEntity<>(leftJson);

		if (idExists.getBody()) {
			response = restTemplate.exchange(new URI(storeUri + "/" + id), HttpMethod.PATCH, requestEntity, Void.class);
		} else {
			response = restTemplate.exchange(storeUri, HttpMethod.POST, requestEntity, Void.class);
		}

		return response;
	}

}
