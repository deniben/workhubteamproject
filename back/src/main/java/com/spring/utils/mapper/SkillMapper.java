package com.spring.utils.mapper;

import com.spring.dto.SkillDto;
import com.spring.entity.Skill;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper implements DTOMapper<Skill, SkillDto> {

    private ModelMapper modelMapper;

    @Autowired
    public SkillMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Skill toEntity(SkillDto skillDto) {
        return skillDto == null ? null : modelMapper.map(skillDto, Skill.class);
    }

    @Override
    public SkillDto toDto(Skill skill) {
        return skill == null ? null : modelMapper.map(skill, SkillDto.class);
    }
}
