package com.spring.dao;

import java.util.Optional;

import com.spring.dto.CompanyDto;
import com.spring.entity.Company;
import com.spring.enums.CompanyType;

import java.util.List;

public interface CompanyDao extends BaseDao<Company> {

  List<Company> allCompanies(Integer firstResult, Integer maxResult);

  Optional<Company> findByName(String name);

  List<Company> blockedCompanies(Integer firstResult, Integer maxResult);

  List<Company> topComp(CompanyType companyType);

  List<Company> findAllCompanyForMyProject(Long id, Integer page);

  Integer numberOfCompanyToMyProject(Long id);

  Integer numberOfAllUnblockedCompanies();

  Integer numberOfAllBlockedCompanies();

  Company getCompanyByProjectId(Long id);

  Company saveAndReturn(Company company);

  void updateAverageRate(Company company);

}
