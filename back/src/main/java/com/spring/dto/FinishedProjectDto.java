package com.spring.dto;

import java.util.HashSet;
import java.util.Set;

import com.spring.entity.Skill;

public class FinishedProjectDto extends ProjectDto {

	public FinishedProjectDto() {
		super();
	}

	private Float employeeMark;

	private Set<SkillDto> skillSet = new HashSet<>();

	public Set<SkillDto> getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(Set<SkillDto> skillSet) {
		this.skillSet = skillSet;
	}

	public Float getEmployeeMark() {
		return employeeMark;
	}

	public void setEmployeeMark(Float employeeMark) {
		this.employeeMark = employeeMark;
	}
}
