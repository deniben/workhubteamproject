package com.spring.controller;

import com.spring.component.UserContext;
import com.spring.dao.ApplicationDao;
import com.spring.dto.EmployeeProjectDto;
import com.spring.dto.ProjectCreationDto;
import com.spring.dto.ProjectDto;
import com.spring.entity.Company;
import com.spring.entity.Project;
import com.spring.entity.ProjectRequest;
import com.spring.entity.ProjectType;
import com.spring.entity.Skill;
import com.spring.enums.CompanyType;
import com.spring.enums.ProjectStatus;
import com.spring.exception.OtherException;
import com.spring.exception.RestException;
import com.spring.service.CompanyService;
import com.spring.service.PhotosService;
import com.spring.service.ProjectRequestService;
import com.spring.service.ProjectService;
import com.spring.service.ProjectTypeService;
import com.spring.service.SkillsService;
import com.spring.utils.mapper.EmployeeMapper;
import com.spring.utils.mapper.ProjectCreationMapper;
import com.spring.utils.mapper.ProjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectTypeService projectTypeService;
    private final ProjectMapper projectMapper;
    private final ProjectCreationMapper projectCreationMapper;
	private ModelMapper modelMapper = new ModelMapper();
    private UserContext userContext;
    private final EmployeeMapper employeeMapper;
    private final PhotosService photosService;
    private final ApplicationDao applicationDao;
    private final SkillsService skillsService;
    private final ProjectRequestService projectRequestService;
    private final CompanyService companyService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    public ProjectController(ProjectService projectService, ProjectMapper projectMapper,
                             ProjectCreationMapper projectCreationMapper, UserContext userContext,
                             ProjectTypeService projectTypeService, EmployeeMapper employeeMapper,
                             PhotosService photosService, ApplicationDao applicationDao, SkillsService skillsService,
                             ProjectRequestService projectRequestService, CompanyService companyService) {

        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.projectCreationMapper = projectCreationMapper;
	    this.userContext = userContext;
        this.projectTypeService = projectTypeService;
	    this.employeeMapper = employeeMapper;
	    this.photosService = photosService;
	    this.applicationDao = applicationDao;
	    this.skillsService = skillsService;
	    this.projectRequestService = projectRequestService;
	    this.companyService = companyService;
    }

    @GetMapping()
    public List<ProjectDto> findAllProject() {
        LOGGER.trace("in getAllProject()");
        return projectService.findAll().stream().map(projectMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping()
    public ResponseEntity<String> createProject(@RequestBody(required = false) ProjectCreationDto projectDto) {

        LOGGER.debug("in createProject(), projectDto: {}", projectDto);

        Set<Skill> skillSet = projectDto.getSkills().stream().map(skillDto -> modelMapper.map(skillDto, Skill.class))
                .collect(Collectors.toSet());

        Project project = projectCreationMapper.toEntity(projectDto);
        project.setRequiredSkills(skillSet);
        ProjectType projectType = projectTypeService.findById(projectDto.getProjectTypeId());
        project.setProjectType(projectType);
        projectService.saveProject(project, userContext.getCurrentUser());
        return ResponseEntity.ok("Project has created");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProjectDto> findProjectById(@PathVariable Long id) {
        LOGGER.debug("in findProjectById(), id: {}", id);
        Project currentProject = projectService.findById(id);

	    Company currentCompany = companyService.findCompanyById(userContext.getCurrentUser().getProfile().getCompany().getId()) ;
        if (currentProject != null && currentProject.getStatus() != null && !currentProject.getStatus().equals(ProjectStatus.NEW)) {
            throw new RestException("Project with such id do not exists", HttpStatus.BAD_REQUEST.value());
        }
	    EmployeeProjectDto employeeProjectDto = employeeMapper.toDto(currentProject);
	    if (employeeProjectDto.getPhoto() != null) {
		    employeeProjectDto.setPhoto(photosService.getPhoto(currentProject.getPhotoUrl()));
	    }
	    ProjectRequest projectRequest = projectRequestService.getProjectRequest(currentCompany, currentProject);

	    if (projectRequest != null) {
		    employeeProjectDto.setNew(projectRequest.getAccepted());
	    } else {
		    employeeProjectDto.setNew(false);
	    }
	    if (currentCompany != null)
	    {
		    employeeProjectDto.setSkillsMatch(skillsService
				    .calculateMatchPercentage(currentProject, currentCompany.getProposedSkills()));
	    }
        return ResponseEntity.ok(employeeProjectDto);
    }

    @GetMapping("/company/{id}")
    @ResponseBody
    public ResponseEntity<List<ProjectDto>> findProjectByCompany(@PathVariable Long id, @RequestParam String companyType) {
        LOGGER.debug("in findProjectByCompany(), id: {}, companyType: {}", id, companyType);
        CompanyType companyEnumType;
        try {
            companyEnumType = CompanyType.valueOf(companyType);
        } catch (IllegalArgumentException exc) {
            throw new OtherException(String.format("Fail to convert '%s' param to CompanyType", companyType), exc);
        }
        List<Project> resultList = projectService.findByCompany(id, companyEnumType);
        List<ProjectDto> dtoList = resultList.stream().map(projectMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

	@PutMapping("/{projectId}")
	public ResponseEntity updateProject(@PathVariable Long projectId,
	                                    @RequestBody ProjectCreationDto projectCreationDto) {
        LOGGER.debug("in updateProject(), projectId:{}, projectCreationDto: {}", projectId, projectCreationDto);

		Project project = projectService.findProjectById(projectCreationDto.getId());

		project.setId(projectId);
		project.setBudget(projectCreationDto.getBudget());
		project.setDescription(projectCreationDto.getDescription());
		project.setName(projectCreationDto.getName());
		project.setPhotoUrl(projectCreationDto.getPhotoUrl());
		project.setExpiryDate(projectCreationDto.getExpiryDate());
		project.setRequiredSkills(projectCreationDto.getSkills().stream().map(skillDto -> modelMapper.map(skillDto, Skill.class)).collect(Collectors.toSet()));

		return ResponseEntity.ok(employeeMapper.toDto(projectService.updateProject(project)));
	}

}
