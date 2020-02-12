package com.waes.jsondiff.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Application that exposes a repository functionality for an H2 database. In a
 * production environment a real database would be desirable, specially a no-sql
 * database like mongo db. The implementation of this store project was created
 * mainly for ease the deployment of the whole application without the need for
 * configuring a database. The H2 is an in memory database, so the data will be
 * lost after each restart.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class JsonDiffStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsonDiffStoreApplication.class, args);
	}

}
