package com.spring.dao;

import java.util.Optional;
import java.util.Set;

import com.spring.dto.PageableResponse;
import com.spring.entity.Skill;

public interface SkillsDao extends BaseDao<Skill> {
	Set<Skill> findByProject(Long id);

	public void deleteSkill(long id);

	Skill findSkillByName(String name);

	PageableResponse findAll(Integer page, Optional<String> name, String currentUser);
}
