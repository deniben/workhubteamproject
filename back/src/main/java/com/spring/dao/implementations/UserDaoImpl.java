package com.spring.dao.implementations;

import com.spring.component.UserContext;
import com.spring.dao.UserDao;
import com.spring.dto.PageableResponse;
import com.spring.entity.User;
import com.spring.utils.Page;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl extends BaseImpl<User> implements UserDao {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	UserContext userContext;

	private static final Integer PAGE_SIZE = 20;

	@Override
	public Optional<User> findByUsername(String username) {

		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

		Root<User> userRoot = criteriaQuery.from(User.class);
		criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("username"), username));

		List result = session.createQuery(criteriaQuery).getResultList();

		if(0 >= result.size()) {
			return Optional.empty();
		}
		return Optional.of((User) result.get(0));

	}

	@Override
	public void changePassword(String password) {
		Session session = sessionFactory.getCurrentSession();

		Long userId = userContext.getCurrentUser().getId();

		session.createQuery("update User u set u.password = :password where u.id = :id")
							.setParameter("password", password)
							.setParameter("id", userId)
							.executeUpdate();
	}

	@Override
	public PageableResponse findAll(Integer page, Optional<String> username) {

	    User currentUser = userContext.getCurrentUser();

		Session session = sessionFactory.getCurrentSession();

		String query = "select count(*) from User u where u.username != :username";
		TypedQuery<Long> countPagesQuery = session.createQuery(query, Long.class)
				.setParameter("username", currentUser.getUsername());
		
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> userRoot = criteriaQuery.from(User.class);

		criteriaQuery.select(userRoot);

		if(username.isPresent() && !username.get().isEmpty()) {

			criteriaQuery.where(
					criteriaBuilder.and(
							criteriaBuilder.like(
									criteriaBuilder.lower(userRoot.get("username")),
									username.get().toLowerCase() + "%"),
							criteriaBuilder.notEqual(userRoot.get("username"), currentUser.getUsername())
					)
			);

			query += " and lower(u.username) like CONCAT(:filter, '%')";
			countPagesQuery = session.createQuery(query, Long.class)
					.setParameter("username", currentUser.getUsername())
					.setParameter("filter", username.get().toLowerCase());

		} else {
			criteriaQuery.where(criteriaBuilder.notEqual(userRoot.get("username"), currentUser.getUsername()));
			criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("username")));
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
