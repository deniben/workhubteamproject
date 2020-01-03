package com.spring.dto;

public class SearchDto {

    private String name;

    private Long typeId;

    private Double minBudget;

    private Double maxBudget;

    private Integer page = 0;

    public SearchDto() {}

    public SearchDto(SearchDto searchDto) {
        this.name = searchDto.getName();
        this.typeId = searchDto.getTypeId();
        this.minBudget = searchDto.getMinBudget();
        this.maxBudget = searchDto.getMaxBudget();
        this.page = searchDto.getPage();
    }

    //for tests
    public SearchDto(String name) {
        this.name = name;
    }

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Double getMinBudget() {
        return minBudget;
    }

    public void setMinBudget(Double minBudget) {
        this.minBudget = minBudget;
    }

    public Double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(Double maxBudget) {
        this.maxBudget = maxBudget;
    }

}
