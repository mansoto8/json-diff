package com.waes.jsondiff.store.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.waes.jsondiff.store.entities.JsonTextPair;

@RepositoryRestResource(collectionResourceRel = "jsontext", path = "jsontext")
public interface JsonTextPairRepository extends CrudRepository<JsonTextPair, Long> {
}
