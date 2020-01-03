package com.spring.controller;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.ProjectDto;
import com.spring.entity.Project;
import com.spring.exception.RestException;
import com.spring.service.RecommendationsService;
import com.spring.utils.mapper.ProjectMapper;

@RestController
@RequestMapping("/recommendations")
public class UserRecommendationsController {

	private final RecommendationsService recommendationsService;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRecommendationsController.class);
	private final ProjectMapper projectMapper;

	@Autowired
	public UserRecommendationsController(RecommendationsService recommendationsService, ProjectMapper projectMapper) {
		this.recommendationsService = recommendationsService;
		this.projectMapper = projectMapper;
	}

	@GetMapping("/by-history")
	public List<ProjectDto> proposeProjectsByHistory() {
		LOGGER.trace("in proposeProjectsByHistoryt()");

		return recommendationsService.generaterecomendations().stream()
				.map(projectMapper::toDto)
				.collect(Collectors.toList());
	}

	@GetMapping("/by-skills")
	public ProjectDto proposeProjectsBySkills() {
		LOGGER.trace("in proposeProjectsBySkills()");
		List<Project> projects = recommendationsService.generateRecommendationsBySkills();
		if(projects.size() > 0) {
			return projectMapper.toDto(
					projects.get(ThreadLocalRandom.current()
							.nextInt(0, projects.size())));
		}
		throw new RestException("Nothing to recommend");
	}

}
