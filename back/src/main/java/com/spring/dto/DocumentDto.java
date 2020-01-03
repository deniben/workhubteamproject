package com.spring.dto;

import java.time.LocalDate;

public class DocumentDto {

	private String id;

	private LocalDate date;

	private CompanyDto companyOwner;

	private String name;

	private Boolean isOwner;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CompanyDto getCompanyOwner() {
		return companyOwner;
	}

	public void setCompanyOwner(CompanyDto companyOwner) {
		this.companyOwner = companyOwner;
	}

	public Boolean getOwner() {
		return isOwner;
	}

	public void setOwner(Boolean owner) {
		isOwner = owner;
	}
}
