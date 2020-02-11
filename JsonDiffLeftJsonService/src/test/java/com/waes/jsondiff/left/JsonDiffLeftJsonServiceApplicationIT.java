package com.waes.jsondiff.left;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JsonDiffLeftJsonServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JsonDiffLeftJsonServiceApplicationIT {
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	@Test
	public void testPostLeftJson() throws JSONException {
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		String encodedJson = "ew0KCSJqc29uUHJvcGVydHkiOiAiTGVmdCBqc29uIHN0cmluZyA0NTQiLA0KCSJqc29uUHJvcGVydHkyIjogew0KCQkianNvblByb3BlcnR5IjogIkxlZnQganNvbiBzdHJpbmcgNDU0Ig0KCX0NCn0=";
		HttpEntity<String> entity = new HttpEntity<String>(encodedJson, headers);

		ResponseEntity<Void> response = restTemplate.exchange(createURLWithPort("/diff/2/left"), HttpMethod.POST,
				entity, Void.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
