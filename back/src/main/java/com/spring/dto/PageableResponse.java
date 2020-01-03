package com.spring.dto;

import java.util.List;

public class PageableResponse {

	private Integer pagesCount;

	private List<?> items;


	public PageableResponse() {}

	public PageableResponse(Integer pagesCount, List<?> items) {
		this.items = items;
		this.pagesCount = pagesCount;
	}


	public Integer getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(Integer pagesCount) {
		this.pagesCount = pagesCount;
	}

	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
}
