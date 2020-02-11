package com.waes.jsondiff.store;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LeftJsonText {
	private long id;
	@NotNull
	private String leftJson; 
}
