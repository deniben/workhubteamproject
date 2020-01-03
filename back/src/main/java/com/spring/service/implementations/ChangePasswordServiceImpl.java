package com.spring.service.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.component.UserContext;
import com.spring.controller.CompanyController;
import com.spring.dao.UserDao;
import com.spring.dto.ChangePasswordDto;
import com.spring.entity.User;
import com.spring.enums.CacheType;
import com.spring.exception.RestException;
import com.spring.exception.SecurityException;
import com.spring.service.ChangePasswordService;
import com.spring.utils.PasswordUtils;
import com.spring.utils.TokenUtils;

@Service
@Transactional
public class ChangePasswordServiceImpl implements ChangePasswordService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChangePasswordServiceImpl.class);
	private final ValidationService validationService;
	private final UserContext userContext;
	private final PasswordEncoder passwordEncoder;
	private final UserDao userRepository;
	private final MailService mailService;

	@Autowired
	public ChangePasswordServiceImpl(ValidationService validationService, UserContext userContext, PasswordEncoder passwordEncoder, UserDao userRepository, MailService mailService) {
		this.validationService = validationService;
		this.userContext = userContext;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.mailService = mailService;
	}

	public void changePassword(ChangePasswordDto changePasswordDto) {
		LOGGER.debug("in changePassword(changePasswordDto: {})", changePasswordDto);

		if(changePasswordDto == null) {
			throw new RestException("Invalid input data", HttpStatus.BAD_REQUEST.value());
		}

		validationService.changePasswordValidation(changePasswordDto);

		String originPassword = userContext.getCurrentUser().getPassword();

		if(!passwordEncoder.matches(changePasswordDto.getOldPassword(), originPassword)) {
			throw new SecurityException("Incorrect old password");
		}

		userRepository.changePassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
	}

	public void changePassword(ChangePasswordDto changePasswordDto, String token) {
		LOGGER.debug("in changePassword(changePasswordDto: {}, token: {})", changePasswordDto, token);

		User user = userContext.getCurrentUser();

		if(changePasswordDto == null || token == null) {
			throw new RestException("Invalid input data", HttpStatus.BAD_REQUEST.value());
		}

		String originToken = TokenUtils.getToken(user.getUsername(), CacheType.RECOVERY);

		if(!originToken.equals(token)) {
			throw new SecurityException(400, "Invalid token", "Invalid recovery token");
		}

		validatePassword(changePasswordDto);

		userRepository.changePassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
		TokenUtils.removeToken(user.getUsername(), CacheType.RECOVERY);
	}

	@Override
	public void changePasswordWithEmail(ChangePasswordDto changePasswordDto) {
		LOGGER.debug("in changePasswordWithEmail(changePasswordDto: {})", changePasswordDto);

		User user = userContext.getCurrentUser();

		if(changePasswordDto == null) {
			throw new RestException("Invalid input data");
		}

		validatePassword(changePasswordDto);

		PasswordUtils.cache(user.getUsername(), changePasswordDto.getNewPassword());
		String token = TokenUtils.generateToken(user.getUsername(), CacheType.RECOVERY);
		mailService.sendPasswordChangeConfirmationMail(user.getUsername(), user.getProfile().getNickname(), token);
	}

	private void validatePassword(ChangePasswordDto changePasswordDto) {
		LOGGER.debug("in validatePassword(changePasswordDto: {})", changePasswordDto);
		if(changePasswordDto.getNewPassword().length() < 6 ||
				!changePasswordDto.getNewPassword().equals(changePasswordDto.getRepeatedPassword())) {
			throw new RestException("Please provide valid passwords", HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public void confirmPasswordChange(String token) {
		LOGGER.debug("in confirmPasswordChange(token: {})", token);
		User user = userContext.getCurrentUser();

		String password = PasswordUtils.get(user.getUsername());

		if(password == null) {
			throw new RestException("Nothing to confirm");
		}

		String tokenOrigin = TokenUtils.getToken(user.getUsername(), CacheType.RECOVERY);

		if(!tokenOrigin.equals(token)) {
			throw new RestException("Invalid confirmation token");
		}

		TokenUtils.removeToken(user.getUsername(), CacheType.RECOVERY);
		PasswordUtils.remove(user.getUsername());

		userRepository.changePassword(passwordEncoder.encode(password));
	}
}
