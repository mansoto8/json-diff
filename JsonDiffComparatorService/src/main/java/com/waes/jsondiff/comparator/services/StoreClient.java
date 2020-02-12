package com.waes.jsondiff.comparator.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Discovery client of detecting the url of the store server.
 */
@Service
public class StoreClient {

	private final DiscoveryClient discoveryClient;

	@Autowired
	public StoreClient(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}

	/**
	 * Gets the uri of the store server
	 * 
	 * @return uri of store server
	 */
	public URI getProfileUri() {
		ServiceInstance instance = discoveryClient.getInstances("json-diff-store").get(0);
		String url = instance.getUri().toString();

		return UriComponentsBuilder.fromHttpUrl(url + "/jsontext").buildAndExpand().toUri();
	}

}
