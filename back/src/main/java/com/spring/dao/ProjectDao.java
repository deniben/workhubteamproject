package com.spring.dao;

import com.spring.dto.PageableResponse;
import com.spring.entity.Company;
import com.spring.entity.Project;
import com.spring.entity.ProjectType;
import com.spring.entity.Skill;
import com.spring.enums.CompanyType;
import com.spring.enums.ProjectStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProjectDao extends BaseDao<Project> {

	Integer PAGE_SIZE = 6;
	Integer PROJECT_BY_STATUS_PAGE_SIZE = 6;
	Integer PROJECTS_BY_COMPANY_PAGE_SIZE = 5;
	Integer FOR_PROJECT_BY_EMPLOYER = 10;
	String COMPANY_EMPLOYEE = "companyEmployee";
	String COMPANY_CREATOR = "companyCreator";
	Integer PROJECT_BY_COMPANY_PAGE_SIZE = 6;

	Optional<Project> findRandomWithType(ProjectType projectType);

    List<Project> findBySkills(Set<Skill> skillSet);

	List<Integer> getSkillsId(Long projectId);

	List<Project> findByStatus(ProjectStatus projectStatus, Integer page);

	void acceptCompanyToProject(Long employeeId, Long projectId);

	void rejectCompany(Long employeeId, Long projectId);

	Integer checkingEmployeeRequest(Long projectId, Long employeeId);

	void finishProject(Long projectId);

	void completeProject(Long id);

    List<Project> findByCompanyAndStatus(Company company, Long page, ProjectStatus... projectStatus);

	List<Project> findByStatus(Company company, Long page,ProjectStatus projectStatus);

	List<Project> findByCompany(Company company);

	Integer forFullEnd(Long projectId);

	Integer existProject(Long ProjectId);

	void updateExpiredStatus();

	Integer countByStatus(ProjectStatus projectStatus);

	Integer numberOfInvite(Long projectName);

	List<Project> findByCompany(Long companyId, CompanyType companyType);

    List<Project> findByType(ProjectType id);


	Integer numberOfMyProjects(ProjectStatus projectStatus);

	void changeStatus(Long projectId, ProjectStatus status);

	List<Project> findAllProjectByCompany(Long id, Integer page, Optional<String> name);

	Integer countProjectsByCompany(Long id);

	Integer countFinishedProjectsNotifications(Company company);

	void merge(Project project);
}
