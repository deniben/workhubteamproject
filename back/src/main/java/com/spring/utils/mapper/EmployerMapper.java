package com.spring.utils.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import com.spring.dto.EmployeeProjectDto;
import com.spring.entity.Company;
import com.spring.entity.Project;


@Component
public class EmployerMapper implements DTOMapper<Project, EmployeeProjectDto> {

	private final CompanyMapper companyMapper;
	private ModelMapper modelMapper;

	public EmployerMapper(CompanyMapper companyMapper, ModelMapper modelMapper) {
		this.companyMapper = companyMapper;
		this.modelMapper = modelMapper;
	}


	@Override
	public Project toEntity(EmployeeProjectDto dto) {
		return modelMapper.map(dto, Project.class);
	}

	@Override
	public EmployeeProjectDto toDto(Project entity) {
		if (entity == null) {
			return null;
		}
		TypeMap<Project, EmployeeProjectDto> toDtoTypeMap = modelMapper.getTypeMap(Project.class, EmployeeProjectDto.class);
		if (toDtoTypeMap == null) {
			toDtoTypeMap = modelMapper.createTypeMap(Project.class, EmployeeProjectDto.class);
		}
		toDtoTypeMap.addMappings(x -> x.skip(EmployeeProjectDto::setNew))
				.addMappings(x -> x.skip(EmployeeProjectDto::setSkillSet))
				.addMappings(x -> x.skip(EmployeeProjectDto::setSkills))
				.addMappings(x -> x.using(y -> companyMapper.toDtoIgnoreSkills((Company) y.getSource()))
						.map(Project::getCompanyCreator, EmployeeProjectDto::setCompanyCreator));

		EmployeeProjectDto employerProjectDto = modelMapper.map(entity, EmployeeProjectDto.class);

		employerProjectDto.setSkillSet(entity.getRequiredSkills());
		employerProjectDto.setPhoto(entity.getPhotoUrl());
		employerProjectDto.setProjectStatus(entity.getStatus());
		employerProjectDto.setProjectType(entity.getProjectType());
		return employerProjectDto;
	}
}
