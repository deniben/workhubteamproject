package com.spring.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {

	List<T> findAll();

	void save(T t);

	Optional<T> delete(long id);

	Optional<T> findById(long id);

}
