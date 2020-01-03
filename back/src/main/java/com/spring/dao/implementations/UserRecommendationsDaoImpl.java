package com.spring.dao.implementations;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.UserRecommendationsDao;
import com.spring.entity.User;
import com.spring.entity.UserRecommendations;

@Repository
@Transactional
public class UserRecommendationsDaoImpl extends BaseImpl<UserRecommendations> implements UserRecommendationsDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<UserRecommendations> getUserRecommendations(User user) {

		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<UserRecommendations> criteriaQuery = criteriaBuilder.createQuery(UserRecommendations.class);
		Root<UserRecommendations> recommendationsRoot = criteriaQuery.from(UserRecommendations.class);

		criteriaQuery.select(recommendationsRoot).where(criteriaBuilder.equal(recommendationsRoot.get("profile"), user.getProfile()));

		List result = session.createQuery(criteriaQuery).getResultList();

		if (result.size() > 0) {
			return (List<UserRecommendations>) result;
		}

		return result;

	}

}
