package com.waes.jsondiff.comparator.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.jsondiff.comparator.dto.JsonComparison;
import com.waes.jsondiff.comparator.dto.JsonTextPair;

@Service
public class JsonComparatorService {

	private StoreClient storeClient;
	private RestTemplate restTemplate;

	@Autowired
	public JsonComparatorService(StoreClient storeClient, RestTemplate restTemplate) {
		this.storeClient = storeClient;
		this.restTemplate = restTemplate;
	}

	public ResponseEntity<JsonComparison> getJsonComparison(long id) throws Exception {
		URI storeUri = this.storeClient.getProfileUri();

		ResponseEntity<JsonTextPair> jsonPair = restTemplate.getForEntity(storeUri + "/" + id, JsonTextPair.class);

		return new ResponseEntity<>(compareJson(jsonPair.getBody()), HttpStatus.OK);
	}

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

	private String generateComparisonReportFromMap(Map<String, String> reportMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("Next is the list of properties with the comparison summary: \n");
		for (Entry<String, String> entry : reportMap.entrySet()) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		return sb.toString();
	}

}
