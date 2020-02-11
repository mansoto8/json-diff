package com.waes.jsondiff.left.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class StoreClient {
	
	private final DiscoveryClient discoveryClient;

    @Autowired
    public StoreClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

	public URI getProfileUri() {
		ServiceInstance instance = discoveryClient.getInstances("json-diff-store").get(0);
		String url = instance.getUri().toString();

		System.out.println("Url retrieved: " + url);
		
		return UriComponentsBuilder.fromHttpUrl(url + "/jsontext/").buildAndExpand().toUri();
	}
    
}
