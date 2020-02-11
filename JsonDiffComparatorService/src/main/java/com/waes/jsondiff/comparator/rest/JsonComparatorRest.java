package com.waes.jsondiff.comparator.rest;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.waes.jsondiff.comparator.dto.JsonComparison;
import com.waes.jsondiff.comparator.services.JsonComparatorService;

@RestController
public class JsonComparatorRest {

	@Autowired
	private JsonComparatorService comparatorService;

	@GetMapping(value = "/diff/{id}")
	public ResponseEntity<JsonComparison> getJsonComparison(@PathVariable Long id)
			throws Exception {

		return comparatorService.getJsonComparison(id);
	}

}