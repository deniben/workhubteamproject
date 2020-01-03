package com.spring.service.implementations;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.service.EmployeeService;
import com.spring.component.UserContext;
import com.spring.controller.CompanyController;
import com.spring.dao.ProjectDao;
import com.spring.entity.Company;
import com.spring.entity.Project;
import com.spring.entity.User;
import com.spring.enums.CompanyType;
import com.spring.enums.ProjectStatus;
import com.spring.exception.RestException;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	private final ProjectDao projectDao;
	private final UserContext userContext;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);


	@Autowired
	public EmployeeServiceImpl(ProjectDao projectDao, UserContext userContext) {

		this.projectDao = projectDao;
		this.userContext = userContext;
	}

	private static final String EMPLOYER_LIMIT_MESSAGE = "Employer company can not use this api";

	@Override
	public List<Project> getCurrentProjects(Long page) {
		LOGGER.debug("in getCurrentProjects(page: {})", page);
		return projectDao.findByCompanyAndStatus(getUserCompany(), page, ProjectStatus.IN_PROGRESS);
	}

	@Override
	public List<Project> getFinishedProjects(Long page) {
		LOGGER.debug("in getFinishedProjects(page: {})", page);

		return projectDao.findByCompanyAndStatus(getUserCompany(), page, ProjectStatus.COMPLETED, ProjectStatus.FAILED);
	}

	@Override
	public List<Project> getCompletedProjects(Long page) {
		LOGGER.debug("in getCompletedProjects(page: {})", page);

		return projectDao.findByCompanyAndStatus(getUserCompany(), page, ProjectStatus.COMPLETED);
	}

	@Override
	public List<Project> getFailedProjects(Long page) {
		LOGGER.debug("in getFailedProjects(page: {})", page);

		return projectDao.findByCompanyAndStatus(getUserCompany(), page, ProjectStatus.FAILED);
	}

	@Override
	public List<Project> getAllNewProjects(Integer page) {
		LOGGER.debug("in getAllNewProjects(page: {})", page);

		return projectDao.findByStatus(ProjectStatus.NEW, page);
	}

	@Override
	public Integer countProjects(ProjectStatus projectStatus) {
		LOGGER.debug("in countProjects(projectStatus: {})", projectStatus);
		Integer dataSize = projectDao.countByStatus(projectStatus);
		return dataSize;
	}

	private Company getUserCompany() {
		LOGGER.trace("in getUserCompany()");
		User user = userContext.getCurrentUser();
		Company company = user.getProfile().getCompany();

		if(company.getType().equals(CompanyType.EMPLOYER)) {
			throw new RestException(EMPLOYER_LIMIT_MESSAGE, HttpStatus.BAD_REQUEST.value());
		}

		return company;
	}


	@Override
	public  Integer forFullEnd(Long projectId){

		return projectDao.forFullEnd(projectId);
	}

}
