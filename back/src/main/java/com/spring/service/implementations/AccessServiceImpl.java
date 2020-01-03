package com.spring.service.implementations;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.component.UserContext;
import com.spring.entity.Company;
import com.spring.entity.User;
import com.spring.exception.RestException;
import com.spring.service.AccessService;

@Service
public class AccessServiceImpl implements AccessService {

	private static final String NOT_AUTHORIZED = "auth_fail";
	private static final String PROFILE_NOT_FILLED = "prof_is_empty";
	private static final String ADMIN = "ADMIN";
	private static final String COMPANY_NEEDED = "comp_is_empty";
	private static final String EMPLOYEE = "EMPLOYEE";
	private static final String EMPLOYER = "EMPLOYER";
	private static final String COMPANY_IS_NOT_ACCEPTED = "comp_is_not_accepted";
	private static final String BLOCKED_COMPANY = "company_is_blocked";
	private UserContext userContext;
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessServiceImpl.class);

	@Autowired
	public AccessServiceImpl(UserContext userContext) {
		this.userContext = userContext;
	}

	public ResponseEntity<String> access(HttpServletRequest request) {
		LOGGER.debug("in AccessServiceImpl access({})", request);
		User user;
		try {
			user = userContext.getCurrentUser();
		} catch(RestException rest) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED);
		}
		if(user.getProfile() == null) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(PROFILE_NOT_FILLED);
		}

		if(Boolean.TRUE.equals(user.isAdmin())) {
			return ResponseEntity.status(HttpStatus.OK).body(ADMIN);
		}

		if(user.getProfile().getCompany() == null) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(COMPANY_NEEDED);
		}

		if(Boolean.TRUE.equals(user.getProfile().getAccepted())) {
			Company company = user.getProfile().getCompany();
			if(Boolean.TRUE.equals(company.isBlocked())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BLOCKED_COMPANY);
			}
			if(company.getType().toString().equals(EMPLOYEE)) {
				return ResponseEntity.status(HttpStatus.OK).body(EMPLOYEE);
			}
			if(company.getType().toString().equals(EMPLOYER)) {
				return ResponseEntity.status(HttpStatus.OK).body(EMPLOYER);
			}

		}
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(COMPANY_IS_NOT_ACCEPTED);
	}

}


