package com.waes.jsondiff.comparator.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the result, returned to the client, of the comparison of two json
 * objects identified by the same id in the database.
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class JsonComparison {
	private boolean equal;
	private String jsonReport;
}
