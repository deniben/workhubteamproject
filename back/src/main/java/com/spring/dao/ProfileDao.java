package com.spring.dao;

import java.util.List;
import java.util.Optional;

import com.spring.entity.Company;
import com.spring.entity.Profile;

public interface ProfileDao extends BaseDao<Profile> {

	List<Profile> findApplicationsByCompany(Company company, boolean status, Integer page,Integer countOfItems);

	Integer getCountOfProfilesByCompany(Long companyId, boolean accepted);


}
