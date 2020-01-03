package com.spring.service;

import java.util.List;

import com.spring.entity.Project;
import com.spring.entity.ProjectType;
import com.spring.entity.User;

public interface RecommendationsService {

	Float SEARCH = 1f;
	Float ATTEMPT = 1.5f;
	Float START = 2.0f;

	Float RESET_BOUND = 10f;


	List<Project> generaterecomendations();

	List<Project> generateRecommendationsBySkills();

	void updateValuation(ProjectType projectType, Float weight);

}
