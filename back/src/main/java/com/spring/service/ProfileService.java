package com.spring.service;

import com.spring.entity.Company;
import com.spring.entity.FileDocument;
import com.spring.entity.Profile;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface ProfileService {

	void useForeignAccount(OAuth2User oAuth2User);

	Profile save(Profile profile);

	Profile update(Profile profile);

	void delete(long id);

	Profile findById(long id);

	List<Profile> findApplicationsByCompany(Company company, boolean status, Integer page, Integer countOfItems);

    Boolean isUserOwner();

    Boolean isUserDocumentOwner(FileDocument fileDocument);

    Integer getNumberOfPages(Long companyId, boolean accepted,Integer countOfItems);

}

