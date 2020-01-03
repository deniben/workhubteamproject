package com.spring.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private Map<String, String> validation;
    private int status = 400;

    public ValidationException(Map<String, String> validation, int status) {
        this.validation = validation;
        this.status = status;
    }

    public String getValidation() {
        return convertWithIteration(validation);
    }

    public void setValidation(Map<String, String> validation) {
        this.validation = validation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

	public String convertWithIteration(Map<String, String> map) {
		StringBuilder mapAsString = new StringBuilder("{");
		for (String key : map.keySet()) {
			mapAsString.append(key + ": " + map.get(key) + ", ");
		}
		mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
		return mapAsString.toString();
	}
}
