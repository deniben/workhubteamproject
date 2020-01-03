package com.spring.service;

import com.spring.entity.Company;
import com.spring.enums.CompanyType;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    List<Company> findAllUnblockedCompanies(Integer firstResult, Integer maxResult);

    List<Company> findAllUnblockedCompanies();

    List<Company> findAllBlockedCompanies(Integer firstResult, Integer maxResult);

    Company saveCompany(Company company);

    Company findMyCompany();

    Company deleteCompany(long id);

    Company findCompanyById(long id);

    List<Company> topCompany(CompanyType companyType);

    Company updateCompany(Company company);

    Company findCompanyByIdCheckOwner(long companyId);

    List<Company> findAllCompaniesForMyProject(Long id, Integer page);

    Integer numberOfAllCompanyToMyProjectPages(Long id);

    Integer numberOfAllUnblockedCompanyPages();

    Integer numberOfAllBlockedCompanyPages();

    Company getCompanyByProjectId(Long id);

    void blockUnblockCompany(Long id, boolean isBlocked);

    Optional<Company> findByName(String name);

}
