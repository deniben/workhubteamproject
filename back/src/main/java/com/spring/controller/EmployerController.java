package com.spring.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.spring.dto.EmployeeProjectDto;
import com.spring.component.UserContext;
import com.spring.dto.*;
import com.spring.entity.Project;
import com.spring.enums.ProjectStatus;
import com.spring.exception.RestException;
import com.spring.service.*;
import com.spring.utils.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.component.UserContext;
import com.spring.dto.CompanyDto;
import com.spring.dto.PageableResponse;
import com.spring.dto.ProfileDto;
import com.spring.dto.ProjectDto;
import com.spring.enums.ProjectStatus;
import com.spring.service.CompanyService;
import com.spring.service.PhotosService;
import com.spring.service.ProfileService;
import com.spring.service.ProjectService;
import com.spring.service.SkillsService;
import com.spring.utils.mapper.CompanyMapper;
import com.spring.utils.mapper.EmployerMapper;
import com.spring.utils.mapper.ProfileMapper;
import com.spring.utils.mapper.ProjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employer")
public class EmployerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployerController.class);
	private final ProjectService projectService;
	private final CompanyService companyService;
	private final UserContext userContext;
	private final ProfileService profileService;
	private final ProjectMapper projectMapper;
	private final ProfileMapper profileMapper;
	private final CompanyMapper companyMapper;
	private final SkillsService skillsService;
	private final EmployerMapper employerMapper;
	private final PhotosService photosService;




	@Autowired
	public EmployerController(ProjectService projectService, CompanyService companyService,
	                          UserContext userContext, ProfileService profileService,
	                          ProjectMapper projectMapper, ProfileMapper profileMapper,
	                          CompanyMapper companyMapper, SkillsService skillsService,
	                          EmployerMapper employerMapper, PhotosService photosService) {
		this.projectService = projectService;
		this.companyService = companyService;
		this.userContext = userContext;
		this.profileService = profileService;
		this.projectMapper = projectMapper;
		this.profileMapper = profileMapper;
		this.companyMapper = companyMapper;
		this.skillsService = skillsService;
		this.employerMapper = employerMapper;
		this.photosService = photosService;
	}

    @GetMapping(value = "/find-by-status/{status}/page/{page}")
    public ResponseEntity<PageableResponse> findByStatus(@PathVariable("status") ProjectStatus status
            , @PathVariable("page") Optional<Integer> page) {
	    LOGGER.debug("in findByStatus(), status: {}, page: {}", status, page);
        Integer numberOfPages = projectService.numberOfProjectsPages(status);
        if (!page.isPresent()) {
            throw new RestException("Number of page not exist");
        }
        if (page.get() > numberOfPages) {
            throw new RestException("Number of page not exist");
        }
        if (page.get() < 0) {
            throw new RestException("Number of page not exist");
        }

        List<ProjectDto> projectDtos = projectService.findByStatus(status, page.get())
                .stream()
                .map(projectMapper::toDto)
                .peek(x -> x.setNumberOfInvite(projectService.numberOfInvite(x.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PageableResponse(numberOfPages, projectDtos));
    }

	@GetMapping(value = "/project/{id}")
	public ResponseEntity<ProjectDto> findProjectById(@PathVariable("id") Long id) {
		LOGGER.debug("in findProjectById(), id: {}", id);

		Project project = projectService.findProjectById(id);
		EmployeeProjectDto employeeProjectDto = employerMapper.toDto(project);
		if (project.getPhotoUrl() != null) {
			employeeProjectDto.setPhoto(photosService.getPhoto(project.getPhotoUrl()));
		}
		return ResponseEntity.ok(employeeProjectDto);
	}


	@GetMapping(value = "/companies-for-my-project/{id}/page/{page}")
	public ResponseEntity<PageableResponse> findAllCompanysForMyProject(@PathVariable("id") Long id, @PathVariable("page") Integer page) {
		LOGGER.debug("in findAllCompanysForMyProject(), id: {}, page: {}", id, page);
		List<CompanyDto> companyDtos = companyService.findAllCompaniesForMyProject(id, page)
				.stream()
				.map(companyMapper::toDtoIgnoreSkills)
				.collect(Collectors.toList());
		return ResponseEntity.ok(new PageableResponse(companyService.numberOfAllCompanyToMyProjectPages(id),companyDtos));
	}


	@GetMapping(value = "/profiles/page/{page}")
	public ResponseEntity<PageableResponse> findProfilesByCompany(@PathVariable("page") Integer page) {
		LOGGER.debug("in findProfilesByCompany(), page: {}", page);
		List<ProfileDto> profileDtos =  profileService.findApplicationsByCompany(userContext.getCurrentUser()
				.getProfile().getCompany(), true,page,10)
				.stream()
				.map(profileMapper::toDtoOutput)
				.collect(Collectors.toList());

        return ResponseEntity.ok(new PageableResponse(profileService.getNumberOfPages(userContext.getCurrentUser().getProfile().getCompany().getId(), true,10), profileDtos));
    }

    @PutMapping(value = "/accept-company/{employeeId}/to-project/{projectId}")
    public void acceptCompanyToProject(@PathVariable("employeeId") Long employeeId
            , @PathVariable("projectId") Long projectId) {
	    LOGGER.debug("in acceptCompanyToProject(), employeeId: {}, projectId: {}", employeeId, projectId);

	    projectService.acceptCompanyToProject(employeeId, projectId);
    }

	@DeleteMapping(value = "/reject-company/{employeeId}/from-project/{projectId}")
	public ResponseEntity<String> rejectCompany(@PathVariable("employeeId") Long employeeId, @PathVariable("projectId") Long projectId) {
		LOGGER.debug("in rejectCompany(), employeeId: {}, projectId: {}", employeeId, projectId);

		projectService.rejectCompany(employeeId, projectId);

        return ResponseEntity.ok("Company rejected");
    }

	@PutMapping(value = "/finish-project/{projectId}")
	public ResponseEntity<String> finishProject(@PathVariable("projectId") Long projectId) {
		LOGGER.debug("in rejectCompany(), projectId: {}", projectId);

		projectService.finishProject(projectId);

        return ResponseEntity.ok("Project finished");
    }

	@PutMapping(value = "/complete-project/projectId/{id}")
	public ResponseEntity<String> completeProject(@PathVariable("id") Long id) {
		LOGGER.debug("in completeProject(), id: {}", id);
		projectService.completeProject(id);

        return ResponseEntity.ok("Project completed");
    }


	@GetMapping(value = "/get-percent-of-matched-skills/projectId/{projectId}/companyId/{companyId}")
	public ResponseEntity<Integer> getPercentOfMatchedSkills(@PathVariable("projectId") Long projectId,@PathVariable("companyId") Long companyId){
		LOGGER.debug("in getPercentOfMatchedSkills(), projectId: {}, companyId: {}", projectId, companyId);

        return  ResponseEntity.ok(skillsService.calculateMatchPercentage(projectService.findProjectById(projectId),companyService.findCompanyById(companyId).getProposedSkills()));
    }


}

