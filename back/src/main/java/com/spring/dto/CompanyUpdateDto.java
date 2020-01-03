package com.spring.dto;

import java.util.Set;

import com.spring.entity.Company;
import com.spring.enums.CompanyType;

public class CompanyUpdateDto {

	private Long id;
	private String name;
	private String description;
	private Set<SkillDto> skills;
	private boolean isOwner;
	private String photoUrl;

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	private Long avgMark;

	public CompanyUpdateDto() {
	}

	public CompanyUpdateDto(Company company) {
		this.id = company.getId();
		this.name = company.getName();
		this.description = company.getDescription();
		this.avgMark = company.getAvgMark();
	}

	public Set<SkillDto> getSkills() {
		return skills;
	}

	public void setSkills(Set<SkillDto> skills) {
		this.skills = skills;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getAvgMark() {
		return avgMark;
	}

	public void setAvgMark(Long avgMark) {
		this.avgMark = avgMark;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean owner) {
		isOwner = owner;
	}

}
