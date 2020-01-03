package com.spring.service.implementations;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.service.ProjectRequestService;
import com.spring.service.RecommendationsService;
import com.spring.component.UserContext;
import com.spring.dao.ApplicationDao;
import com.spring.dao.ProjectDao;
import com.spring.entity.ProjectRequest;
import com.spring.entity.Company;
import com.spring.entity.Profile;
import com.spring.entity.Project;
import com.spring.entity.User;
import com.spring.enums.CompanyType;
import com.spring.enums.ProjectStatus;
import com.spring.exception.RestException;
import com.spring.service.SkillsService;
import com.spring.utils.ProjectRequestUtils;

@Service
@Transactional
public class ProjectRequestServiceImpl implements ProjectRequestService {

	private final ApplicationDao applicationDao;

	private final ProjectDao projectDao;

	private final UserContext userContext;

	private final SkillsService skillsService;

	private final RecommendationsService recommendationsService;

	private static final Integer THROUGHOUT_PERCENTAGE = 20;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	public ProjectRequestServiceImpl(ApplicationDao applicationDao, ProjectDao projectDao,
	                                 UserContext userContext, SkillsService skillsService,
	                                 RecommendationsService recommendationsService) {
		this.applicationDao = applicationDao;
		this.projectDao = projectDao;
		this.userContext = userContext;
		this.skillsService = skillsService;

		this.recommendationsService = recommendationsService;
	}

	@Override
	public void apply(Long projectId) {
		User user = userContext.getCurrentUser();
		Profile profile = user.getProfile();
		Company company = profile.getCompany();

		if(company.getType().equals(CompanyType.EMPLOYER)) {
			throw new RestException("Employer company can not implement projects", HttpStatus.BAD_REQUEST.value());
		}

		Optional<Project> destinitionProject = projectDao.findById(projectId);

		if(!destinitionProject.isPresent() || !destinitionProject.get().getStatus().equals(ProjectStatus.NEW)) {
			throw new RestException("Such project do not exists", HttpStatus.BAD_REQUEST.value());
		}


		if(applicationDao.findByEmployeeAndProject(company, destinitionProject.get()).isPresent()) {
			throw new RestException("Already applied", HttpStatus.BAD_REQUEST.value());
		}

		if(skillsService.calculateMatchPercentage(destinitionProject.get(), company.getProposedSkills()) < THROUGHOUT_PERCENTAGE) {
			throw new RestException("Not compatible skills. You need to match at least " + THROUGHOUT_PERCENTAGE + "% of required skills", HttpStatus.BAD_REQUEST.value());
		}

		if(!ProjectRequestUtils.attempt(user.getId(), company.getId())) {
			throw new RestException("Out of attempts for today", HttpStatus.FORBIDDEN.value());
		}

		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setProject(destinitionProject.get());
		projectRequest.setEmployee(company);
		projectRequest.setEmployer(destinitionProject.get().getCompanyCreator());

		applicationDao.save(projectRequest);
		recommendationsService.updateValuation(projectRequest.getProject().getProjectType(), RecommendationsService.ATTEMPT);

		LOGGER.info("User with id: {} applied to project with id: {} ", profile.getUser().getId(), projectId);
	}

	@Override
	public void cancel(Long projectId) {
		User user = userContext.getCurrentUser();
		Profile profile = user.getProfile();
		Company company = profile.getCompany();

		Optional<Project> project = projectDao.findById(projectId);

		if(project.isPresent()) {
			Optional<ProjectRequest> projectRequest = applicationDao.findByEmployeeAndProject(company, project.get());
			if(projectRequest.isPresent()) {
				ProjectRequestUtils.removeAttempt(user.getId(), company.getId());
				applicationDao.delete(projectRequest.get().getId());
			} else {
				throw new RestException("Nonexistent project", HttpStatus.BAD_REQUEST.value());
			}
		} else {

			LOGGER.info("User with id: {} canceled his application to project with id: {}", user.getId(), projectId);
			throw new RestException("To cancel project request, firstly apply", HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public boolean isApplied(Long projectId) {

		Company company = userContext.getCurrentUser().getProfile().getCompany();

		Optional<Project> project = projectDao.findById(projectId);

		if(project.isPresent()) {
			return applicationDao.findByEmployeeAndProject(company, project.get()).isPresent();
		}

		throw new RestException("Nonexistent project", HttpStatus.BAD_REQUEST.value());
	}

	@Override
	public List<ProjectRequest> getMyRequests() {
		User user = userContext.getCurrentUser();
		Company company = user.getProfile().getCompany();
		return applicationDao.findByEmployee(company);
	}

	@Override
	public ProjectRequest getProjectRequest(Company employee, Project project) {
		Optional<ProjectRequest> projectRequest = applicationDao.findByEmployeeAndProject(employee, project);
		if(projectRequest.isPresent()) {
			return projectRequest.get();
		}
		return null;
	}

}
