package com.spring.utils.mapper;

import com.spring.dto.ProjectDto;
import com.spring.entity.Company;
import com.spring.entity.Project;
import com.spring.service.PhotosService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProjectMapper implements DTOMapper<Project, ProjectDto> {

    private final PhotosService photosService;
	private ModelMapper modelMapper;
    TypeMap<Project, ProjectDto> toProjectDtoTypeMap;

    @Autowired
    public ProjectMapper(PhotosService photosService, CompanyMapper companyMapper, ModelMapper modelMapper) {
        this.photosService = photosService;
        this.modelMapper = modelMapper;


        toProjectDtoTypeMap = modelMapper.getTypeMap(Project.class, ProjectDto.class);
        if (toProjectDtoTypeMap == null) {
            toProjectDtoTypeMap = modelMapper.createTypeMap(Project.class, ProjectDto.class);
        }
        toProjectDtoTypeMap.addMappings(x ->
                x.when(y -> y.getSource() != null).using(y -> companyMapper.toDtoIgnoreSkills((Company) y.getSource()))
                        .map(Project::getCompanyCreator, ProjectDto::setCompanyCreator))
                .addMappings(x -> x.skip(ProjectDto::setSkills));
    }

    public Project toEntity(ProjectDto projectDto) {
        return modelMapper.map(projectDto, Project.class);
    }

    public ProjectDto toDto(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDto projectDto = toProjectDtoTypeMap.map(project);
        String photoUrl = project.getPhotoUrl();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            projectDto.setPhoto(photosService.getPhoto(project.getPhotoUrl()));
        }
        return projectDto;
    }

    public ProjectDto toDtoIgnoreSkills(Project project) {

        TypeMap<Project, ProjectDto> toDto = modelMapper.getTypeMap(Project.class, ProjectDto.class);
        if (toDto == null) {
            toDto = modelMapper.createTypeMap(Project.class, ProjectDto.class);
        }

        toDto.addMappings(x -> x.skip(ProjectDto::setSkills));

        ProjectDto projectDto = toDto.map(project);
        projectDto.setPhoto(photosService.getPhoto(project.getPhotoUrl()));
        return projectDto;
    }

}
