package com.waes.jsondiff.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JsonDiffComparatorServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JsonDiffComparatorServiceApplicationIT {
	@LocalServerPort
	private int port;
	
	@Autowired
	private StoreClient storeClient;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testGetJsonComparison() throws JSONException {
		prepareData();
		ResponseEntity<JsonComparison> response = restTemplate.getForEntity(createURLWithPort("/diff/100"),
				JsonComparison.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Both json parts are equal.", response.getBody().getJsonReport());
		assertTrue(response.getBody().isEqual());
	}
	
	private void prepareData() {
		restTemplate.delete(createURLWithPort("/jsontext/100"));
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty3\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty3\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		TestJsonTextPair jsonTextPair = new TestJsonTextPair(100, leftJson, rightJson);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
		final HttpEntity<TestJsonTextPair> requestEntity = new HttpEntity<>(jsonTextPair, headers);
		System.out.println("URL: " + storeClient.getProfileUri() + "/100");
		restTemplate.exchange(storeClient.getProfileUri(), HttpMethod.POST, requestEntity, Void.class);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
	
	public class TestJsonTextPair {
		private long id;
		private String leftJson;
		private String rightJson;
		public TestJsonTextPair(long id, String leftJson, String rightJson) {
			super();
			this.id = id;
			this.leftJson = leftJson;
			this.rightJson = rightJson;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getLeftJson() {
			return leftJson;
		}
		public void setLeftJson(String leftJson) {
			this.leftJson = leftJson;
		}
		public String getRightJson() {
			return rightJson;
		}
		public void setRightJson(String rightJson) {
			this.rightJson = rightJson;
		}
		
	}
}
