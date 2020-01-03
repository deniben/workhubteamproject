package com.spring.service;

import com.spring.entity.User;

public interface PasswordRecoveryService {

	void recoverPassword(String email);

	User recoverPassword(Long userId, String token);

}
