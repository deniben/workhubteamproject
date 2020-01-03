package com.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.SearchDto;
import com.spring.dto.SearchProposalDto;
import com.spring.service.ProposalSearchService;

@RestController
@RequestMapping("/search")
public class SearchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
    private final ProposalSearchService proposalSearchService;

    @Autowired
    public SearchController(ProposalSearchService proposalSearchService) {
    	this.proposalSearchService = proposalSearchService;
    }

    @PostMapping
    public ResponseEntity<SearchProposalDto> search(@RequestBody(required = false) SearchDto searchDto) {
    	LOGGER.debug("in search(), searchDto: {}", searchDto);
        SearchProposalDto searchResult = proposalSearchService.searchWithProposal(searchDto);
        return ResponseEntity.status(HttpStatus.OK).body(searchResult);
    }

}
