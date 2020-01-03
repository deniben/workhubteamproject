package com.spring.dao.implementations;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.spring.component.UserContext;
import com.spring.dto.PageableResponse;
import com.spring.entity.User;
import com.spring.utils.Page;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.dao.SkillsDao;
import com.spring.entity.Skill;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


@Repository
public class SkillsDaoImpl extends BaseImpl<Skill> implements SkillsDao {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final Integer PAGE_SIZE = 9;


	@Override
	public Set<Skill> findByProject(Long id) {
		LOGGER.debug("in SkillDaoImpl findByProject({})", id);
		Session session = sessionFactory.getCurrentSession();
		List<Skill> result = session
				.createQuery("select s from Skill s where s.id in (select rs.id from Project p join p.requiredSkills rs where p.id = :id)",
						Skill.class)
				.setParameter("id", id)
				.getResultList();

		return new HashSet<>(result);
	}


	@Override
	public void deleteSkill(long id) {
		LOGGER.debug("in SkillDaoImpl deleteSkill({})", id);
		Session session = sessionFactory.getCurrentSession();

		Query<Skill> query = session.createNamedQuery("DeleteSkill");

		query.setParameter(1, id);
		query.setParameter(2, id);
		query.setParameter(3, id);

		query.executeUpdate();

	}

	@Override
	public Skill findSkillByName(String name) {
		LOGGER.debug("in SkillDaoImpl findSkillByName({})", name);
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Skill> criteriaQuery = criteriaBuilder.createQuery(Skill.class);
		Root<Skill> root = criteriaQuery.from(Skill.class);

		criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.equal(root.get("name"),
				name)));

		List result = session.createQuery(criteriaQuery).getResultList();

		if(!result.isEmpty()) {
			return (Skill) result.get(0);
		}
		return null;
	}

	@Override
	public PageableResponse findAll(Integer page, Optional<String> name, String currentUser) {


		Session session = sessionFactory.getCurrentSession();

		String query = "select count(*) from Skill p where p.name != :name";

		TypedQuery<Long> countPagesQuery = session.createQuery(query, Long.class)
				.setParameter("name", currentUser);

		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Skill> criteriaQuery = criteriaBuilder.createQuery(Skill.class);
		Root<Skill> userRoot = criteriaQuery.from(Skill.class);

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
