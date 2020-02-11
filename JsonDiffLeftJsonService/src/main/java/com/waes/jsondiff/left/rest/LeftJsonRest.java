package com.waes.jsondiff.left.rest;

import java.net.URI;

import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.waes.jsondiff.left.services.LeftJsonService;

@RestController
public class LeftJsonRest {
	
	@Autowired
    private LeftJsonService jsonService;
	
	@PostMapping(value = "/diff/{id}/left")
    public ResponseEntity<Object> postLeftText(@PathVariable Long id, @Valid @RequestBody String encodedJson) {
        jsonService.persistLeftJson(id, new String(Base64.decodeBase64(encodedJson.getBytes())));

		URI location = ServletUriComponentsBuilder.fromPath("http://localhost:8083/jsontext/"+id).build().toUri();
	
        return ResponseEntity.created(location).build();
    }
	
}
