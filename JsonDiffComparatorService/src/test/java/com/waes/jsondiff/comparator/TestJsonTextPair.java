package com.waes.jsondiff.comparator;

/**
 * DTO used in tests for the communication with the store in the setting of the
 * data necessary for the integration tests.
 */
public class TestJsonTextPair {
	private long id;
	private String leftJson;
	private String rightJson;

	public TestJsonTextPair() {
	}

	public TestJsonTextPair(long id, String leftJson, String rightJson) {
		super();
		this.id = id;
		this.leftJson = leftJson;
		this.rightJson = rightJson;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLeftJson() {
		return leftJson;
	}

	public void setLeftJson(String leftJson) {
		this.leftJson = leftJson;
	}

	public String getRightJson() {
		return rightJson;
	}

	public void setRightJson(String rightJson) {
		this.rightJson = rightJson;
	}
}