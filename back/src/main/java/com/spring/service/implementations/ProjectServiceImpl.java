package com.spring.service.implementations;

import com.spring.component.UserContext;
import com.spring.dao.ProfileDao;
import com.spring.dao.ProjectDao;
import com.spring.dao.ProjectTypeDao;
import com.spring.entity.*;
import com.spring.enums.CompanyType;
import com.spring.enums.ProjectStatus;
import com.spring.exception.RestException;
import com.spring.service.CompanyService;
import com.spring.service.PhotosService;
import com.spring.service.ProjectService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());


    private final ValidationService validationService;
    private final ProfileDao profileDao;
    private final CompanyService companyService;
    private final ProjectDao projectDao;
    private final ProjectTypeDao projectTypeDao;
    private final UserContext userContext;
    private final PhotosService photosService;

    @Autowired
    public ProjectServiceImpl(ValidationService validationService, ProfileDao profileDao, CompanyService companyService,
                              ProjectDao projectDao, ProjectTypeDao projectTypeDao, UserContext userContext,
                              PhotosService photosService) {
        this.validationService = validationService;
        this.profileDao = profileDao;
        this.companyService = companyService;
        this.projectDao = projectDao;
        this.projectTypeDao = projectTypeDao;
        this.userContext = userContext;
        this.photosService = photosService;
    }

    @Override
    public List<Project> findAll() {
        LOGGER.trace("in findAll()");
        return projectDao.findAll();
    }

    @Override
    public Project save(Project project) {

        LOGGER.debug("in save()");
        project.setStatus(ProjectStatus.NEW);
        projectDao.save(project);
        return project;
    }

    @Override
    public Project delete(long id) {

        LOGGER.debug("in delete()");
        Optional<Project> project = projectDao.findById(id);
        if (project.isPresent()) {
            projectDao.delete(id);
            return project.get();
        }
        throw new RestException("Project was not found", 404);
    }

    @Override
    public Project findById(long projectId) {

        LOGGER.debug("in findById()");
        Optional<Project> project = projectDao.findById(projectId);
        if (project.isPresent()) {
            project.get().getRequiredSkills().size();
            return project.get();
        }
        throw new RestException("Project was not found", 404);
    }

    @Override
    public Project saveProject(Project project, User user) {

        LOGGER.debug("in saveProject()");
        validationService.projectValidation(project);

        Profile profile = user.getProfile();
        Company company = profile.getCompany();
        Hibernate.initialize(project.getRequiredSkills());
        project.setCompanyCreator(company);
        project.setStatus(ProjectStatus.NEW);
        projectDao.save(project);
        return project;
    }

    @Override
    public Project updateProject(Project projectFromUi) {
        LOGGER.debug("in updateProject()");
        User user = userContext.getCurrentUser();
        //get user profile who wants edit project
        Profile profile = user.getProfile();
        // get project from DB
        Project projectFromDb = findById(projectFromUi.getId());
        //check if project exist in DB, can be null
        if (projectFromDb == null) {
            throw new RestException(String.format("Project was not found. Project id: %s", projectFromUi.getId()), 404);
        }
        if (profile == null) {
            throw new RestException(String.format("Enable to find user profile for user with '%s' id", user.getId()), 404);
        }
        //check if profile member of company can update company
        if (!(profile.getCompany().getId().equals(projectFromDb.getCompanyCreator().getId()))) {
            throw new RestException(String.format("This profile do not have permission to update this project. Your company id: %s, Company id of project: %s",
                    profile.getCompany().getId(), projectFromDb.getCompanyCreator().getId()), 400);
        }
        projectFromDb.setBudget(projectFromUi.getBudget());
        projectFromDb.setDescription(projectFromUi.getDescription());
        projectFromDb.setName(projectFromUi.getName());
        projectFromDb.setExpiryDate(projectFromUi.getExpiryDate());
        projectFromDb.setPhotoUrl(projectFromUi.getPhotoUrl());
        projectFromDb.setProjectType(projectFromUi.getProjectType());
        projectFromDb.setRequiredSkills(projectFromUi.getRequiredSkills());

        projectDao.merge(projectFromDb);
        LOGGER.debug("project updated successfully!");
        return projectFromDb;
    }


    @Override
    public List<Project> findByStatus(ProjectStatus status, Integer page) {

        LOGGER.debug("in findByStatus()");
//        photosService.getPhoto(project.getPhotoUrl())

        return projectDao.findByStatus(userContext.getCurrentUser().getProfile().getCompany(), page.longValue(), status);
    }

    @Override
    public int numberOfInvite(Long projectId) {
        LOGGER.debug("in numberOfInvite()");
        long start = System.currentTimeMillis();
        Integer numberOfInvite = projectDao.numberOfInvite(projectId);
        LOGGER.info("numberOfInvite passed in [{}]ms", System.currentTimeMillis() - start);
        return numberOfInvite;
    }

    @Override
    public Integer numberOfProjectsPages(ProjectStatus projectStatus) {
        Integer dataSize = projectDao.numberOfMyProjects(projectStatus);
        float pages = (float) dataSize / 10;
        if (pages * 10000 > ((int) pages) * 10000) {
            pages++;
        }
        int i = (int) pages--;

        return i;
    }

    @Override
    public Project findProjectById(Long projectId) {

        LOGGER.debug("in findProjectById()");
        Optional<Project> project = projectDao.findById(projectId);
        if (project.isPresent()) {
            project.get().getRequiredSkills().size();
            return project.get();
        }
        throw new RestException("Project was not found", 404);

    }

    @Override
    public List<Project> findByCompany(Long companyId, CompanyType companyType) {

        LOGGER.debug("in findByCompany()");
        return projectDao.findByCompany(companyId, companyType);
    }

    @Override
    public void acceptCompanyToProject(Long employeeId, Long projectId) {

        LOGGER.debug("in acceptCompanyToProject()");
        existProject(projectId);
        isMember(projectId);
        isEmployeeRequest(employeeId, projectId);
        projectDao.acceptCompanyToProject(employeeId, projectId);
    }

    @Override
    public void rejectCompany(Long employeeId, Long projectId) {

        LOGGER.debug("in rejectCompany({}, {})", employeeId, projectId);
        existProject(projectId);
        isMember(projectId);
        isEmployeeRequest(employeeId, projectId);
        projectDao.rejectCompany(employeeId, projectId);
    }

    @Override
    public void completeProject(Long projectId) {
        existProject(projectId);
        isMember(projectId);
        projectDao.completeProject(projectId);
    }

    @Override
    public void finishProject(Long projectId) {
        LOGGER.debug("in finishProject()");
        existProject(projectId);
        isMember(projectId);
        projectDao.finishProject(projectId);
    }


    @Override
    public List<Project> findByCompany(long id) {

        LOGGER.debug("in findByCompany()");
        List<Project> projects = new ArrayList<>();
        projectDao.findByCompany(companyService.findCompanyById(id))
                .forEach(project -> {
                    project.getRequiredSkills().size();
                    projects.add(project);
                });
        return projects;
    }

    @Override
    public void updateStatus(Long id, String status) {

        LOGGER.debug("in updateStatus()");
        Optional<Project> project = projectDao.findById(id);
        ProjectStatus status1 = ProjectStatus.valueOf(status);

        if (project.isPresent()) {
            project.get().setStatus(status1);
            projectDao.save(project.get());
        }
        throw new RestException("Project was not found", 404);

    }

    @Override
    public void updateProjectType(Long id, String projectType) {

        LOGGER.debug("in updateProjectType()");
        Optional<Project> project = projectDao.findById(id);
        Optional<ProjectType> projectType1 = projectTypeDao.findProjectTypeByName(projectType);

        if (project.isPresent()) {
            if (projectType1.isPresent()) {
                project.get().setProjectType(projectType1.get());
                projectDao.save(project.get());
            } else {
                throw new RestException("ProjectType was not found", 404);
            }
        }else {
            throw new RestException("Project was not found", 404);
        }

    }

    @Override
    public List<String> getAllStatus() {

        LOGGER.trace("in getAllStatus()");
        return Arrays.stream(ProjectStatus.values()).map(ProjectStatus::name).collect(Collectors.toList());
    }

    @Override
    public List<ProjectType> getAllProjectType() {

        LOGGER.trace("in getAllProjectType()");
        return projectTypeDao.findAll();
    }

    @Override
    public List<Integer> getSkillsId(Long projectId) {
        return projectDao.getSkillsId(projectId);
    }

    private void isMember(Long projectId) {

        LOGGER.debug("in isMember()");
        User user = userContext.getCurrentUser();
        Optional<Project> project = projectDao.findById(projectId);
        if (project.isPresent()) {
            if (!project.get().getCompanyCreator().isMember(user)) {
                throw new RestException("You are not member the company. You can not accept the project", HttpStatus.BAD_REQUEST.value());
            }
        } else {
            throw new RestException("Project was not found", 404);
        }
    }

    private void existProject(Long projectId) {
        if (projectDao.existProject(projectId) == 0) {
            throw new RestException("The project not exist", HttpStatus.BAD_REQUEST.value());
        }
    }

    private void isEmployeeRequest(Long employeeId, Long projectId) {
        if (projectDao.checkingEmployeeRequest(projectId, employeeId) == 0) {
            throw new RestException("In  employee  not exist a req to project  ", HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    public List<Project> findProjectsByCompany(Long id, Integer page, Optional<String> name) {

        LOGGER.debug("in findProjectsByCompany()");
        List<Project> projects = projectDao.findAllProjectByCompany(id, page, name);
        projects.forEach(x -> (x).getRequiredSkills().size());
        initCompany((projects.get(0)).getCompanyCreator());
        return projects;
    }

    private void initCompany(Company company) {
        company.getProposedSkills().size();
        company.getWorkers().size();
    }


    @Override
    public Integer countProjectsByCompany(Long id) {

        LOGGER.debug("in countProjectsByCompany()");
        Integer dataSize = projectDao.countProjectsByCompany(id);

        float pages = (float) dataSize / 5;

        if (pages * 10000 > ((int) pages) * 10000) {
            pages++;
        }

        return (int) pages--;

    }


}