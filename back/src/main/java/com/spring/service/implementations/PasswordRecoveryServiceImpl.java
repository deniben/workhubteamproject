package com.spring.service.implementations;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.UserDao;
import com.spring.entity.User;
import com.spring.enums.CacheType;
import com.spring.exception.RestException;
import com.spring.exception.SecurityException;
import com.spring.service.PasswordRecoveryService;
import com.spring.utils.TokenUtils;

@Service
@Transactional
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

	private final UserDao userRepository;
	private final MailService mailService;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	public PasswordRecoveryServiceImpl(UserDao userRepository, MailService mailService) {
		this.userRepository = userRepository;
		this.mailService = mailService;
	}

	@Override
	public void recoverPassword(String email) {

		if(email == null || email.isEmpty() || !email.matches(".+@.+\\..+")) {
			throw new RestException("Please provide valid email address", HttpStatus.BAD_REQUEST.value());
		}

		Optional<User> userOptional = userRepository.findByUsername(email);

		if(!userOptional.isPresent()) {
			throw new RestException("User with such email do not exists", HttpStatus.BAD_REQUEST.value());
		}

		LOGGER.info("Password recovery attempt. Sending mail to " + email);

		String token = TokenUtils.generateToken(email, CacheType.RECOVERY);
		mailService.sendPasswordRecoveryMail(userOptional.get(), token);
	}

	@Override
	public User recoverPassword(Long userId, String token) {

		User user = userRepository.findById(userId).get();

		String originalToken = TokenUtils.getToken(user.getUsername(), CacheType.RECOVERY);

		if(originalToken == null || !originalToken.equals(token)) {
			throw new SecurityException(HttpStatus.BAD_REQUEST.value(), "Invalid token", "Token is wrong");
		}

		LOGGER.info("Password recovered with token for user : ID " + user.getId() + " : Username " + user.getUsername());

		return user;

	}
}
