package com.spring.controller;

import com.spring.entity.ProjectType;
import com.spring.service.ProjectTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectTypeController {


	private ProjectTypeService projectTypeService;

	@Autowired
	public ProjectTypeController(ProjectTypeService projectTypeService) {
		this.projectTypeService = projectTypeService;
	}

	@RequestMapping(value = "/projectType", method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectType> findAllProjectType() {
		return projectTypeService.findAllNotBlocked();
	}

	@RequestMapping(value = {"/projectType"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<ProjectType> createProjectType(@RequestBody(required = false) ProjectType projectType) {

		return ResponseEntity.ok(projectTypeService.save(projectType));

	}


	@GetMapping(value = "/findByProject/{id}")
	@ResponseBody
	public ProjectType findProjectTypeById(@PathVariable("id") Long id) {
		return projectTypeService.findById(id);
	}

	@GetMapping("/project/types")
	public ResponseEntity<List<ProjectType>> getAllProjectTypes() {
		return ResponseEntity.status(HttpStatus.OK).body(projectTypeService.findAll());
	}


}
