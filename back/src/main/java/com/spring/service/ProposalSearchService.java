package com.spring.service;

import com.spring.dto.SearchDto;
import com.spring.dto.SearchProposalDto;

public interface ProposalSearchService {
    SearchProposalDto searchWithProposal(SearchDto searchDto);
}
