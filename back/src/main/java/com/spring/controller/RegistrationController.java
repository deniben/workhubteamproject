package com.spring.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.constants.SecurityConstants;
import com.spring.dto.RegistrationDto;
import com.spring.entity.User;
import com.spring.exception.RestException;
import com.spring.service.JwtService;
import com.spring.service.implementations.MailService;
import com.spring.service.implementations.RegistrationService;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

	private final RegistrationService registrationService;
	private final JwtService jwtService;
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);


	@Autowired
	public RegistrationController(RegistrationService registrationService, MailService mailService,
	                              JwtService jwtService) {
		this.registrationService = registrationService;
		this.jwtService = jwtService;
	}

	@PostMapping
	public ResponseEntity<String> registerUser(@RequestBody RegistrationDto registrationDto) {
		LOGGER.debug("in registerUser(), registrationDto: {}", registrationDto);
		registrationService.registerUser(registrationDto);
		return ResponseEntity.ok("Please activate account by email");
	}

	@GetMapping("/activate-account")
	public ResponseEntity<String> activateAccount(@RequestParam(required = false) String id, @RequestParam(required = false) String token,
	                                              HttpServletResponse httpServletResponse) {
		LOGGER.debug("in registerUser(), id: {}, token: {}, httpServletResponse: {}", id, token, httpServletResponse);


		Long userId = Long.parseLong(id);

		if(token == null) {
			throw new RestException("Token is required", 400);
		}

		registrationService.activateAccount(userId, token);

		User user = registrationService.getUserById(userId);

		httpServletResponse.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX +
				jwtService.generateToken(user.getUsername(), user.getRoles()
						.stream().map(x -> x.name() + "")
						.collect(Collectors.toSet())));
		return ResponseEntity.ok("Account activated");
	}

}
