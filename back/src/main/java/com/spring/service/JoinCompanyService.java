package com.spring.service;

import com.spring.entity.Profile;
import com.spring.entity.User;

import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface JoinCompanyService {

    List<Profile> getMembershipApplications(Integer page);

    void acceptMembership(Long userId);

    void rejectMembership(Long userId);

    void joinCompany(String companyName);


}
