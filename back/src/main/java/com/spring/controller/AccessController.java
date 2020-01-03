package com.spring.controller;

import com.spring.service.AccessService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/access")
public class AccessController {

	private AccessService accessService;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	public AccessController(AccessService accessService) {
		this.accessService = accessService;
	}

    @GetMapping
    public ResponseEntity<String> accessFunc(HttpServletRequest request) {
	    LOGGER.debug("in accessFunc(), request: {}", request);
	    return accessService.access(request);
    }

}

