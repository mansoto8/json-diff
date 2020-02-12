package com.waes.jsondiff.right.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.waes.jsondiff.right.services.RightJsonService;

/**
 * Controller that exposes the endpoint for the storing in database the right
 * part of the json object pair to be compared.
 */
@RestController
public class RightJsonRest {

	@Autowired
	private RightJsonService jsonService;

	/**
	 * Receives a base64 encoded json, decode it and store it in the database as the right part.
	 * 
	 * @param id Represents the pair of json objects that are going to be compared.
	 * @param encodedJson Base64 encoded json to be compared
	 * @return A response entity with CREATED code if json stored successfully.
	 * @throws RestClientException If an error happens while accessing the store.
	 * @throws URISyntaxException If an error occurs when generating the uri for accessing the store
	 */
	@PostMapping(value = "/diff/{id}/right")
	public ResponseEntity<Object> postLeftText(@PathVariable Long id, @Valid @RequestBody String encodedJson)
			throws RestClientException, URISyntaxException {
		jsonService.persistLeftJson(id, new String(Base64.decodeBase64(encodedJson.getBytes())));

		URI location = ServletUriComponentsBuilder.fromPath("http://localhost:8083/jsontext/" + id).build().toUri();

		return ResponseEntity.created(location).build();
	}

}
