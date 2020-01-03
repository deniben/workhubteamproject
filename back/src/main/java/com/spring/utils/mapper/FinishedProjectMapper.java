package com.spring.utils.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import com.spring.dto.FinishedProjectDto;
import com.spring.dto.SkillDto;
import com.spring.entity.Company;
import com.spring.entity.Project;
import com.spring.entity.Skill;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinishedProjectMapper {

    private final CompanyMapper companyMapper;
    private ModelMapper modelMapper;
    private SkillMapper skillMapper;

    @Autowired
    public FinishedProjectMapper(CompanyMapper companyMapper, ModelMapper modelMapper, SkillMapper skillMapper) {
        this.companyMapper = companyMapper;
        this.modelMapper = modelMapper;
	    this.skillMapper = skillMapper;
    }

    public FinishedProjectDto toDto(Project project) {
        if (project == null) {
            return null;
        }
        TypeMap<Project, FinishedProjectDto> toDtoTypeMap = modelMapper.getTypeMap(Project.class, FinishedProjectDto.class);
        if (toDtoTypeMap == null) {
            toDtoTypeMap = modelMapper.createTypeMap(Project.class, FinishedProjectDto.class);
        }
        toDtoTypeMap.addMappings(x -> x.when(y -> y.getSource() != null)
                .using(y -> companyMapper.toDtoIgnoreSkills((Company) y.getSource()))
                .map(Project::getCompanyCreator, FinishedProjectDto::setCompanyCreator));

	    Converter<Set<Skill>, Set<SkillDto>> skillConverter =
			    ctx -> ctx.getSource().stream().map(skill -> skillMapper.toDto(skill)).collect(Collectors.toSet());

	    toDtoTypeMap.addMappings(mapper -> mapper.using(skillConverter).map(Project::getRequiredSkills, FinishedProjectDto::setSkillSet));


        FinishedProjectDto finishedProjectDto = toDtoTypeMap.map(project);
        finishedProjectDto.setPhoto(project.getPhotoUrl());
        return finishedProjectDto;
    }

}
