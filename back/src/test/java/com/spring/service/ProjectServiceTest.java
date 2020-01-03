package com.spring.service;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.google.common.base.Verify;
import com.spring.component.UserContext;
import com.spring.dao.CompanyDao;
import com.spring.dao.ProfileDao;
import com.spring.dao.ProjectDao;
import com.spring.entity.Company;
import com.spring.entity.Profile;
import com.spring.entity.Project;
import com.spring.entity.ProjectType;
import com.spring.entity.Skill;
import com.spring.entity.User;
import com.spring.enums.CompanyType;
import com.spring.exception.RestException;
import com.spring.service.implementations.ProjectServiceImpl;

public class ProjectServiceTest {

	@Captor
	ArgumentCaptor<Project> argCaptor;

	@InjectMocks
	ProjectServiceImpl projectService;

	@Mock
	ProjectDao projectDao;

	@Mock
	ProfileDao profileDao;

	@Mock
	UserContext userContext;

	@Spy
	Project projectSpy;

	@Spy
	Project oldProjectSpy;


	@Spy
	User userSpy;

	@Spy
	Profile profileSpy;

	@Spy
	Company companySpy;

	@Spy
	ProjectType projectTypeSpy;

	@Spy
	Skill skillsSpy;

	@Spy
	Skill requiredSkillsSpy;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void updateProject(){
		userSpy.setUsername("companyUser");
		userSpy.setId(300L);
		userSpy.setProfile(profileSpy);
		Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

		profileSpy.setId(20L);
		profileSpy.setFirstName("test");
		profileSpy.setLastName("test");
		profileSpy.setNickname("test");
		profileSpy.setCompany(companySpy);

		oldProjectSpy.setId(123L);
		oldProjectSpy.setName("TestProject");
		oldProjectSpy.setDescription("Test project description");
		oldProjectSpy.setPhotoUrl("214238428675");
		oldProjectSpy.setBudget(11111.00);
		oldProjectSpy.setCompanyCreator(companySpy);
		requiredSkillsSpy.setId(23L);
		requiredSkillsSpy.setName("test Skill");

		oldProjectSpy.setRequiredSkills(Collections.singleton(requiredSkillsSpy));

		oldProjectSpy.setProjectType(projectTypeSpy);
		oldProjectSpy.setExpiryDate(LocalDate.of(1997,9,21));
		oldProjectSpy.setCompanyCreator(companySpy);


		companySpy.setId(25L);
		companySpy.setName("TestCompany");
		companySpy.setType(CompanyType.EMPLOYEE);
		companySpy.setDescription("Test company description");
		companySpy.setPhotoUrl("214238428675");
		companySpy.setOwner(profileSpy);
		skillsSpy.setId(2L);
		skillsSpy.setName("test skill");
		companySpy.setProposedSkills(Collections.singleton(skillsSpy));

		projectTypeSpy.setId(13L);
		projectTypeSpy.setName("Test ProjectType");

		projectSpy.setId(123L);
		projectSpy.setName("TestProject");
		projectSpy.setDescription("Test project description");
		projectSpy.setPhotoUrl("214238428675");
		projectSpy.setBudget(11111.00);
		requiredSkillsSpy.setId(23L);
		requiredSkillsSpy.setName("test Skill");

		projectSpy.setRequiredSkills(Collections.singleton(requiredSkillsSpy));

		projectSpy.setProjectType(projectTypeSpy);
		projectSpy.setExpiryDate(LocalDate.of(1997,9,21));
		projectSpy.setCompanyCreator(companySpy);

		argCaptor = ArgumentCaptor.forClass(Project.class);
		Mockito.when(projectDao.findById(projectSpy.getId())).thenReturn(Optional.of(oldProjectSpy));
		projectService.updateProject(projectSpy);

		verify(projectDao, times(1)).findById(projectSpy.getId());
		verify(projectDao, times(1)).merge(argCaptor.capture());
		verifyNoMoreInteractions(projectDao, profileDao);

		Project returnedProject = argCaptor.getValue();
		assertEquals("Names were not equal", projectSpy.getName(), returnedProject.getName());
		assertEquals("Descriptions were not equal", projectSpy.getDescription(), returnedProject.getDescription());
		assertEquals("Photos were not equal", projectSpy.getPhotoUrl(), returnedProject.getPhotoUrl());
		assertEquals("Budget were not equal", projectSpy.getBudget(), returnedProject.getBudget());
		assertEquals("Skills were not equal", projectSpy.getRequiredSkills(), returnedProject.getRequiredSkills());
	}

	@Test
	public void updateProjecNegativeWithNullProjectFromDb(){
		userSpy.setUsername("companyUser");
		userSpy.setId(300L);
		userSpy.setProfile(profileSpy);
		Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

		profileSpy.setId(20L);
		profileSpy.setFirstName("test");
		profileSpy.setLastName("test");
		profileSpy.setNickname("test");
		profileSpy.setCompany(companySpy);

		oldProjectSpy.setId(123L);
		oldProjectSpy.setName("TestProject");
		oldProjectSpy.setDescription("Test project description");
		oldProjectSpy.setPhotoUrl("214238428675");
		oldProjectSpy.setBudget(11111.00);
		oldProjectSpy.setCompanyCreator(companySpy);
		requiredSkillsSpy.setId(23L);
		requiredSkillsSpy.setName("test Skill");

		oldProjectSpy.setRequiredSkills(Collections.singleton(requiredSkillsSpy));

		oldProjectSpy.setProjectType(projectTypeSpy);
		oldProjectSpy.setExpiryDate(LocalDate.of(1997,9,21));
		oldProjectSpy.setCompanyCreator(companySpy);


		companySpy.setId(25L);
		companySpy.setName("TestCompany");
		companySpy.setType(CompanyType.EMPLOYEE);
		companySpy.setDescription("Test company description");
		companySpy.setPhotoUrl("214238428675");
		companySpy.setOwner(profileSpy);
		skillsSpy.setId(2L);
		skillsSpy.setName("test skill");
		companySpy.setProposedSkills(Collections.singleton(skillsSpy));

		projectTypeSpy.setId(13L);
		projectTypeSpy.setName("Test ProjectType");

		projectSpy.setId(123L);
		projectSpy.setName("TestProject");
		projectSpy.setDescription("Test project description");
		projectSpy.setPhotoUrl("214238428675");
		projectSpy.setBudget(11111.00);
		requiredSkillsSpy.setId(23L);
		requiredSkillsSpy.setName("test Skill");

		projectSpy.setRequiredSkills(Collections.singleton(requiredSkillsSpy));

		projectSpy.setProjectType(projectTypeSpy);
		projectSpy.setExpiryDate(LocalDate.of(1997,9,21));
		projectSpy.setCompanyCreator(companySpy);

		argCaptor = ArgumentCaptor.forClass(Project.class);
		Mockito.when(projectDao.findById(projectSpy.getId())).thenReturn(Optional.empty());

		try {
			projectService.updateProject(projectSpy);
			fail("Exception was not thrown");
		} catch (Exception exc) {
			assertTrue("Wrong exception type was found. Expected RestException, but found: " + exc.getClass(), exc instanceof RestException);
			assertEquals("Wrong exception message was found", exc.getMessage(),
					"Project was not found");
		}

		verify(projectDao, times(1)).findById(projectSpy.getId());
		verify(projectDao, never()).save(argCaptor.capture());
		verifyNoMoreInteractions(projectDao, profileDao);

	}


}
