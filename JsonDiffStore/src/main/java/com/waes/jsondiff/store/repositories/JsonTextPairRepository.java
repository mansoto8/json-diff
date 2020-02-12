package com.waes.jsondiff.store.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.waes.jsondiff.store.entities.JsonTextPair;

/**
 * We extend the CrudRepository for the JsonTextPair object. It automatically exposes
 * rest endpoints for the main operations. 
 */
@RepositoryRestResource(collectionResourceRel = "jsontext", path = "jsontext")
public interface JsonTextPairRepository extends CrudRepository<JsonTextPair, Long> {
}
