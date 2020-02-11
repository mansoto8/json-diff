package com.waes.jsondiff.right;

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

import com.waes.jsondiff.right.services.StoreClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JsonDiffRightJsonServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JsonDiffRightJsonServiceApplicationIT {
	@LocalServerPort
	private int port;

	@Autowired
	private StoreClient storeClient;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	private HttpHeaders headers = new HttpHeaders();

	@Test
	public void postRightText_patch() throws JSONException {
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\"}\r\n}";
		prepareData(leftJson, null, 300);
		
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		String encodedJson = "ew0KCSJqc29uUHJvcGVydHkiOiAiTGVmdCBqc29uIHN0cmluZyA0NTQiLA0KCSJqc29uUHJvcGVydHkyIjogew0KCQkianNvblByb3BlcnR5IjogIkxlZnQganNvbiBzdHJpbmcgNDU0Ig0KCX0NCn0=";
		HttpEntity<String> entity = new HttpEntity<String>(encodedJson, headers);

		ResponseEntity<Void> response = restTemplate.exchange(createURLWithPort("/diff/300/right"), HttpMethod.POST,
				entity, Void.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		//Validate the data stored in database
		ResponseEntity<TestJsonTextPair> jsonPair = restTemplate.getForEntity(storeClient.getProfileUri() + "/" + 300,
				TestJsonTextPair.class);
		assertEquals(leftJson, jsonPair.getBody().getLeftJson());
		String expectedRightJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		assertEquals(expectedRightJson, jsonPair.getBody().getRightJson());
	}
	
	@Test
	public void postRightText_post() throws JSONException {	
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		String encodedJson = "ew0KCSJqc29uUHJvcGVydHkiOiAiTGVmdCBqc29uIHN0cmluZyA0NTQiLA0KCSJqc29uUHJvcGVydHkyIjogew0KCQkianNvblByb3BlcnR5IjogIkxlZnQganNvbiBzdHJpbmcgNDU0Ig0KCX0NCn0=";
		HttpEntity<String> entity = new HttpEntity<String>(encodedJson, headers);

		ResponseEntity<Void> response = restTemplate.exchange(createURLWithPort("/diff/301/right"), HttpMethod.POST,
				entity, Void.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		//Validate the data stored in database
		ResponseEntity<TestJsonTextPair> jsonPair = restTemplate.getForEntity(storeClient.getProfileUri() + "/" + 301,
				TestJsonTextPair.class);
		assertEquals(null, jsonPair.getBody().getLeftJson());
		String expectedRightJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		assertEquals(expectedRightJson, jsonPair.getBody().getRightJson());
	}
	
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
