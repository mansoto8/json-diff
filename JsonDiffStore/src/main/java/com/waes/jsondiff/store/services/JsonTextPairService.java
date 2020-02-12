package com.waes.jsondiff.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.waes.jsondiff.store.repositories.JsonTextPairRepository;

/**
 * Service that exposes additional operations for the database.
 */
@Service
public class JsonTextPairService {
	
	private JsonTextPairRepository repository;
	
	@Autowired
	public JsonTextPairService(JsonTextPairRepository repository) {
		this.repository = repository;
	}
	
	/**
	 * Returns true if the JsonTextPair with id passed as parameter exists in the database. 
	 * @param id
	 * @return
	 */
	public ResponseEntity<Boolean> existById(Long id) {

		return new ResponseEntity<>(repository.existsById(id), HttpStatus.OK);
	}
}
