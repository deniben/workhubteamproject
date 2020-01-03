package com.spring.service;

import java.util.List;

import com.spring.entity.Company;
import com.spring.entity.Project;
import com.spring.entity.ProjectRequest;

public interface ProjectRequestService {
	void apply(Long projectId);
	void cancel(Long projectId);
	boolean isApplied(Long projectId);
	List<ProjectRequest> getMyRequests();
	ProjectRequest getProjectRequest(Company employee, Project project);
}
