package com.spring.exception;

public class RestException extends RuntimeException {

	private Integer status = 400;

	public RestException() {
	}

	public RestException(String message) {
		super(message);
	}

	public RestException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestException(Throwable cause) {
		super(cause);
	}

	public RestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RestException(String message, Integer status) {
		super(message);
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
