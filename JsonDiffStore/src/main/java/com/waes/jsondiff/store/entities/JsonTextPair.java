package com.waes.jsondiff.store.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class JsonTextPair {

	@Id
	private long id;
	private String leftJson;
	private String rightJson;
}