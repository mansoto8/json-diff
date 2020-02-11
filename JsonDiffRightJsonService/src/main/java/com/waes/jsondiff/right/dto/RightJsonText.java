package com.waes.jsondiff.right.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RightJsonText {
	private long id;
	@NotNull
	private String rightJson; 
}
