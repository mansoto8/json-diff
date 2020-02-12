package com.waes.jsondiff.comparator.services;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.jsondiff.comparator.dto.JsonComparison;
import com.waes.jsondiff.comparator.dto.JsonTextPair;

/**
 * Service that groups all the logic necessary for the comparison of json
 * objects represented by an id.
 */
@Service
public class JsonComparatorService {

	private StoreClient storeClient;
	private RestTemplate restTemplate;

	@Autowired
	public JsonComparatorService(StoreClient storeClient, RestTemplate restTemplate) {
		this.storeClient = storeClient;
		this.restTemplate = restTemplate;
	}

	/**
	 * Search for the json objects stored in database represented by the id passed
	 * as parameter.
	 * 
	 * @param id Of the json pair of objects stored in database
	 * @return The result of the comparison of the two json objects
	 * @throws JsonProcessingException Error processing the json objects
	 * @throws JsonMappingException    Error mapping the json strings to java
	 *                                 objects
	 */
	public ResponseEntity<JsonComparison> getJsonComparison(long id)
			throws JsonMappingException, JsonProcessingException {
		URI storeUri = this.storeClient.getProfileUri();

		ResponseEntity<JsonTextPair> jsonPair = restTemplate.getForEntity(storeUri + "/" + id, JsonTextPair.class);

		return new ResponseEntity<>(compareJson(jsonPair.getBody()), HttpStatus.OK);
	}

	/**
	 * Identifies if the json pair passed as parameter is equal, of different size
	 * or of different size or if of same size with statistics about differences in
	 * each offset.
	 * 
	 * @param jsonTextPair Pair of json objects to be compared
	 * @return The result of the comparison of the json object pair
	 * @throws JsonMappingException    Error mapping the json strings to java
	 *                                 objects
	 * @throws JsonProcessingException Error processing the json objects
	 */
	protected JsonComparison compareJson(JsonTextPair jsonTextPair)
			throws JsonMappingException, JsonProcessingException {
		String leftJson = jsonTextPair.getLeftJson();
		String rightJson = jsonTextPair.getRightJson();

		boolean equal = false;
		boolean equalSize = false;
		if (rightJson == null) {
			if (leftJson == null) {
				equal = true;
			}
		} else {
			if (leftJson != null) {
				equal = leftJson.equals(rightJson);
				equalSize = leftJson.length() == rightJson.length();
			}
		}

		return buildJsonComparison(equal, equalSize, jsonTextPair);
	}

	/**
	 * Build the jsonComparison object based on the information of equality,
	 * equalSize and the pair of json objects.
	 * 
	 * @param equal        True if the two json objects are exactly equal
	 * @param equalSize    True if the two json objects are of the same size
	 * @param jsonTextPair Pair of json objects being compared
	 * @return The result of the comparison of the json object pair
	 * @throws JsonMappingException    Error mapping the json strings to java
	 *                                 objects
	 * @throws JsonProcessingException Error processing the json objects
	 */
	private JsonComparison buildJsonComparison(boolean equal, boolean equalSize, JsonTextPair jsonTextPair)
			throws JsonMappingException, JsonProcessingException {
		JsonComparison comparison;
		if (equal) {
			comparison = new JsonComparison(equal, "Both json parts are equal.");
		} else {
			if (equalSize) {
				comparison = new JsonComparison(equal, getJsonDiffReport(jsonTextPair));
			} else {
				comparison = new JsonComparison(equal, "Json parts are of different size.");
			}
		}

		return comparison;
	}

	/**
	 * Gets the summary of differences between the two json objects being compared.
	 * This comparison is made when the pair has exactly the same length.
	 * 
	 * @param jsonTextPair Pair of json objects being compared
	 * @return The summary of differences between the two json objects being
	 *         compared
	 * @throws JsonMappingException    Error mapping the json strings to java
	 *                                 objects
	 * @throws JsonProcessingException Error processing the json objects
	 */
	private String getJsonDiffReport(JsonTextPair jsonTextPair) throws JsonMappingException, JsonProcessingException {
		Map<String, String> mapReport = new TreeMap<>();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode leftNode = objectMapper.readTree(jsonTextPair.getLeftJson());
		JsonNode rightNode = objectMapper.readTree(jsonTextPair.getRightJson());

		// 1. We iterate over all the properties of the left node comparing with the
		// ones in the right node
		Iterator<String> leftItr = leftNode.fieldNames();
		while (leftItr.hasNext()) {
			String field = leftItr.next();
			String leftValue = leftNode.get(field).asText();
			JsonNode rightNodeTemp = rightNode.get(field);
			String summary = "";
			if (rightNodeTemp == null) {
				summary = "Value only exists in the left part.";
			} else if (leftValue.equals(rightNodeTemp.asText())) {
				summary = "Value is equal in both parts.";
			} else {
				int diffSize = Math.abs(leftValue.length() - rightNodeTemp.asText().length());
				summary = "Value differs by " + diffSize + " characters.";
			}
			mapReport.put(field, summary);
		}

		// 2. We iterate over all the properties of the right node to check which ones
		Iterator<String> rightItr = rightNode.fieldNames();
		while (rightItr.hasNext()) {
			String field = rightItr.next();
			JsonNode leftNodeTemp = leftNode.get(field);
			if (leftNodeTemp == null) {
				mapReport.put(field, "Value only exists in the right part.");
			}
		}

		return generateComparisonReportFromMap(mapReport);
	}

	/**
	 * Generate a String summary of differences between the two json objects being
	 * compared based on the Map passed as parameter.
	 * 
	 * @param reportMap Map where the key correspond to each json node (of first
	 *                  level) and the value as a String with the summary of the
	 *                  diff state of that property between the two pair of json
	 *                  objects being compared.
	 * @return The summary report as a string representation
	 */
	private String generateComparisonReportFromMap(Map<String, String> reportMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("Next is the list of properties with the comparison summary: \n");
		for (Entry<String, String> entry : reportMap.entrySet()) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		return sb.toString();
	}

}
