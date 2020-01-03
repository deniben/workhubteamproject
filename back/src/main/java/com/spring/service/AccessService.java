package com.spring.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AccessService {
	ResponseEntity<String> access(HttpServletRequest request);
}
