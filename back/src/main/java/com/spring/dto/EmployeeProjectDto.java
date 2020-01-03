package com.spring.dto;

import java.util.HashSet;
import java.util.Set;

import com.spring.entity.Skill;

public class EmployeeProjectDto extends ProjectDto {

	public EmployeeProjectDto() {
	}

	private Set<Skill> skillSet = new HashSet<>();

	private Integer skillsMatch;

	private Boolean isNew;

	public Set<Skill> getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(Set<Skill> skillSet) {
		this.skillSet = skillSet;
	}

	public Integer getSkillsMatch() {
		return skillsMatch;
	}

	public void setSkillsMatch(Integer skillsMatch) {
		this.skillsMatch = skillsMatch;
	}

	public Boolean getNew() {
		return isNew;
	}

	public void setNew(Boolean isNew) {
		this.isNew = isNew;
	}
}
