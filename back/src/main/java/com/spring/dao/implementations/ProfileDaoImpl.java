package com.spring.dao.implementations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.spring.dao.ProfileDao;
import com.spring.entity.Company;
import com.spring.entity.Profile;


@Repository
public class ProfileDaoImpl extends BaseImpl<Profile> implements ProfileDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileDaoImpl.class);

	@Override
	public List<Profile> findApplicationsByCompany(Company company, boolean status, Integer page,Integer countOfItems) {
		LOGGER.debug("in ProfileDaoImpl findApplicationsByCompany({},{},{})", company, status, page);
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();

		CriteriaQuery<Profile> criteriaQuery = criteriaBuilder.createQuery(Profile.class);
		Root<Profile> profileRoot = criteriaQuery.from(Profile.class);

		criteriaQuery.select(profileRoot).where(criteriaBuilder.and(criteriaBuilder.equal(profileRoot.get("company"), company),
				criteriaBuilder.equal(profileRoot.get("accepted"), status)));

		List<Profile> profiles = session.createQuery(criteriaQuery)
				.setFirstResult(page * countOfItems)
				.setMaxResults(countOfItems)
				.getResultList();
		return profiles == null ? Collections.emptyList() : profiles;

	}

	@Override
	public Integer getCountOfProfilesByCompany(Long companyId,  boolean accepted) {
		LOGGER.trace("in ProfileDaoImpl getCountOfProfilesByCompany()");
		Session session = sessionFactory.getCurrentSession();

		return ((Long) session.createNamedQuery("getCountOfProfilesByCompany")
				.setParameter("companyId", companyId)
				.setParameter("accepted" , accepted)
				.getSingleResult()).intValue();
	}
}


