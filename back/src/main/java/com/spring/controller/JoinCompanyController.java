package com.spring.controller;

import com.spring.component.UserContext;
import com.spring.dto.PageableResponse;
import com.spring.dto.ProfileDto;
import com.spring.exception.RestException;
import com.spring.service.JoinCompanyService;
import com.spring.service.ProfileService;
import com.spring.utils.mapper.ProfileMapper;
import com.spring.service.implementations.MailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/companies")
public class JoinCompanyController {

  private UserContext userContext;
  private final JoinCompanyService joinCompanyService;
	private final ProfileService profileService;
	private final ProfileMapper profileMapper;

	private static final Logger LOGGER = LoggerFactory.getLogger(JoinCompanyController.class);


  @Autowired
  public JoinCompanyController(UserContext userContext, JoinCompanyService joinCompanyService, MailService mailService, ProfileService profileService, ProfileMapper profileMapper) {
	  this.userContext = userContext;
	  this.joinCompanyService = joinCompanyService;
	  this.profileService = profileService;
	  this.profileMapper = profileMapper;
  }


  @GetMapping("/join/{name}")
  public ResponseEntity<String> sendRequest(@PathVariable String name) {
  	LOGGER.debug("in sendRequest(), name: {}", name);
    joinCompanyService.joinCompany(name);
    return ResponseEntity.ok("Successfully sent");
  }

    @GetMapping("/accepting-member/{id}")
    public ResponseEntity<String> accept(@PathVariable Long id) {
        LOGGER.debug("in accept() with id: [{}]", id);
        if (id == null) {
            throw new RestException("Invalid user id", HttpStatus.BAD_REQUEST.value());
        }
        joinCompanyService.acceptMembership(id);

        return ResponseEntity.ok("Accepted");
    }

    @GetMapping("/rejection-member/{id}")
    public ResponseEntity<String> reject(@PathVariable Long id) {
        LOGGER.debug("in reject() with id: [{}]", id);
        if (id == null) {
            throw new RestException("Invalid user id", HttpStatus.BAD_REQUEST.value());
        }
        joinCompanyService.rejectMembership(id);
        return ResponseEntity.ok("Rejected");
    }

    @GetMapping("/applications/{pageNumber}")
    public ResponseEntity<PageableResponse> getAllApplications(@PathVariable Integer pageNumber) {
        LOGGER.debug("in getAllApplications() with pageNumber: [{}]", pageNumber);
        Long companyId = userContext.getCurrentUser()
                .getProfile().getCompany().getId();
        List<ProfileDto> profileDtos = profileService.findApplicationsByCompany(userContext.getCurrentUser()
                .getProfile().getCompany(), false, pageNumber,1)
                .stream()
                .map(profileMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PageableResponse(profileService.getNumberOfPages(companyId, false,1), profileDtos));
    }

}

