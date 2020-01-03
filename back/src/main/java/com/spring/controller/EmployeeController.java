package com.spring.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dao.ProjectDao;
import com.spring.dto.PageableResponse;
import com.spring.entity.Project;
import com.spring.enums.ProjectStatus;
import com.spring.exception.RestException;
import com.spring.service.EmployeeService;
import com.spring.service.PhotosService;
import com.spring.utils.Page;
import com.spring.utils.mapper.EmployeeMapper;
import com.spring.utils.mapper.FinishedProjectMapper;
import com.spring.utils.mapper.ProjectMapper;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	private final EmployeeService employeeService;
	private final FinishedProjectMapper finishedProjectMapper;
	private final ProjectMapper projectMapper;
	private final PhotosService photosService;
	private final EmployeeMapper employeeMapper;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);


	@Autowired
	public EmployeeController(EmployeeService employeeService, FinishedProjectMapper finishedProjectMapper,
	                          ProjectMapper projectMapper, PhotosService photosService, EmployeeMapper employeeMapper) {
		this.employeeService = employeeService;
		this.finishedProjectMapper = finishedProjectMapper;
		this.projectMapper = projectMapper;
		this.photosService = photosService;
		this.employeeMapper = employeeMapper;
	}

	private static final String COMPLETED_TYPE_PARAM = "completed";

	private static final String FAILED_TYPE_PARAM = "failed";

	@GetMapping("/projects/current/{page}")
	public ResponseEntity<PageableResponse> getCurrentProjects(@PathVariable(required = false) Long page) {
		LOGGER.debug("in getCurrentProjects(), page: {}", page);
		List<Project> projects = employeeService.getCurrentProjects(page != null ? page : 0);
		projects.forEach(project -> project.setPhotoUrl(photosService.getPhoto(project.getPhotoUrl())));


		return ResponseEntity.ok(
				new PageableResponse(
						Page.countPages(employeeService.countProjects(ProjectStatus.IN_PROGRESS), ProjectDao.PROJECT_BY_STATUS_PAGE_SIZE),
						projects.stream()
							.map(employeeMapper::toDto)
							.collect(Collectors.toList())
				)
		);

	}

	@GetMapping("/projects/finished/{page}")
	public ResponseEntity<PageableResponse> getFinishedProjects(@PathVariable(required = false) Long page) {
		LOGGER.debug("in getFinishedProjects(), page: {}", page);

		List<Project> projects = employeeService.getFinishedProjects(page != null ? page : 0);

		projects.forEach(project -> project.setPhotoUrl(photosService.getPhoto(project.getPhotoUrl())));

		return ResponseEntity.ok(
				new PageableResponse(Page.countPages(employeeService.countProjects(ProjectStatus.COMPLETED)
						+ employeeService.countProjects(ProjectStatus.FAILED), ProjectDao.PROJECT_BY_STATUS_PAGE_SIZE), projects
								.stream()
								.map(finishedProjectMapper::toDto)
						.peek(x->x.setFlagForRate(employeeService.forFullEnd(x.getId())))
						.peek(x-> System.out.println(x.getFlagForRate() +"  flagForRate"))
								.collect(Collectors.toList())
				)
		);
	}

	@GetMapping("/projects/finished/{type}/{page}")
	public ResponseEntity<PageableResponse> getCompletedOrFailedProjects(@PathVariable String type, @PathVariable(required = false) Long page) {
		LOGGER.debug("in getCompletedOrFailedProjects(), page: {}", page);


		List<Project> projects;
		Integer projectsCount;
		page = page != null ? page : 0;

		if (type.equals(COMPLETED_TYPE_PARAM)) {
			projects = employeeService.getCompletedProjects(page);
			projectsCount = employeeService.countProjects(ProjectStatus.COMPLETED);
		} else if (type.equals(FAILED_TYPE_PARAM)) {
			projects = employeeService.getFailedProjects(page);
			projectsCount = employeeService.countProjects(ProjectStatus.FAILED);
		} else {
			throw new RestException("Invalid project type");
		}

		projects.forEach(project -> project.setPhotoUrl(photosService.getPhoto(project.getPhotoUrl())));

		return ResponseEntity.ok(
				new PageableResponse(Page.countPages(projectsCount, ProjectDao.PROJECT_BY_STATUS_PAGE_SIZE),
						projects.stream()
							.map(finishedProjectMapper::toDto)
							.collect(Collectors.toList())
				)
		);
	}

	@GetMapping("/projects")
	public ResponseEntity<PageableResponse> getAllProjects(Optional<Integer> page) {
		LOGGER.debug("in getAllProjects(), page: {}", page);

		Integer pageNumber = 0;

		if(page.isPresent()) {
			pageNumber = page.get();
		}

		return ResponseEntity.ok(
				new PageableResponse(
						Page.countPages(employeeService.countProjects(ProjectStatus.NEW), ProjectDao.PAGE_SIZE),
						employeeService.getAllNewProjects(pageNumber)
								.stream()
								.map(projectMapper::toDto)
								.collect(Collectors.toList())));

	}

}
