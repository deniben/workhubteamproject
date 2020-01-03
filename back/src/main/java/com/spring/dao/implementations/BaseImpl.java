package com.spring.dao.implementations;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.dao.BaseDao;

public abstract class BaseImpl<T> implements BaseDao<T> {

	private Class<T> entityClass;

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public BaseImpl() {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public List<T> findAll() {
		LOGGER.trace("in BaseImpl findAll()");
		List<T> t = sessionFactory.getCurrentSession().createQuery("from " + entityClass.getName()).list();
		return t == null ? Collections.emptyList() : t;
	}

	@Override
	public void save(T t) {
		LOGGER.debug("in BaseImpl save({})", t);
		sessionFactory.getCurrentSession().saveOrUpdate(t);
	}

	@Override
	public Optional<T> delete(long id) {
		LOGGER.debug("in BaseImpl delete({})", id);
		Optional<T> t = findById(id);
		if(t.isPresent()) {
			sessionFactory.getCurrentSession().delete(t.get());
			return t;
		}
		return Optional.empty();
	}

	@Override
	public Optional<T> findById(long id) {
		LOGGER.debug("in BaseImpl findById({})", id);
		try{
			return Optional.of(sessionFactory.getCurrentSession().get(entityClass, id));
		}catch(NullPointerException e){
			return Optional.empty();
		}
	}
}
