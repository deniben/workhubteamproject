package com.spring.utils.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.dto.ProjectRequestDto;
import com.spring.entity.Company;
import com.spring.entity.Project;
import com.spring.entity.ProjectRequest;


@Component
public class ProjectRequestMapper implements DTOMapper<ProjectRequest, ProjectRequestDto> {

	private ModelMapper modelMapper;
	TypeMap<ProjectRequest, ProjectRequestDto> toDtoTypeMap;

    @Autowired
    public ProjectRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
	    toDtoTypeMap = modelMapper.getTypeMap(ProjectRequest.class, ProjectRequestDto.class);

	    if (toDtoTypeMap == null) {
		    toDtoTypeMap = modelMapper.createTypeMap(ProjectRequest.class, ProjectRequestDto.class);
	    }
	    toDtoTypeMap.addMappings(x -> x.using(y -> ((Project) y.getSource()).getName())
			    .map(ProjectRequest::getProject, ProjectRequestDto::setProjectName))
			    .addMappings(x -> x.using(y -> ((Company) y.getSource()).getName())
					    .map(ProjectRequest::getEmployer, ProjectRequestDto::setEmployerName))
			    .addMappings(x -> x.using(y -> ((Project) y.getSource()).getId())
					    .map(ProjectRequest::getProject, ProjectRequestDto::setProjectId));
    }

	@Override
	public ProjectRequest toEntity(ProjectRequestDto dto) {
		return modelMapper.map(dto, ProjectRequest.class);
	}

	@Override
	public ProjectRequestDto toDto(ProjectRequest projectRequest) {
        if (projectRequest == null) {
            return null;
        }

        return toDtoTypeMap.map(projectRequest);
    }

}
