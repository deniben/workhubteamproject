package com.spring.dto;

import java.time.LocalDate;
import java.util.Set;

import com.spring.entity.ProjectType;
import com.spring.entity.Skill;
import com.spring.enums.ProjectStatus;

public class ProjectDto {

	private Long id;

	private String name;

	private String description;

	private ProjectType projectType;

	private CompanyDto companyCreator;

	private ProjectStatus status;

	private Double budget;

	private int numberOfInvite;

	private LocalDate expiryDate;

	private String photoUrl;

	private Set<Skill> skills;


	private Integer flagForRate;

	public ProjectDto() {}

	public Integer getFlagForRate() {
		return flagForRate;
	}

	public void setFlagForRate(Integer flagForRate) {
		this.flagForRate = flagForRate;
	}

	public Set<Skill> getSkills() {
		return skills;
	}

	public void setSkills(Set<Skill> skills) {
		this.skills = skills;
	}
	public ProjectType getProjectType() {
		return projectType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {return name; }

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CompanyDto getCompanyCreator() {
		return companyCreator;
	}

	public void setCompanyCreator(CompanyDto companyCreator) {
		this.companyCreator = companyCreator;
	}

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Object expiryDate) {

		if (expiryDate instanceof String) {
			this.expiryDate = LocalDate.parse((String)expiryDate);
		} else {
			this.expiryDate = (LocalDate) expiryDate;
		}
	}

	public String getPhoto() {
		return photoUrl;
	}

	public void setPhoto(String photoUrl) {
		this.photoUrl = photoUrl;
	}


	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public ProjectStatus getProjectStatus() {
		return status;
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		this.status = status;
	}

	public int getNumberOfInvite() {
		return numberOfInvite;
	}

	public void setNumberOfInvite(int numberOfInvite) {
		this.numberOfInvite = numberOfInvite;
	}
}
