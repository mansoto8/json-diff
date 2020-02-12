package com.waes.jsondiff.left.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the left part of the json pair to be stored in the database.
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LeftJsonText {
	private long id;
	@NotNull
	private String leftJson;
}
