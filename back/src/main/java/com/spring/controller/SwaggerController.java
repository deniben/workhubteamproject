package com.spring.controller;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.constants.SecurityConstants;
import com.spring.enums.Role;
import com.spring.service.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@Api(value = "/swagger")
@RestController
@RequestMapping("/swagger")
public class SwaggerController {

	private final AuthenticationProvider authenticationManager;
	private final JwtService jwtService;
	private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerController.class);


	@Autowired
	public SwaggerController(AuthenticationProvider authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@ApiOperation(notes = "Authorization for Swagger", code = 200, value = "Authorization Endpoint", produces = "JWT token", consumes = "username, password")
	@ApiResponse(code = 200, message = "User successfully authorized. Token returned", response = String.class)
	@PostMapping("/authorization")
	public String authorizationSwagger(String username, String password) {
		LOGGER.debug("in authorizationSwagger(), username: {}, password #", username);
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		if(authentication == null || !authentication.isAuthenticated()) {
			throw new SecurityException("Invalid credentials");
		}
		return SecurityConstants.TOKEN_PREFIX + jwtService.generateToken(username, Collections.singleton(Role.USER.name()));
	}

}
