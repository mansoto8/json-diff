package com.waes.jsondiff.comparator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stores two json objects that are going to be compared by the service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonTextPair {
	private String leftJson;
	private String rightJson;
}
