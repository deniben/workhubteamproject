package com.spring.dto;

import java.util.List;

public class SearchProposalDto {

    private String proposalName;

    private List<ProjectDto> result;

    private Integer pagesCount;

    public SearchProposalDto() {}

    public String getProposalName() {
        return proposalName;
    }

    public void setProposalName(String proposalName) {
        this.proposalName = proposalName;
    }

    public List<ProjectDto> getResult() {
        return result;
    }

    public void setResult(List<ProjectDto> result) {
        this.result = result;
    }

	public Integer getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(Integer pagesCount) {
		this.pagesCount = pagesCount;
	}
}
