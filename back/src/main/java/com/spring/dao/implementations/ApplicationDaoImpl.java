package com.spring.dao.implementations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.spring.dao.ApplicationDao;
import com.spring.entity.Company;
import com.spring.entity.Project;
import com.spring.entity.ProjectRequest;
import com.spring.exception.OtherException;

@Repository
public class ApplicationDaoImpl extends BaseImpl<ProjectRequest> implements ApplicationDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationDaoImpl.class);

	@Override
	public Optional<ProjectRequest> findByEmployeeAndProject(Company employee, Project project) {
		LOGGER.debug("in ApplicationDaoImpl findByEmployeeAndProject({},{})", employee, project);
		Session session = sessionFactory.getCurrentSession();
		try {
			return Optional.of(session.createQuery("from ProjectRequest pr where pr.employee.id = :employee_id and pr.project.id = :project_id",
					ProjectRequest.class)
					.setParameter("employee_id", employee.getId())
					.setParameter("project_id", project.getId())
					.getSingleResult());
		} catch(Exception e) {
			return Optional.empty();
		}

	}

	@Override
	public List<ProjectRequest> findByEmployee(Company employee) {
		LOGGER.debug("in ApplicationDaoImpl findByEmployee({})", employee);
		Session session = sessionFactory.getCurrentSession();
		List<ProjectRequest> projectRequests = session.createQuery("from ProjectRequest PR where PR.employee = :employee and (select P.status from PR.project P) = 'NEW'",
				ProjectRequest.class)
				.setParameter("employee", employee)
				.getResultList();
		return projectRequests == null ? Collections.emptyList() : projectRequests;
	}

	@Override
	public List<ProjectRequest> findByAcceptedAndEmployee(Boolean accepted, Company employee) {
		LOGGER.debug("in ApplicationDaoImpl findByAcceptedAndEmployee({},{})", accepted,employee);

		Session session = sessionFactory.getCurrentSession();
		List<ProjectRequest> projectRequests = session.createQuery("from ProjectRequest pr where pr.accepted = :accepted and pr.employee.id = :employee_id", ProjectRequest.class)
				.setParameter("accepted", accepted)
				.setParameter("employee_id", employee.getId())
				.getResultList();
		return projectRequests == null ? Collections.emptyList() : projectRequests;
	}

	@Override
	public void deleteByAccepted(Boolean accepted) {
		LOGGER.debug("in ApplicationDaoImpl deleteByAccepted({})", accepted);

		Session session = sessionFactory.getCurrentSession();
		session.createQuery("delete from ProjectRequest pr where pr.accepted = :accepted")
				.setParameter("accepted", accepted)
				.executeUpdate();
	}
}
