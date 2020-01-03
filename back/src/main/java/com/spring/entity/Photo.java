package com.spring.entity;

import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

public class Photo {

    private String name;

    @Pattern(regexp = "^image/(.*)", message = "Only images are supported")

    private String type;


    private long size;

	@Override
	public String toString() {
		return "Photo{" +
				"name='" + name + '\'' +
				", type='" + type + '\'' +
				", size=" + size +
				'}';
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}
