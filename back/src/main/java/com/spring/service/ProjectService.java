package com.spring.service;

import com.spring.entity.Project;
import com.spring.entity.ProjectType;
import com.spring.entity.User;
import com.spring.enums.CompanyType;
import com.spring.enums.ProjectStatus;

import java.util.List;
import java.util.Optional;


public interface ProjectService {

    List<Project> findAll();

    Project save(Project project);

    Project delete(long id);

    Project findById(long id);

    Project saveProject(Project project, User user);

    List<Project> findByStatus(ProjectStatus status, Integer page);

    int numberOfInvite(Long projectId);

	Integer numberOfProjectsPages(ProjectStatus projectStatus);

    Project findProjectById(Long id);

    void acceptCompanyToProject(Long employeeId, Long projectId);

    void rejectCompany(Long employeeId, Long projectId);

    void finishProject(Long projectId);

	void completeProject(Long id);

    List<Project> findByCompany(Long companyId, CompanyType companyType);

    List<Project> findByCompany(long id);

    void updateStatus(Long id, String status);

    void updateProjectType(Long id, String projectType);

    List<String> getAllStatus();

    List<Integer> getSkillsId(Long projectId);

	Project updateProject(Project project);

    List<Project> findProjectsByCompany(Long id, Integer page, Optional<String> name);


    Integer countProjectsByCompany(Long id);

    List<ProjectType> getAllProjectType();

}
