package com.spring.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class ChangePasswordDto {

	@NotBlank
	@Length(min = 6)
	private String oldPassword;

	@NotBlank
	@Length(min = 6)
	private String newPassword;

	@NotBlank
	@Length(min = 6)
	private String repeatedPassword;

	public ChangePasswordDto() {

	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRepeatedPassword() {
		return repeatedPassword;
	}

	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}
}
