package com.waes.jsondiff.store.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waes.jsondiff.store.services.JsonTextPairService;

/**
 * Controller that exposes additional endpoints to the ones exposed by the
 * CrudRepository.
 */
@RestController
@RequestMapping(path = "jsontext")
public class JsonTextPairRest {

	@Autowired
	private JsonTextPairService jsonTextService;

	/**
	 * Identifies if the JsonTextPair with the id passed as parameter exists in the
	 * database.
	 * 
	 * @param id Identifier of a JsonTextPair object
	 * @return True if the JsonTextPair object is stored in the database
	 */
	@GetMapping(value = "/exists/{id}")
	public ResponseEntity<Boolean> existsById(@PathVariable Long id) {

		return jsonTextService.existById(id);
	}

}
