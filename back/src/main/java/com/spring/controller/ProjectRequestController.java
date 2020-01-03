package com.spring.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.ProjectRequestDto;
import com.spring.service.ProjectRequestService;
import com.spring.utils.mapper.ProjectRequestMapper;

@RestController
@RequestMapping("/projects")
public class ProjectRequestController {

	private final ProjectRequestService projectRequestService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectRequestController.class);
	private final ProjectRequestMapper projectRequestMapper;

	@Autowired
	public ProjectRequestController(ProjectRequestService projectRequestService, ProjectRequestMapper projectRequestMapper) {
		this.projectRequestMapper = projectRequestMapper;
		this.projectRequestService = projectRequestService;
	}

	@PostMapping("/{id}/requests")
	public ResponseEntity<String> applyForProject(@PathVariable Long id) {
		LOGGER.debug("in applyForProject(), id: {}", id);
		projectRequestService.apply(id);
		return ResponseEntity.ok("Applied");
	}

	@DeleteMapping("/{id}/requests")
	public ResponseEntity<String> cancelApplicationForProject(@PathVariable Long id) {
		LOGGER.debug("in cancelApplicationForProject(), id: {}", id);
		projectRequestService.cancel(id);
		return ResponseEntity.ok("Canceled");
	}

	@GetMapping("/{id}/requests")
	public ResponseEntity<Boolean> checkIfAlreadyApplied(@PathVariable Long id) {
		LOGGER.debug("in checkIfAlreadyApplied(), id: {}", id);
		return ResponseEntity.ok(projectRequestService.isApplied(id));
	}

	@GetMapping("/current/requests/all")
	public ResponseEntity<List<ProjectRequestDto>> getMyRequests() {
		LOGGER.debug("in getMyRequests()");
		return ResponseEntity.ok(projectRequestService.getMyRequests().stream()
					.map(projectRequestMapper::toDto)
					.collect(Collectors.toList()));
	}

}
