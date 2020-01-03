package com.spring.service;

import java.util.List;

import com.spring.entity.Project;
import com.spring.enums.ProjectStatus;

public interface EmployeeService {

	List<Project> getCurrentProjects(Long page);

	List<Project> getFinishedProjects(Long page);

	List<Project> getCompletedProjects(Long page);

	List<Project> getFailedProjects(Long page);

	List<Project> getAllNewProjects(Integer page);

	Integer countProjects(ProjectStatus projectStatus);

	Integer forFullEnd(Long projectId);


}
