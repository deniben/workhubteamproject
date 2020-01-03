package com.spring.service;

import com.spring.dto.ChangePasswordDto;

public interface ChangePasswordService {

	void changePassword(ChangePasswordDto changePasswordDto);

	void changePassword(ChangePasswordDto changePasswordDto, String token);

	void changePasswordWithEmail(ChangePasswordDto changePasswordDto);

	void confirmPasswordChange(String token);
}
