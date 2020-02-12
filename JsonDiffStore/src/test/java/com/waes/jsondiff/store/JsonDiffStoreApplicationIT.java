package com.waes.jsondiff.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.waes.jsondiff.store.entities.JsonTextPair;
import com.waes.jsondiff.store.repositories.JsonTextPairRepository;

/**
 * Integration tests for the endpoints used by other services. 
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JsonDiffStoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JsonDiffStoreApplicationIT {
	@LocalServerPort
	private int port;
	
	@Autowired
	private JsonTextPairRepository repository;
	
	private TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	public void existsById_true() throws JSONException {
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\"}\r\n}";
		JsonTextPair jsonTextPair = new JsonTextPair();
		jsonTextPair.setId(150);
		jsonTextPair.setLeftJson(leftJson);
		jsonTextPair.setRightJson(null);
		repository.save(jsonTextPair);

		ResponseEntity<Boolean> response = restTemplate.getForEntity(createURLWithPort("/jsontext/exists/150"), Boolean.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody());
	}
	
	@Test
	public void existsById_false() throws JSONException {
		ResponseEntity<Boolean> response = restTemplate.getForEntity(createURLWithPort("/jsontext/exists/151"), Boolean.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody());
	}
	
	@Test
	public void save() throws JSONException {
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\"}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty\": \"Right json string 454\"}\r\n}";
		JsonTextPair jsonTextPair = new JsonTextPair();
		jsonTextPair.setId(152);
		jsonTextPair.setLeftJson(leftJson);
		jsonTextPair.setRightJson(rightJson);
		HttpEntity<JsonTextPair> entity = new HttpEntity<>(jsonTextPair);
		
		ResponseEntity<JsonTextPair> response = restTemplate.postForEntity(createURLWithPort("/jsontext/"), entity, JsonTextPair.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Optional<JsonTextPair> actual = repository.findById(152L);
		assertEquals(leftJson, actual.get().getLeftJson());
		assertEquals(rightJson, actual.get().getRightJson());
	}
	
	@Test
	public void patchUpdate() throws JSONException {
		String leftJson = null;
		String rightJson = "{\r\n\t\"jsonProperty\": \"Right json string 454\"}\r\n}";
		JsonTextPair jsonTextPair = new JsonTextPair();
		jsonTextPair.setId(153);
		jsonTextPair.setLeftJson(leftJson);
		jsonTextPair.setRightJson(rightJson);
		repository.save(jsonTextPair);
		String leftJsonNew = "{\r\n\t\"jsonProperty\": \"Left json string 454\"}\r\n}";

		LeftJsonText leftJsonRequest = new LeftJsonText(153, leftJsonNew);
		HttpEntity<LeftJsonText> entity = new HttpEntity<>(leftJsonRequest);
		
		ResponseEntity<JsonTextPair> response = restTemplate.exchange(createURLWithPort("/jsontext/153"), HttpMethod.PATCH, entity, JsonTextPair.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		Optional<JsonTextPair> actual = repository.findById(153L);
		assertEquals(leftJsonNew, actual.get().getLeftJson());
		assertEquals(rightJson, actual.get().getRightJson());
	}
	
	@Test
	public void findById() throws JSONException {
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\"}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty\": \"Right json string 454\"}\r\n}";
		JsonTextPair jsonTextPair = new JsonTextPair();
		jsonTextPair.setId(154);
		jsonTextPair.setLeftJson(leftJson);
		jsonTextPair.setRightJson(rightJson);
		repository.save(jsonTextPair);
		
		ResponseEntity<JsonTextPair> response = restTemplate.getForEntity(createURLWithPort("/jsontext/154"), JsonTextPair.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(jsonTextPair.getLeftJson(), response.getBody().getLeftJson());
		assertEquals(jsonTextPair.getRightJson(), response.getBody().getRightJson());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
