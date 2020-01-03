package com.spring.controller;

import com.spring.enums.ProjectStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.service.RateService;

@RestController
@RequestMapping("/rates")
public class RateController {

    private final RateService rateService;
	private static final Logger LOGGER = LoggerFactory.getLogger(RateController.class);


	@Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @PostMapping("/projectId/{projectId}/rate/{rate}/status/{status}")
    public ResponseEntity<String> rateProject(@PathVariable Long projectId, @PathVariable Long rate, @PathVariable ProjectStatus status) {
		LOGGER.debug("in rateProject(), projectId: {}, rate: {}, projectStatus: {}", projectId, rate, status);
        rateService.rate(projectId, rate, status);
        return ResponseEntity.ok("Successfully saved rate");
    }

}
