package com.waes.jsondiff.left.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class LeftJsonServiceTest {

	@InjectMocks
	private LeftJsonService jsonService;

	@Mock
	private StoreClient storeClient;

	@Mock
	private RestTemplate restTemplate;

	@Test
	public void persistLeftJson_patch() throws URISyntaxException {
		URI mockURI = new URI("http://sample.uri");
		when(storeClient.getProfileUri()).thenReturn(mockURI);
		ResponseEntity<Boolean> mockResponse = new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		when(restTemplate.getForEntity(eq("http://sample.uri/exists/1"), eq(Boolean.class))).thenReturn(mockResponse);
		ResponseEntity<Void> expectedResponse = new ResponseEntity<Void>(HttpStatus.OK);
		URI callingURI = new URI("http://sample.uri/1");
		when(restTemplate.exchange(eq(callingURI), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Void.class)))
				.thenReturn(expectedResponse);

		ResponseEntity<Void> actualResponse = jsonService.persistLeftJson(1, "encodedjsonbase64");

		assertEquals(expectedResponse, actualResponse);

		verify(storeClient).getProfileUri();
		verify(restTemplate).getForEntity(eq("http://sample.uri/exists/1"), eq(Boolean.class));
		verify(restTemplate).exchange(eq(callingURI), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Void.class));
	}

	@Test
	public void persistLeftJson_post() throws URISyntaxException {
		URI mockURI = new URI("http://sample.uri");
		when(storeClient.getProfileUri()).thenReturn(mockURI);
		ResponseEntity<Boolean> mockResponse = new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.OK);
		System.out.println("mockResponse" + mockResponse.getBody());
		when(restTemplate.getForEntity(eq("http://sample.uri/exists/1"), eq(Boolean.class))).thenReturn(mockResponse);
		ResponseEntity<Void> expectedResponse = new ResponseEntity<Void>(HttpStatus.OK);
		when(restTemplate.exchange(eq(mockURI), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class)))
				.thenReturn(expectedResponse);

		ResponseEntity<Void> actualResponse = jsonService.persistLeftJson(1, "encodedjsonbase64");

		assertEquals(expectedResponse, actualResponse);

		verify(storeClient).getProfileUri();
		verify(restTemplate).getForEntity(eq("http://sample.uri/exists/1"), eq(Boolean.class));
		verify(restTemplate).exchange(eq(mockURI), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class));
	}

}
