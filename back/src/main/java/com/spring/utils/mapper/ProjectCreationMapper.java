package com.spring.utils.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.spring.dto.ProjectCreationDto;
import com.spring.entity.Project;


@Component
public class ProjectCreationMapper implements DTOMapper<Project, ProjectCreationDto> {
    private ModelMapper modelMapper;

	@Autowired
    public ProjectCreationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public Project toEntity(ProjectCreationDto projectCreationDto) {
        if (projectCreationDto == null) {
            return null;
        }
		TypeMap<ProjectCreationDto, Project> toDtoTypeMap = modelMapper.getTypeMap(ProjectCreationDto.class, Project.class);
        if (toDtoTypeMap == null) {
            toDtoTypeMap = modelMapper.createTypeMap(ProjectCreationDto.class, Project.class);
        }

        return toDtoTypeMap.map(projectCreationDto);
    }

	@Override
	public ProjectCreationDto toDto(Project entity) {
		return modelMapper.map(entity, ProjectCreationDto.class);
	}

}
