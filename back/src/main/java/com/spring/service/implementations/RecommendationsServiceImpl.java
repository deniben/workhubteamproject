package com.spring.service.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.spring.service.RecommendationsService;
import com.spring.component.UserContext;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.CompanyDao;
import com.spring.dao.ProjectDao;
import com.spring.dao.ProjectTypeDao;
import com.spring.dao.UserRecommendationsDao;
import com.spring.entity.Company;
import com.spring.entity.Profile;
import com.spring.entity.Project;
import com.spring.entity.ProjectType;
import com.spring.entity.User;
import com.spring.entity.UserRecommendations;

@Service
@Transactional
public class RecommendationsServiceImpl implements RecommendationsService {

	private final UserContext userContext;
	private final ProjectDao projectDao;
	private final ProjectTypeDao projectTypeDao;
	private final UserRecommendationsDao userRecommendationsDao;
	private final CompanyDao companyDao;
	private final SessionFactory sessionFactory;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	public RecommendationsServiceImpl(UserContext userContext, ProjectDao projectDao,
	                                  ProjectTypeDao projectTypeDao, UserRecommendationsDao userRecommendationsDao,
	                                  CompanyDao companyDao, SessionFactory sessionFactory) {
		this.userContext = userContext;
		this.projectDao = projectDao;

		this.projectTypeDao = projectTypeDao;
		this.userRecommendationsDao = userRecommendationsDao;
		this.companyDao = companyDao;
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Project> generaterecomendations() {

		LOGGER.info("Generating recommendations for employee");

		List<Project> projects = new ArrayList<>();

		List<UserRecommendations> userRecommendations = userRecommendationsDao.getUserRecommendations(userContext.getCurrentUser());

		if(userRecommendations.size() == 0) {

			List<Project> allProjects = projectDao.findAll();

			return allProjects.size() > 3 ? allProjects.subList(0, 3) : allProjects;
		}

		Collections.shuffle(userRecommendations);

		List<ProjectType> projectTypes = userRecommendations.stream().map(x -> x.getProjectType()).collect(Collectors.toList());

		if(projectTypes.size() >= 3) {
			projectTypes = projectTypes.subList(0, 3);
		}

		for(ProjectType type : projectTypes) {

			Optional<Project> randomProject = projectDao.findRandomWithType(type);

			randomProject.ifPresent(projects::add);
		}

		LOGGER.info("Fetched recommendations. {} projects", projects.size());

		return projects;

	}

	@Override
	public void updateValuation(ProjectType projectType, Float weight) {

		User user = userContext.getCurrentUser();

		Profile profile = user.getProfile();
		List<UserRecommendations> userRecommendations = userRecommendationsDao.getUserRecommendations(user);

		if(userRecommendations.size() > 0) {

			userRecommendations = addRecommendation(resetIfBound(userRecommendations), profile, projectType, weight);

			if(userRecommendations.size() > 6) {
				userRecommendations.sort((x, y) -> (int) (x.getValuation() * 10 - y.getValuation() * 10));

				userRecommendations.subList(6, userRecommendations.size()).stream()
						.forEach(x -> {
							userRecommendationsDao.delete(x.getId());
						});
				userRecommendations = userRecommendations.subList(0, 6);
			}

			userRecommendations.stream().forEach(x -> {
				userRecommendationsDao.save(x);
			});

		} else {
			fillEmpty(profile, weight, projectType, userRecommendations);
		}

		LOGGER.info("Updated " + projectType.getName() + " project type's history");

	}

	private List<UserRecommendations> addRecommendation(List<UserRecommendations> userRecommendations, Profile profile, ProjectType projectType, Float weight) {

		UserRecommendations currentRec = new UserRecommendations();

		currentRec.setProfile(profile);
		currentRec.setProjectType(projectType);

		if(userRecommendations.stream().anyMatch(x -> x.getProjectType().getId().equals(projectType.getId()))) {

			currentRec = userRecommendations.stream().filter(x -> x.getProjectType().getId().equals(projectType.getId())).findFirst().get();
			userRecommendations.remove(currentRec);

			currentRec.setValuation(currentRec.getValuation() + weight);

		} else {
			currentRec.setValuation(weight);
		}

		userRecommendations.add(currentRec);

		return userRecommendations;

	}

	private void fillEmpty(Profile profile, Float weight, ProjectType projectType, List<UserRecommendations> userRecommendations) {

		UserRecommendations newRec = new UserRecommendations();
		newRec.setProfile(profile);
		newRec.setValuation(weight);
		newRec.setProjectType(projectType);

		userRecommendations.add(newRec);

		List<ProjectType> projectTypes = projectTypeDao.findAll().stream()
				.filter(x -> !x.getId().equals(projectType.getId()))
				.limit(5)
				.collect(Collectors.toList());

		userRecommendationsDao.save(newRec);
		projectTypes.stream().forEach(x -> {

			UserRecommendations tmpRec = new UserRecommendations();
			newRec.setProfile(profile);
			newRec.setValuation(0f);
			newRec.setProjectType(x);

			userRecommendationsDao.save(newRec);
		});

	}

	private List<UserRecommendations> resetIfBound(List<UserRecommendations> userRecommendations) {

		Session session = sessionFactory.getCurrentSession();

		return userRecommendations.stream().map(x -> {

			if(x.getValuation() >= 10) {
				x.setValuation(0f);
				session.merge(x);
			}

			return x;

		}).collect(Collectors.toList());

	}

	@Override
	public List<Project> generateRecommendationsBySkills() {

		LOGGER.info("Generating recommendations by skills");

		Profile profile = userContext.getCurrentUser().getProfile();
		Company company = companyDao.findById(profile.getCompany().getId()).get();

		Hibernate.initialize(company.getProposedSkills());

		List<Project> projects = projectDao.findBySkills(company.getProposedSkills());

		LOGGER.info(projects.size() + " project found by skills");

		if(projects.size() > 0) {
			Collections.shuffle(projects);
			return projects.size() >= 3 ? projects.subList(0, 3) : projects;
		}

		return projects;
	}
}
