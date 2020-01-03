package com.spring.utils.mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import com.spring.dto.EmployeeProjectDto;
import com.spring.entity.Company;
import com.spring.entity.Project;

@Component
public class EmployeeMapper implements DTOMapper<Project, EmployeeProjectDto> {
	private final ModelMapper modelMapper;
	private final CompanyMapper companyMapper;
	TypeMap<Project, EmployeeProjectDto> toDtoTypeMap;

	public EmployeeMapper(ModelMapper modelMapper, CompanyMapper companyMapper) {
		this.modelMapper = modelMapper;
		this.companyMapper = companyMapper;

		toDtoTypeMap = modelMapper.getTypeMap(Project.class, EmployeeProjectDto.class);
		if (toDtoTypeMap == null) {
			toDtoTypeMap = modelMapper.createTypeMap(Project.class, EmployeeProjectDto.class);
		}

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
		toDtoTypeMap.addMappings(x -> x.skip(EmployeeProjectDto::setNew))
				.addMappings(x -> x.skip(EmployeeProjectDto::setSkillSet))
				.addMappings(x -> x.skip(EmployeeProjectDto::setSkills))
				.addMappings(x -> x.using(y -> companyMapper.toDtoIgnoreSkills((Company) y.getSource()))
						.map(Project::getCompanyCreator, EmployeeProjectDto::setCompanyCreator));

		EmployeeProjectDto employeeProjectDto = modelMapper.map(entity, EmployeeProjectDto.class);
		employeeProjectDto.setSkillSet(entity.getRequiredSkills());
		employeeProjectDto.setPhoto(entity.getPhotoUrl());
		employeeProjectDto.setProjectStatus(entity.getStatus());
		employeeProjectDto.setProjectType(entity.getProjectType());
		return employeeProjectDto;
	}
}
