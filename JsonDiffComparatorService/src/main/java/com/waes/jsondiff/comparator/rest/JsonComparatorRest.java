package com.waes.jsondiff.comparator.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.waes.jsondiff.comparator.dto.JsonComparison;
import com.waes.jsondiff.comparator.services.JsonComparatorService;

/**
 * Controller that exposes the rest endpoints related to the comparison of two
 * json objects.
 */
@RestController
public class JsonComparatorRest {

	@Autowired
	private JsonComparatorService comparatorService;

	/**
	 * Search for the json objects stored in database represented by the id passed
	 * as parameter.
	 * 
	 * @param id Of the json pair of objects stored in database
	 * @return The result of the comparison of the two json objects
	 * @throws JsonProcessingException Error processing the json objects
	 * @throws JsonMappingException    Error mapping the json strings to java
	 *                                 objects
	 */
	@GetMapping(value = "/diff/{id}")
	public ResponseEntity<JsonComparison> getJsonComparison(@PathVariable Long id)
			throws JsonMappingException, JsonProcessingException {

		return comparatorService.getJsonComparison(id);
	}

}