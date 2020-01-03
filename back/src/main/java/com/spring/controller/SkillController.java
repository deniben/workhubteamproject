package com.spring.controller;

import com.spring.dto.SkillDto;
import com.spring.entity.Skill;
import com.spring.service.SkillsService;
import com.spring.utils.mapper.SkillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/skills")
public class SkillController {

    private SkillsService skillsService;
    private final SkillMapper skillMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillController.class);

    @Autowired
    public SkillController(SkillsService skillsService, SkillMapper skillMapper) {
        this.skillsService = skillsService;
        this.skillMapper = skillMapper;
    }

    @GetMapping
    public List<SkillDto> findAllSkill() {
        LOGGER.trace("in findAllSkill()");
        return skillsService.findAll().stream().map(SkillDto::new).collect(Collectors.toList());
    }


    @PostMapping(consumes = "application/json")
    public ResponseEntity createSkill(@RequestBody(required = false) SkillDto skillDto) {
        LOGGER.debug("in createSkill(), skillDto: {}", skillDto);
        Skill skill = skillMapper.toEntity(skillDto);
        Skill createdSkill = skillsService.save(skill);
        return ResponseEntity.ok(skillMapper.toDto(createdSkill));
    }


    @GetMapping("/{id}")
    public ResponseEntity findSkillById(@PathVariable("id") Long id) {
        LOGGER.debug("in findSkillById(), id: {}", id);
        Skill returnedSkill = skillsService.findById(id);
        return ResponseEntity.ok(skillMapper.toDto(returnedSkill));
    }


}
