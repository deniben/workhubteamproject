package com.spring.dto;

import java.util.Set;

import com.spring.enums.Role;

public class UserDto {
	private String username;
	private Set<Role> roles;

	public UserDto() {
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
