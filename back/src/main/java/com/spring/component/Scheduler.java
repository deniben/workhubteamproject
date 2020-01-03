package com.spring.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.ApplicationDao;
import com.spring.dao.ProjectDao;

@Component
@Transactional
public class Scheduler {

    private final ProjectDao projectDao;
	private final ApplicationDao applicationDao;

    @Autowired
    public Scheduler(ProjectDao projectDao, ApplicationDao applicationDao) {
        this.projectDao = projectDao;
	    this.applicationDao = applicationDao;
    }

    @Scheduled(cron = "0 0 13 * * ?")
    public void projectExpiryDateControl() {
        projectDao.updateExpiredStatus();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void newProjectsNotifications() {
	    applicationDao.deleteByAccepted(true);
    }

}
