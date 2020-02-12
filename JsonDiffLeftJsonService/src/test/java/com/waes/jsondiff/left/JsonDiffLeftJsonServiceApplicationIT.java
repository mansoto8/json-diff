package com.waes.jsondiff.left;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.waes.jsondiff.left.services.StoreClient;

/**
 * Integration test for testing the endpoint that stores the left part of the
 * json pair to be compared.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JsonDiffLeftJsonServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JsonDiffLeftJsonServiceApplicationIT {
	@LocalServerPort
	private int port;
	
	@Autowired
	private StoreClient storeClient;

	//Rest template for executing HTTP requests in the test
	private TestRestTemplate restTemplate = new TestRestTemplate();

	private HttpHeaders headers = new HttpHeaders();

	@Test
	public void postLeftText_patch() throws JSONException {
		String rightJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\"}\r\n}";
		prepareData(null, rightJson, 200);
		
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		String encodedJson = "ew0KCSJqc29uUHJvcGVydHkiOiAiTGVmdCBqc29uIHN0cmluZyA0NTQiLA0KCSJqc29uUHJvcGVydHkyIjogew0KCQkianNvblByb3BlcnR5IjogIkxlZnQganNvbiBzdHJpbmcgNDU0Ig0KCX0NCn0=";
		HttpEntity<String> entity = new HttpEntity<String>(encodedJson, headers);

		ResponseEntity<Void> response = restTemplate.exchange(createURLWithPort("/diff/200/left"), HttpMethod.POST,
				entity, Void.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		//Validate the data stored in database
		ResponseEntity<TestJsonTextPair> jsonPair = restTemplate.getForEntity(storeClient.getProfileUri() + "/" + 200,
				TestJsonTextPair.class);
		assertEquals(rightJson, jsonPair.getBody().getRightJson());
		String expectedLeftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		assertEquals(expectedLeftJson, jsonPair.getBody().getLeftJson());
	}
	
	@Test
	public void postLeftText_post() throws JSONException {	
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		String encodedJson = "ew0KCSJqc29uUHJvcGVydHkiOiAiTGVmdCBqc29uIHN0cmluZyA0NTQiLA0KCSJqc29uUHJvcGVydHkyIjogew0KCQkianNvblByb3BlcnR5IjogIkxlZnQganNvbiBzdHJpbmcgNDU0Ig0KCX0NCn0=";
		HttpEntity<String> entity = new HttpEntity<String>(encodedJson, headers);

		ResponseEntity<Void> response = restTemplate.exchange(createURLWithPort("/diff/201/left"), HttpMethod.POST,
				entity, Void.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		//Validate the data stored in database
		ResponseEntity<TestJsonTextPair> jsonPair = restTemplate.getForEntity(storeClient.getProfileUri() + "/" + 201,
				TestJsonTextPair.class);
		assertEquals(null, jsonPair.getBody().getRightJson());
		String expectedLeftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		assertEquals(expectedLeftJson, jsonPair.getBody().getLeftJson());
	}
	
	/**
	 * Prepares the data necessary for the succesfull execution of the tests. It may
	 * delete data already stored in the database and recreate it for ensuring the
	 * proper structuring for the tests.
	 * 
	 * @param leftJson left part of the json pair to be compared
	 * @param rightJson right part of the json pair to be compared
	 * @param id Identifies the json pair to be compared
	 */
	private void prepareData(String leftJson, String rightJson, long id) {
		restTemplate.delete(createURLWithPort("/jsontext/"+id));

		TestJsonTextPair jsonTextPair = new TestJsonTextPair(id, leftJson, rightJson);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
		final HttpEntity<TestJsonTextPair> requestEntity = new HttpEntity<>(jsonTextPair, headers);
		restTemplate.exchange(storeClient.getProfileUri(), HttpMethod.POST, requestEntity, Void.class);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
