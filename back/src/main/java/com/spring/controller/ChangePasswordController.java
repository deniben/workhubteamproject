package com.spring.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.dto.ChangePasswordDto;
import com.spring.service.ChangePasswordService;
import com.spring.service.implementations.MailService;

@RestController
@RequestMapping("/change-password")
public class ChangePasswordController {

	private final ChangePasswordService changePassword;
	private final MailService mailService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ChangePasswordController.class);

	@Autowired
	public ChangePasswordController(ChangePasswordService changePassword, MailService mailService) {
		this.changePassword = changePassword;
		this.mailService = mailService;
	}

	@PostMapping
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto,
	                                     @RequestHeader(name = "User-Agent", required = false) String browserData,
	                                     @RequestHeader(name = "X-Forwarded-For", required = false) String remoteAddress,
	                                     HttpServletRequest httpServletRequest, Locale locale) {
		LOGGER.debug("in changePassword(), changePasswordDto: {}, browserData: {}, remoteAddress: {}, httpServletRequest: {}, locale: {}",
				changePasswordDto, browserData, remoteAddress, httpServletRequest,  locale);
		changePassword.changePassword(changePasswordDto);
		mailService.sendPasswordChangedMail(remoteAddress == null ? httpServletRequest.getRemoteAddr() : remoteAddress, locale.getCountry(), browserData);
		return ResponseEntity.ok("Successfully changed password");
	}

	@PostMapping("/email-verification")
	public ResponseEntity<String> changePasswordByEmail(@RequestBody ChangePasswordDto changePasswordDto) {
		LOGGER.debug("in changePasswordByEmail(), changePasswordDto: {}", changePasswordDto);
		changePassword.changePasswordWithEmail(changePasswordDto);
		return ResponseEntity.ok("Please use email to confirm password change");
	}

	@PostMapping("/email-verification/{token}")
	public ResponseEntity<String> confirmPasswordChange(@PathVariable String token,
	                                                    @RequestHeader(name = "User-Agent", required = false) String browserData,
	                                                    @RequestHeader(name = "X-Forwarded-For", required = false) String remoteAddress,
	                                                    HttpServletRequest httpServletRequest, Locale locale) {
		LOGGER.debug("in confirmPasswordChange(), token: {}, browserData: {}, remoteAddress: {}, httpServletRequest: {}, locale: {}",
				token, browserData, remoteAddress, httpServletRequest, locale);
		changePassword.confirmPasswordChange(token);
		mailService.sendPasswordChangedMail(remoteAddress != null ? remoteAddress : httpServletRequest.getRemoteAddr(), locale.getCountry(), browserData);
		return ResponseEntity.ok("Password was successfully changed");
	}

	@PostMapping("/recovery/{token}")
	public ResponseEntity<String> recoveryPasswordChange(@RequestBody ChangePasswordDto changePasswordDto, @PathVariable String token) {
		LOGGER.debug("in recoveryPasswordChange(), changePasswordDto: {}, token: {}", changePasswordDto, token);
		changePassword.changePassword(changePasswordDto, token);
		return ResponseEntity.ok("New password successfully saved");
	}

}
