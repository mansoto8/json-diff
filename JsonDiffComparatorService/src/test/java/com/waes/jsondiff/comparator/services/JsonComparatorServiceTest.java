package com.waes.jsondiff.comparator.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.waes.jsondiff.comparator.dto.JsonComparison;
import com.waes.jsondiff.comparator.dto.JsonTextPair;

/**
 * Unit tests for the comparator service 
 */
@ExtendWith(MockitoExtension.class)
public class JsonComparatorServiceTest {

	@InjectMocks
	private JsonComparatorService comparatorService;

	@Mock
	private StoreClient storeClient;

	@Mock
	private RestTemplate restTemplate;

	@Test
	public void getJsonComparison_success()
			throws Exception {
		URI mockURI = new URI("http://sample.uri");
		when(storeClient.getProfileUri()).thenReturn(mockURI);
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		JsonTextPair jsonTextResponse = new JsonTextPair(leftJson, rightJson);
		ResponseEntity<JsonTextPair> expectedResponse = new ResponseEntity<JsonTextPair>(jsonTextResponse,
				HttpStatus.OK);
		when(restTemplate.getForEntity(eq(new URI(mockURI + "/" + 1)), eq(JsonTextPair.class)))
				.thenReturn(expectedResponse);

		ResponseEntity<JsonComparison> response = comparatorService.getJsonComparison(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Both json parts are equal.", response.getBody().getJsonReport());
		assertTrue(response.getBody().isEqual());

		verify(storeClient).getProfileUri();
		verify(restTemplate).getForEntity(eq(new URI(mockURI + "/" + 1)), eq(JsonTextPair.class));
	}

	@Test
	public void getJsonDiffReport_bothJsonNull()
			throws URISyntaxException, JsonMappingException, JsonProcessingException {
		JsonTextPair jsonTextPair = new JsonTextPair(null, null);
		JsonComparison jsonComparison = comparatorService.compareJson(jsonTextPair);

		assertTrue(jsonComparison.isEqual());
		assertEquals("Both json parts are equal.", jsonComparison.getJsonReport());
	}

	@Test
	public void getJsonDiffReport_leftJsonNull()
			throws URISyntaxException, JsonMappingException, JsonProcessingException {
		JsonTextPair jsonTextPair = new JsonTextPair(null, "{}");
		JsonComparison jsonComparison = comparatorService.compareJson(jsonTextPair);

		assertFalse(jsonComparison.isEqual());
		assertEquals("Json parts are of different size.", jsonComparison.getJsonReport());
	}

	@Test
	public void getJsonDiffReport_rightJsonNull()
			throws URISyntaxException, JsonMappingException, JsonProcessingException {
		JsonTextPair jsonTextPair = new JsonTextPair("{}", null);
		JsonComparison jsonComparison = comparatorService.compareJson(jsonTextPair);

		assertFalse(jsonComparison.isEqual());
		assertEquals("Json parts are of different size.", jsonComparison.getJsonReport());
	}

	@Test
	public void getJsonDiffReport_equalParts()
			throws URISyntaxException, JsonMappingException, JsonProcessingException {
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		JsonTextPair jsonTextPair = new JsonTextPair(leftJson, rightJson);
		JsonComparison jsonComparison = comparatorService.compareJson(jsonTextPair);

		assertTrue(jsonComparison.isEqual());
		assertEquals("Both json parts are equal.", jsonComparison.getJsonReport());
	}

	@Test
	public void getJsonDiffReport_differentSizeParts()
			throws URISyntaxException, JsonMappingException, JsonProcessingException {
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\"}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}";
		JsonTextPair jsonTextPair = new JsonTextPair(leftJson, rightJson);
		JsonComparison jsonComparison = comparatorService.compareJson(jsonTextPair);

		assertFalse(jsonComparison.isEqual());
		assertEquals("Json parts are of different size.", jsonComparison.getJsonReport());
	}

	@Test
	public void getJsonDiffReport_sameSizeButDifferentAndEqualSize()
			throws URISyntaxException, JsonMappingException, JsonProcessingException {
		String leftJson = "{\r\n\t\"jsonProperty1\": \"propertyValue12345\",\r\n\t\"jsonProperty2\": \"propertyValue12345\",\r\n\t\"jsonProperty3\": \"propertyValue12345\"}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty1\": \"propertyValue1234\",\r\n\t\"jsonProperty2\": \"propertyValue123456\",\r\n\t\"jsonProperty3\": \"propertyValue12345\"}\r\n}";
		JsonTextPair jsonTextPair = new JsonTextPair(leftJson, rightJson);
		JsonComparison jsonComparison = comparatorService.compareJson(jsonTextPair);

		assertFalse(jsonComparison.isEqual());
		assertEquals("Next is the list of properties with the comparison summary: \n"
				+ "jsonProperty1: Value differs by 1 characters.\n" + "jsonProperty2: Value differs by 1 characters.\n"
				+ "jsonProperty3: Value is equal in both parts.\n", jsonComparison.getJsonReport());
	}

	@Test
	public void getJsonDiffReport_sameSizeButDifferentProperties()
			throws URISyntaxException, JsonMappingException, JsonProcessingException {
		String leftJson = "{\r\n\t\"jsonProperty\": \"Left json string 454\"}\r\n}";
		String rightJson = "{\r\n\t\"jsonProperty2\": \"Left json string 45\"}\r\n}";
		JsonTextPair jsonTextPair = new JsonTextPair(leftJson, rightJson);
		JsonComparison jsonComparison = comparatorService.compareJson(jsonTextPair);

		assertFalse(jsonComparison.isEqual());
		assertEquals("Next is the list of properties with the comparison summary: \n"
				+ "jsonProperty: Value only exists in the left part.\n"
				+ "jsonProperty2: Value only exists in the right part.\n", jsonComparison.getJsonReport());
	}
}
