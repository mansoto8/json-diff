package com.waes.jsondiff.store;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO used for being able to perform a patch operation without affecting the
 * rightJson part of the json pair stored in database.
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LeftJsonText {
	private long id;
	@NotNull
	private String leftJson;
}
