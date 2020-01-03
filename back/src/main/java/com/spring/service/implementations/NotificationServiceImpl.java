package com.spring.service.implementations;

import com.spring.dao.ProjectDao;
import com.spring.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.service.NotificationService;
import com.spring.component.UserContext;
import com.spring.dao.ApplicationDao;
import com.spring.entity.User;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

	private final ApplicationDao applicationDao;
	private final UserContext userContext;
	private final ProjectDao projectDao;

	@Autowired
	public NotificationServiceImpl(ApplicationDao applicationDao, UserContext userContext, ProjectDao projectDao) {
		this.applicationDao = applicationDao;
		this.userContext = userContext;
		this.projectDao = projectDao;
	}

	@Override
	public Integer countNewProjects() {
		User user = userContext.getCurrentUser();
		return applicationDao.findByAcceptedAndEmployee(true, user.getProfile().getCompany()).size() + countFinishedProjects();
	}

	@Override
	public Integer countFinishedProjects() {
		User user = userContext.getCurrentUser();
		Company company = user.getProfile().getCompany();
		if(!company.getOwner().getId().equals(user.getProfile().getId())) {
			return 0;
		}
		return projectDao.countFinishedProjectsNotifications(user.getProfile().getCompany());
	}

}
