package com.spring.dao.implementations;

import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.spring.dao.ProjectTypeDao;
import com.spring.dto.PageableResponse;
import com.spring.entity.ProjectType;
import com.spring.utils.Page;

@Repository
public class ProjectTypeDaoImpl extends BaseImpl<ProjectType> implements ProjectTypeDao {

	private static final Integer PAGE_SIZE = 9;

	@Override
	public Optional<ProjectType> findProjectTypeByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<ProjectType> criteriaQuery = criteriaBuilder.createQuery(ProjectType.class);
		Root<ProjectType> root = criteriaQuery.from(ProjectType.class);

		criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.equal(root.get("name"),
				name)));

		try {
			return Optional.of(session.createQuery(criteriaQuery).getSingleResult());
		}catch(NoResultException e){
			return Optional.empty();
		}
	}

	@Override
	public void deleteType(long id) {

		Session session = sessionFactory.getCurrentSession();

		Query<ProjectType> query = session.createNamedQuery("DeleteType");

		query.setParameter(1, id);
		query.setParameter(2, id);


		query.executeUpdate();
	}

	@Override
	public List<ProjectType> findAllNotBlocked() {
		Session session = sessionFactory.getCurrentSession();
		Query<ProjectType> query = session.createNamedQuery("ProjectTypeType_notBlocked", ProjectType.class);

		return query.getResultList();
	}

	@Override
	public void blockedType(long projectType) {
		Session session = sessionFactory.getCurrentSession();

		Query<ProjectType> query = session.createNamedQuery("BlockedType");
		query.setParameter("projectType", projectType);

		query.executeUpdate();
	}

	@Override
	public void unBlockedType(long projectType) {
		Session session = sessionFactory.getCurrentSession();

		Query<ProjectType> query = session.createNamedQuery("UnBlockedType");
		query.setParameter("projectType", projectType);

		query.executeUpdate();
	}

	@Override
	public PageableResponse findAll(Integer page, Optional<String> name, String currentUser) {


		Session session = sessionFactory.getCurrentSession();

		String query = "select count(*) from ProjectType p where p.name != :name";

		TypedQuery<Long> countPagesQuery = session.createQuery(query, Long.class)
				.setParameter("name", currentUser);
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<ProjectType> criteriaQuery = criteriaBuilder.createQuery(ProjectType.class);
		Root<ProjectType> userRoot = criteriaQuery.from(ProjectType.class);
		criteriaQuery.select(userRoot);
		if(name.isPresent() && !name.get().isEmpty()) {
			criteriaQuery.where(
					criteriaBuilder.and(
							criteriaBuilder.like(
									criteriaBuilder.lower(userRoot.get("name")),
									name.get().toLowerCase() + "%"),
							criteriaBuilder.notEqual(userRoot.get("name"), currentUser)
					)
			);
			query += " and lower(p.name) like CONCAT(:filter, '%')";
			countPagesQuery = session.createQuery(query, Long.class)
					.setParameter("name", currentUser)
					.setParameter("filter", name.get().toLowerCase());
		} else {
			criteriaQuery.where(criteriaBuilder.notEqual(userRoot.get("name"), currentUser));
			criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("name")));
		}

		return new PageableResponse(Page.countPages(
				countPagesQuery.getSingleResult().intValue()
				, PAGE_SIZE),
				session.createQuery(criteriaQuery)
						.setFirstResult(page * PAGE_SIZE)
						.setMaxResults(PAGE_SIZE)
						.getResultList());
	}
}
