package com.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	private final NotificationService notificationService;
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);


	@Autowired
	public NotificationController(NotificationService notificationService) {

		this.notificationService = notificationService;
	}

	@GetMapping("/new-projects")
	public ResponseEntity<Integer> countNewProjects() {
		LOGGER.trace("in countNewProjects()");
		return ResponseEntity.ok(notificationService.countNewProjects());
	}

	@GetMapping("/finished-projects")
	public ResponseEntity<Integer> countFinishedProjects() {
		return ResponseEntity.ok(notificationService.countFinishedProjects());
	}

}
