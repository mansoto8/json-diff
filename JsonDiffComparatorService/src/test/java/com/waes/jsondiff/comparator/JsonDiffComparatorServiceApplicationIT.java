package com.waes.jsondiff.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.waes.jsondiff.comparator.dto.JsonComparison;
import com.waes.jsondiff.comparator.services.StoreClient;

/**
 * Contains the integration tests for checking the comparator behavior. As a
 * precondition for the tests to be successful the store server and the eureka
 * server must be running.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JsonDiffComparatorServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JsonDiffComparatorServiceApplicationIT {
	@LocalServerPort
	private int port;
	
	@Autowired
	private StoreClient storeClient;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testGetJsonComparison_equalJsons() throws JSONException {
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty3\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty3\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		prepareData(leftJson, rightJson, 100);
		
		ResponseEntity<JsonComparison> response = restTemplate.getForEntity(createURLWithPort("/diff/100"),
				JsonComparison.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Both json parts are equal.", response.getBody().getJsonReport());
		assertTrue(response.getBody().isEqual());
	}
	
	@Test
	public void testGetJsonComparison_differentJsons() throws JSONException {
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty3\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\"}\r\n}";
		prepareData(leftJson, rightJson, 101);
		
		ResponseEntity<JsonComparison> response = restTemplate.getForEntity(createURLWithPort("/diff/101"),
				JsonComparison.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Json parts are of different size.", response.getBody().getJsonReport());
		assertFalse(response.getBody().isEqual());
	}
	
	@Test
	public void testGetJsonComparison_equalSizeJsonsWithDifferences() throws JSONException {
		String leftJson = "{\r\n\t\"jsonProperty1\": \"propertyValue12345\",\r\n\t\"jsonProperty2\": \"propertyValue12345\",\r\n\t\"jsonProperty3\": \"propertyValue12345\"}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty1\": \"propertyValue1234\",\r\n\t\"jsonProperty2\": \"propertyValue123456\",\r\n\t\"jsonProperty3\": \"propertyValue12345\"}\r\n}";
		prepareData(leftJson, rightJson, 102);
		ResponseEntity<JsonComparison> response = restTemplate.getForEntity(createURLWithPort("/diff/102"),
				JsonComparison.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Next is the list of properties with the comparison summary: \n"
				+ "jsonProperty1: Value differs by 1 characters.\n" + "jsonProperty2: Value differs by 1 characters.\n"
				+ "jsonProperty3: Value is equal in both parts.\n", response.getBody().getJsonReport());
		assertFalse(response.getBody().isEqual());
	}
	
	/**
	 * Creates the necessary data in the database for the integration tests.
	 * @param leftJson To be stored in the database
	 * @param rightJson To be stored in the database
	 * @param id Represents the two json objects to be compared
	 */
	private void prepareData(String leftJson, String rightJson, long id) {
		restTemplate.delete(createURLWithPort("/jsontext/"+id));

		TestJsonTextPair jsonTextPair = new TestJsonTextPair(102, leftJson, rightJson);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
		final HttpEntity<TestJsonTextPair> requestEntity = new HttpEntity<>(jsonTextPair, headers);
		restTemplate.exchange(storeClient.getProfileUri(), HttpMethod.POST, requestEntity, Void.class);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
