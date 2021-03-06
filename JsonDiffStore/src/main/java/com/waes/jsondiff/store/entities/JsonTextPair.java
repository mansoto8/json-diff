package com.waes.jsondiff.store.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity that contains the two json parts to be compared and an identifier.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class JsonTextPair {
	@Id
	private long id;
	@Column(columnDefinition="TEXT")
	private String leftJson;
	@Column(columnDefinition="TEXT")
	private String rightJson;
}
