package com.spring.dto;

public class BlockIpDto {

	private String address;

	private String reason;

	public BlockIpDto() {

	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
