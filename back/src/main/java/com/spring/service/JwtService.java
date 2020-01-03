package com.spring.service;

import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface JwtService {

	String generateToken(String username, Set<String> roles);

	UsernamePasswordAuthenticationToken parseToken(String token);

}
