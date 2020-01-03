package com.spring.exception;

public class SecurityException extends RestException {

	private String description;

	public SecurityException() {
		super();
	}

	public SecurityException(String message) {
		super(message);
	}

	public SecurityException(Integer status, String message, String description) {
		super(message, status);
		this.description = description;
	}

	public SecurityException(String message, Integer status) {
		super(message, status);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
