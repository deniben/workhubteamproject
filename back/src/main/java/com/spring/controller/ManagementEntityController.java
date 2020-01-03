package com.spring.controller;

import com.spring.dto.PageableResponse;
import com.spring.dto.ProjectDto;
import com.spring.dto.ProjectTypeDto;
import com.spring.dto.SkillDto;
import com.spring.entity.Project;
import com.spring.entity.ProjectType;
import com.spring.entity.Skill;
import com.spring.enums.ProjectStatus;
import com.spring.exception.RestException;
import com.spring.service.PhotosService;
import com.spring.service.ProjectService;
import com.spring.service.ProjectTypeService;
import com.spring.service.SkillsService;
import com.spring.utils.mapper.EmployeeMapper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.spring.service.ProjectService;

@RestController
@RequestMapping("/admin/projects")
public class ManagementEntityController {

	private final ProjectService projectService;

	private final ProjectTypeService projectTypeService;

	private final SkillsService skillsService;

	private final EmployeeMapper employeeMapper;

	private final PhotosService photosService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManagementEntityController.class);


	@Autowired
	public ManagementEntityController(ProjectService projectService,
	                                  ProjectTypeService projectTypeService, SkillsService skillsService, EmployeeMapper employeeMapper, PhotosService photosService) {

		this.projectService = projectService;
		this.projectTypeService = projectTypeService;
		this.skillsService = skillsService;

		this.employeeMapper = employeeMapper;
		this.photosService = photosService;
	}


    @GetMapping()
    public ResponseEntity<PageableResponse> findProjectsByCompany(Long id, Integer page, Optional<String> name) {
        List<Project> projects = projectService.findProjectsByCompany(id, page, name);
        projects.forEach(project -> project.setPhotoUrl(photosService.getPhoto(project.getPhotoUrl())));

		List<ProjectDto> projectDtos = projects
				.stream()
				.map(employeeMapper::toDto)
				.collect(Collectors.toList());
LOGGER.debug("in findByCompany()");

		return ResponseEntity.ok(new PageableResponse(projectService.countProjectsByCompany(id), projectDtos));
	}


	@PostMapping("/status")
	public ResponseEntity updateStatus(Long id, String status) {
		LOGGER.debug("in updateRole()");
		projectService.updateStatus(id, status);
		return ResponseEntity.ok("Project status was successfully updated");
	}


	@PostMapping("/projectType")
	public ResponseEntity updateProjectTypeinProject(Long id, String projectType) {
		LOGGER.debug("in updateProjectTypeinProject()");
		projectService.updateProjectType(id, projectType);
		return ResponseEntity.ok("Project type was successfully updated");
	}


	@PostMapping("/upProjectType")
	public ResponseEntity updateProjectType(ProjectTypeDto projectTypeDto) {
		LOGGER.debug("in updateProjectType()");
		ProjectType projectType = projectTypeService.findById(projectTypeDto.getId());
		projectType.setName(projectTypeDto.getName());

		return ResponseEntity.ok(projectTypeService.updateProjectType(projectType));

	}


	@PostMapping("/upSkill")
	public ResponseEntity updateSkill(SkillDto skillDto) {
		LOGGER.debug("in updateSkill()");
		Skill skill = skillsService.findById((skillDto.getId()));
		skill.setName(skillDto.getName());

        return ResponseEntity.ok(skillsService.updateSkill(skill));
    }

	@GetMapping("/status")
	public ResponseEntity getAllStatus() {
		LOGGER.trace("in getAllStatus()");
		return ResponseEntity.ok(projectService.getAllStatus());
	}


	@GetMapping("/projectType/{id}")
	public ProjectType findTypeById(@PathVariable Long id) {
		LOGGER.debug("in findTypeById()");
		ProjectType projectType = projectTypeService.findById(id);
		return projectType;
	}


	@GetMapping("/skill/{id}")
	public Skill findSkillById(@PathVariable Long id) {
		LOGGER.debug("in findSkillById()");
		Skill skill = skillsService.findById(id);
		return skill;
	}


	@GetMapping("/skillD/{id}")
	public ResponseEntity deleteSkill(@PathVariable Long id) {
		LOGGER.debug("in deleteSkill()");
		return ResponseEntity.ok(skillsService.deleteSkill(id));

	}


	@PutMapping("/typeD/{id}")
	public ResponseEntity deleteType(@PathVariable Long id) {

		LOGGER.debug("in deleteType()");
		return ResponseEntity.ok(projectTypeService.deleteType(id));
}


	@GetMapping("/typeD/{id}")
	public ResponseEntity unBlockedType(@PathVariable Long id) {
		LOGGER.debug("in deleteType()");
		return ResponseEntity.ok(projectTypeService.unBlockedType(id));
	}


	@GetMapping(value = "/id={id}")
	public ResponseEntity<ProjectDto> findProjectById(@PathVariable Long id) {
		LOGGER.debug("in findProjectById({})", id);
		Project currentProject = projectService.findById(id);
		if(currentProject != null && currentProject.getStatus() != null && !currentProject.getStatus().equals(ProjectStatus.NEW)) {
			throw new RestException("Project with such id do not exists", HttpStatus.BAD_REQUEST.value());
		}
		return ResponseEntity.ok(employeeMapper.toDto(projectService.findById(id)));
	}


	@GetMapping("/projectType")
	public ResponseEntity<PageableResponse> getAllType(Integer page, Optional<String> name) {
		LOGGER.debug("in getAllType()");
		PageableResponse result = projectTypeService.getAll(page, name);

		return ResponseEntity.ok(result);
	}

	@GetMapping("/skills")
	public ResponseEntity<PageableResponse> getAllSkills(Integer page, Optional<String> name) {
		LOGGER.debug("in getAllSkills()");
		PageableResponse result = skillsService.getAll(page, name);

		return ResponseEntity.ok(result);
	}
}