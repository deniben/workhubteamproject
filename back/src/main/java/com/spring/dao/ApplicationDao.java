package com.spring.dao;

import java.util.List;
import java.util.Optional;

import com.spring.entity.ProjectRequest;
import com.spring.entity.Company;
import com.spring.entity.Project;

public interface ApplicationDao extends BaseDao<ProjectRequest> {
	Optional<ProjectRequest> findByEmployeeAndProject(Company employee, Project project);

	List<ProjectRequest> findByEmployee(Company employee);

	List<ProjectRequest> findByAcceptedAndEmployee(Boolean accepted, Company employee);

	void deleteByAccepted(Boolean accepted);
}
