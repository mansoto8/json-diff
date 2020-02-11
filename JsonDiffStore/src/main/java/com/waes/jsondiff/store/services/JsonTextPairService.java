package com.waes.jsondiff.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.waes.jsondiff.store.repositories.JsonTextPairRepository;

@Service
public class JsonTextPairService {
	
	JsonTextPairRepository repository;
	
	@Autowired
	public JsonTextPairService(JsonTextPairRepository repository) {
		this.repository = repository;
	}
	
	public ResponseEntity<Boolean> existById(Long id) {

		return new ResponseEntity<>(repository.existsById(id), HttpStatus.OK);
	}
}
