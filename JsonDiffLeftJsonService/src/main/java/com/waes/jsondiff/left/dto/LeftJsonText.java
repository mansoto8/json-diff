package com.waes.jsondiff.left.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LeftJsonText {
	private long id;
	private String leftJson; 
}
