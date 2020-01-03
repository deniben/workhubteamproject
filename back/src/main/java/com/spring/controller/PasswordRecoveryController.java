package com.spring.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.constants.SecurityConstants;
import com.spring.entity.User;
import com.spring.enums.Role;
import com.spring.service.JwtService;
import com.spring.service.PasswordRecoveryService;

@RestController
@RequestMapping("/recover-password")
public class PasswordRecoveryController {

	private final PasswordRecoveryService passwordRecoveryService;
	private final JwtService jwtService;
	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordRecoveryController.class);


	@Autowired
	public PasswordRecoveryController(PasswordRecoveryService passwordRecoveryService, JwtService jwtService) {
		this.passwordRecoveryService = passwordRecoveryService;
		this.jwtService = jwtService;
	}

	@PostMapping
	public ResponseEntity<String> recoverPassword(@RequestParam String email) {
		LOGGER.debug("in recoverPassword(), email: {}", email);
		passwordRecoveryService.recoverPassword(email);
		return ResponseEntity.ok("Recovery mail successfully sent. Please check your inbox");
	}

	@PostMapping("/check-token")
	public ResponseEntity<String> responseEntity(@RequestParam Long id, @RequestParam String token,
	                                     HttpServletResponse httpServletResponse) {
		LOGGER.debug("in responseEntity(), id: {}, token: {}, httpServletResponse: {}", id, token, httpServletResponse);
		User user = passwordRecoveryService.recoverPassword(id, token);
		httpServletResponse.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX +
				jwtService.generateToken(user.getUsername(), user.getRoles().stream()
						.map(Role::name)
						.collect(Collectors.toSet())));
		return ResponseEntity.ok("Token is valid. Now you can change your password");
	}

}
