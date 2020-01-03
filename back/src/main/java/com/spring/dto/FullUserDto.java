package com.spring.dto;

import com.spring.enums.Role;

public class FullUserDto extends ProfileDto {

	public FullUserDto() {
		super();
	}

	private String username;

	private Long userId;

	private Role role;

	private Boolean blocked;

	public Boolean getBlocked() {
		return blocked;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
