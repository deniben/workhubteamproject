package com.spring.dto;

import com.spring.entity.Skill;

public class SkillDto {
	private Long id;
	private String name;

	public SkillDto() {
	}

	public SkillDto(Skill skill) {
		this.id = skill.getId();
		this.name = skill.getName();
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

}
