package com.spring.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.spring.dto.PageableResponse;
import com.spring.dto.SkillDto;
import org.springframework.http.ResponseEntity;

import com.spring.entity.Project;
import com.spring.entity.Skill;

public interface SkillsService {
	List<Skill> findAll();

	 Skill save(Skill skill) ;

	 ResponseEntity<Object> delete(long id) ;

	 Skill findById(long id) ;

	 Integer calculateMatch(Project project, Set<Skill> skills);

	 Integer calculateMatchPercentage(Project project, Set<Skill> skills);

	 Skill updateSkill(Skill skill);

	 ResponseEntity<Object> deleteSkill(long id);

	PageableResponse getAll(Integer page, Optional<String> name);

}
