package com.spring.dao.implementations;

import com.spring.component.UserContext;
import com.spring.dao.CompanyDao;
import com.spring.dao.ProjectDao;
import com.spring.dao.ProjectTypeDao;
import com.spring.entity.*;
import com.spring.enums.CompanyType;
import com.spring.enums.ProjectStatus;
import com.spring.exception.RestException;
import com.spring.service.SkillsService;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Repository
public class ProjectDaoImpl extends BaseImpl<Project> implements ProjectDao {

	public static final String COMPANY_EMPLOYEE = "companyEmployee";
	public static final String COMPANY_CREATOR = "companyCreator";
	public static final String STATUS = "status";
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDaoImpl.class);
	private final SessionFactory sessionFactory;
	private final CompanyDao companyDao;
	private final ProjectTypeDao projectTypeDao;
	private final SkillsService skillsService;
	private final UserContext userContext;

	private static final Integer PAGE_SIZE = 6;
	private static final Integer PROJECT_BY_STATUS_PAGE_SIZE = 6;

	@Autowired
	public ProjectDaoImpl(SessionFactory sessionFactory, CompanyDao companyDao,
	                      ProjectTypeDao projectTypeDao, SkillsService skillsService,
	                      UserContext userContext) {
		this.sessionFactory = sessionFactory;
		this.companyDao = companyDao;
		this.projectTypeDao = projectTypeDao;
		this.skillsService = skillsService;
		this.userContext = userContext;
	}

    @Override
    public Optional<Project> findRandomWithType(ProjectType projectType) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = criteriaQuery.from(Project.class);

		criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.equal(root.get("projectType"),
				projectType), criteriaBuilder.equal(root.get(STATUS), ProjectStatus.NEW)));

        List<Project> result = session.createQuery(criteriaQuery).getResultList();
        int rightBound = result.size();
        if (!result.isEmpty()) {
            int randomElement = ThreadLocalRandom.current().nextInt(rightBound);
            return  Optional.of(result.get(randomElement));
        }
        return Optional.empty();
    }

	@Override
	public List<Project> findBySkills(Set<Skill> skillSet) {
		if(skillSet.isEmpty()) {
			return new ArrayList<>();
		}

        Session session = sessionFactory.getCurrentSession();
        Query<Project> query = session.createNamedQuery("Project_findBySkills", Project.class);
        query.setParameterList("skills", skillSet.stream().map(Skill::getId).collect(Collectors.toList()));
        List<Project> projects = query.getResultList();
	    return projects == null ? Collections.emptyList() : projects;
    }

    @Override
    public List<Integer> getSkillsId(Long projectId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createNamedQuery("getSkillsId");
        query.setParameter("projectId", projectId);
	    List<Integer> skills = query.getResultList();
	    return skills == null ? Collections.emptyList() : skills;
    }

    @Override
    public List<Project> findByStatus(ProjectStatus projectStatus, Integer page) {
        Session session = sessionFactory.getCurrentSession();
        Company company = userContext.getCurrentUser().getProfile().getCompany();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Project> projectCriteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = projectCriteriaQuery.from(Project.class);
	    Fetch<Project, Skill> requiredSkills = root.fetch("requiredSkills", JoinType.INNER);

		projectCriteriaQuery.select(root)
				.where(criteriaBuilder.equal(root.get(STATUS), projectStatus))
				.orderBy(criteriaBuilder.asc(root.get("expiryDate")));

		List<Project> projects = session.createQuery(projectCriteriaQuery)
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(page * PAGE_SIZE)
				.getResultList();

        Hibernate.initialize(company.getProposedSkills());
        projects.sort(Comparator.comparingInt(x -> skillsService.calculateMatch(x, company.getProposedSkills())));
        return projects;
    }

	@Override
	public Integer numberOfInvite(Long projectId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery(ProjectRequest.NUMBER_OF_INVITE_QUERY);
		query.setParameter("projectId", projectId);

		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	public void acceptCompanyToProject(Long employeeId, Long projectId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery("acceptCompanyToProject");
		query.setParameter("employeeId", employeeId);
		query.setParameter("projectId", projectId);
		query.executeUpdate();

	}

	@Override
	public void rejectCompany(Long employeeId, Long projectId) {

		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery("rejectCompany");
		query.setParameter("employeeId", employeeId);
		query.setParameter("projectId", projectId);
		query.executeUpdate();
	}

	@Override
	public Integer checkingEmployeeRequest(Long projectId, Long employeeId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery("checkingEmployeeRequest");
		query.setParameter("employeeId", employeeId);
		query.setParameter("projectId", projectId);

		return ((Long) query.getSingleResult()).intValue();
	}

    @Override
    public void finishProject(Long projectId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createNamedQuery("deleteProject");
        query.setParameter("projectId", projectId);
        query.executeUpdate();
    }

	@Override
	public void completeProject(Long id) {

		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery("completingProject");
		query.setParameter("projectId", id);
		query.executeUpdate();
	}

	@Override
	public Integer numberOfMyProjects(ProjectStatus projectStatus) {

		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery("numberOfMyProjects");
		query.setParameter("projectStatus", projectStatus);
		query.setParameter("companyId", userContext.getCurrentUser().getProfile().getCompany().getId());

		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	public Integer existProject(Long projectId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery("existProject");
		query.setParameter("projectId", projectId);


		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	public List<Project> findByCompanyAndStatus(Company company, Long page, ProjectStatus... projectStatus) {

		if(projectStatus == null || projectStatus.length < 1) {
			throw new RestException("Invalid project status parameters");
		}

		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
		Root<Project> root = criteriaQuery.from(Project.class);

		String companyField = company.getType().equals(CompanyType.EMPLOYEE) ? COMPANY_EMPLOYEE : COMPANY_CREATOR;

		List<Predicate> predicates = new ArrayList<>();

		Arrays.stream(projectStatus).forEach(x ->
				predicates.add(criteriaBuilder.equal(root.get(STATUS), x)));

		root.fetch("requiredSkills", JoinType.INNER);
		criteriaQuery.select(root).where(
				criteriaBuilder.and(
						criteriaBuilder.equal(root.get(companyField), company),
						predicates.size() > 1 ? criteriaBuilder.or(predicates.toArray(new Predicate[]{})) : predicates.get(0)))
				.orderBy(criteriaBuilder.asc(root.get("name")));


            return session.createQuery(criteriaQuery)
                    .setFirstResult(page.intValue() * PROJECT_BY_STATUS_PAGE_SIZE)
                    .setMaxResults(PROJECT_BY_STATUS_PAGE_SIZE)
                    .getResultList();
    }

    @Override
    public List<Project> findByStatus(Company company, Long page,ProjectStatus projectStatus) {

        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("select project from Project project where project.status =:status and project.companyCreator.id =:employerId", Project.class)
                .setParameter("status", projectStatus)
                .setParameter("employerId", company.getId())
                .setFirstResult(page.intValue() * FOR_PROJECT_BY_EMPLOYER)
                .setMaxResults(FOR_PROJECT_BY_EMPLOYER)
                .getResultList();
    }


        @Override
    public void updateExpiredStatus() {
        Session session = sessionFactory.getCurrentSession();
        LocalDate localDate = LocalDate.now();
        ProjectStatus projectStatus = ProjectStatus.EXPIRED;

		session.createQuery("update Project p set p.status = :status where p.expiryDate < :date")
				.setParameter(STATUS, projectStatus)
				.setParameter("date", localDate)
				.executeUpdate();
	}

	@Override
	public Integer countByStatus(ProjectStatus projectStatus) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("select count(*) from Project p where p.status = :status", Long.class)
				.setParameter(STATUS, projectStatus)
				.getSingleResult().intValue();
	}

	@Override
	public List<Project> findByCompany(Long companyId, CompanyType companyType) {
		LOGGER.debug("in findByCompany(companyId=[{}], companyType=[{}]) method", companyId, companyType);
		String companyField = companyType.equals(CompanyType.EMPLOYEE) ? COMPANY_EMPLOYEE : COMPANY_CREATOR;

		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

		CriteriaQuery<Project> criteria = criteriaBuilder.createQuery(Project.class);
		Root<Project> projectRoot = criteria.from(Project.class);
		criteria.select(projectRoot);
		criteria.where(criteriaBuilder.equal(projectRoot.get(companyField), companyId));

		return session.createQuery(criteria).getResultList();
	}

	@Override
	public List<Project> findByCompany(Company id) {
		Session session = sessionFactory.getCurrentSession();
		Query<Project> query = session.createNamedQuery("Project_findByCompany", Project.class);
		query.setParameter("idCompany", id);
		return query.getResultList();
	}

    @Override
    public Integer forFullEnd(Long projectId){
        Session session = sessionFactory.getCurrentSession();

        return ((Long) session.createQuery("select count (*) from Project project where project.id =:projectId and project.employerMark = null")
                .setParameter("projectId",projectId)
                .getSingleResult()).intValue();
    }

    @Override
    public List<Project> findByType(ProjectType id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Project> query = session.createNamedQuery("Project_findByType", Project.class);
        query.setParameter("idType", id);
        return query.getResultList();
    }


	@Override
	public void changeStatus(Long projectId, ProjectStatus status) {

		Session session = sessionFactory.getCurrentSession();
		session.createNamedQuery("changeStatus")
				.setParameter("status", status)
				.setParameter("projectId", projectId)
				.executeUpdate();

	}

    @Override
    public List<Project> findAllProjectByCompany(Long id, Integer page, Optional<String> name){
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("select p from Project p join p.companyCreator c where c.id = :id and lower(p.name)" +
                "like CONCAT(:name, '%')", Project.class)
                .setParameter("id", id )
                .setParameter("name", name.orElse(""))
                .setFirstResult(page*5)
                .setMaxResults(5)
                .getResultList();
    }

	@Override
	public Integer countProjectsByCompany(Long id) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("select count(p) from Project p join p.companyCreator c where c.id = :id", Long.class)
				.setParameter("id", id)
				.getSingleResult().intValue();
	}

    @Override
    public Integer countFinishedProjectsNotifications(Company company) {
        Session session = sessionFactory.getCurrentSession();
        String companyField = company.getType().equals(CompanyType.EMPLOYEE) ? COMPANY_EMPLOYEE : COMPANY_CREATOR;
        return session.createQuery("select count(*) from Project p where p." + companyField + ".id = :company_id and p.status in :statuses and p.employerMark is null",
                Long.class)
                .setParameter("company_id", company.getId())
                .setParameterList("statuses", Arrays.asList(ProjectStatus.COMPLETED, ProjectStatus.FAILED))
                .getSingleResult().intValue();
    }


    @Override
	public void merge(Project project){
	    sessionFactory.getCurrentSession().merge(project);
    }
}

