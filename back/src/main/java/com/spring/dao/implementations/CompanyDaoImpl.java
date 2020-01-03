package com.spring.dao.implementations;

import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spring.dao.ProjectDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.spring.dao.CompanyDao;
import com.spring.entity.Company;
import com.spring.enums.CompanyType;

@Repository
public class CompanyDaoImpl extends BaseImpl<Company> implements CompanyDao {

	private final SessionFactory sessionFactory;
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImpl.class);
	public static final String EMPLOYEE_RATE = "employeeMark";
	public static final String EMPLOYER_RATE = "employerMark";


	public CompanyDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

  @Override
  public List<Company> allCompanies(Integer firstResult, Integer maxResult) {
	  LOGGER.trace("in AllCompanies()");

	  Session session = sessionFactory.getCurrentSession();
    Query<Company> query = session.createNamedQuery("getAllUnblockedCompanies", Company.class);
    return query
      .setFirstResult(firstResult*maxResult)
      .setMaxResults(maxResult)
      .getResultList();
  }

  @Override
  public Optional<Company> findByName(String name) {
	  LOGGER.debug("in findByName(), name: {}", name);

	  Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder companyCriteriaBuilder = session.getCriteriaBuilder();
    CriteriaQuery<Company> companyCriteriaQuery = companyCriteriaBuilder.createQuery(Company.class);
    Root<Company> root = companyCriteriaQuery.from(Company.class);

    companyCriteriaQuery.select(root).where(
      companyCriteriaBuilder.equal(
        companyCriteriaBuilder.lower(root.get("name")), name.toLowerCase()
      )
    );

    List<Company> result = session.createQuery(companyCriteriaQuery).getResultList();

    if (result.size() > 0) {
      return Optional.of(result.get(0));
    }

    return Optional.empty();

  }

  @Override

  public List<Company> topComp(CompanyType companyType) {
	  LOGGER.debug("in findByName(), name: {}", companyType);
	  Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
    CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
    Root<Company> root = criteriaQuery.from(Company.class);
    criteriaQuery.select(root);
    criteriaQuery.where(criteriaBuilder.equal(root.get("type"), companyType));
    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("avgMark")));
    Query<Company> query = session.createQuery(criteriaQuery);
    return session.createQuery(criteriaQuery).list();
  }


    @Override
    public List<Company>findAllCompanyForMyProject(Long id, Integer page){
	    LOGGER.debug("in findAllCompanyForMyProject(), id: {}, page: {}", id, page);
	    Session session = sessionFactory.getCurrentSession();
        Query<Company> query = session.createNamedQuery("getCompaniesForMyProject", Company.class)
                .setFirstResult(page * 4)
                .setMaxResults(4);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public Integer numberOfCompanyToMyProject(Long id){
	    LOGGER.debug("in numberOfCompanyToMyProject(), id: {}", id);

	    Session session  = sessionFactory.getCurrentSession();
        Query query = session.createNamedQuery("numberOfCompanyToMyProject");
        query.setParameter("id", id);

        return  ( (Long) query.getSingleResult()).intValue();
    }

	@Override
	public Company getCompanyByProjectId(Long id) {
		LOGGER.debug("in getCompanyByProjectId(), id: {}", id);
		Session session = sessionFactory.getCurrentSession();
		Query<Company> query = session.createNamedQuery("getCompanyByProjectId", Company.class);
		query.setParameter("id", id);

    return query.getSingleResult();
  }

  @Override
  public List<Company> blockedCompanies(Integer firstResult, Integer maxResult) {
	  LOGGER.trace("in blockedCompanies()");
	  Session session = sessionFactory.getCurrentSession();
    Query<Company> query = session.createNamedQuery("getBlockedCompanies", Company.class);
    return query
      .setFirstResult(firstResult*maxResult)
      .setMaxResults(maxResult)
      .getResultList();
  }

  @Override
  public Company saveAndReturn(Company company) {
	  LOGGER.debug("in saveAndReturn(), company: {}", company);

	  super.save(company);
    return sessionFactory.getCurrentSession().createQuery("from Company c where c.name = :name", Company.class)
      .setParameter("name", company.getName())
      .getSingleResult();
  }

	@Override
	public void updateAverageRate(Company company) {
		LOGGER.debug("in updateAverageRate(), company: {}", company);
		Session session = sessionFactory.getCurrentSession();
		String rateFieldName = company.getType().equals(CompanyType.EMPLOYEE) ? EMPLOYEE_RATE : EMPLOYER_RATE;
		String companyFieldName = company.getType().equals(CompanyType.EMPLOYEE) ? ProjectDao.COMPANY_EMPLOYEE : ProjectDao.COMPANY_CREATOR;
		session.createQuery("update Company c set c.avgMark = (select avg(p." + rateFieldName + ") from Project p where p." + companyFieldName + ".id = :company_id) where c.id = :company_id")
				.setParameter("company_id", company.getId())
				.executeUpdate();
	}

  @Override
  public Integer numberOfAllUnblockedCompanies() {
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createNamedQuery("getCountOfUnblockedAllCompanies");
    return ((Long) query.getSingleResult()).intValue();
  }

  @Override
  public Integer numberOfAllBlockedCompanies() {
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createNamedQuery("getCountOfBlockedAllCompanies");
    return ((Long) query.getSingleResult()).intValue();
  }
}
