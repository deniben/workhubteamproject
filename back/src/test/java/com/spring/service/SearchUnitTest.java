package com.spring.service;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.spring.dao.UserDao;
import com.spring.dao.implementations.BaseImpl;
import com.spring.dto.SearchDto;
import com.spring.entity.Company;
import com.spring.entity.Profile;
import com.spring.entity.Project;
import com.spring.entity.ProjectType;
import com.spring.entity.User;
import com.spring.enums.ProjectStatus;
import com.spring.enums.Role;
import com.spring.service.implementations.SearchServiceImpl;
import com.spring.utils.Page;
import com.spring.utils.SearchQueryUtils;

public class SearchUnitTest {

	private static SearchService searchService;
	private static User user;

	@Mock
	UserDao userDao;

	@Mock
	BaseImpl<Project> projectDao;

	@Mock
	com.spring.dao.implementations.BaseImpl<ProjectType> projectTypeDao;

	@Mock
	SkillsService skillsService;

	@Mock
	SessionFactory sessionFactory;

	@Mock
	SearchQueryUtils queryUtils;


	@BeforeClass
	public void init() {

		MockitoAnnotations.initMocks(this);

		searchService = new SearchServiceImpl(userDao, projectDao,
				projectTypeDao, skillsService,
				sessionFactory, queryUtils);

		user = new User();
		user.setActive(true);
		user.setId(1l);
		String username = "test.user@softserve.inc";
		user.setUsername(username);
		user.setRoles(Collections.singleton(Role.USER));
		Mockito.when(userDao.findByUsername(username)).thenReturn(Optional.of(user));

		Profile profile = new Profile();
		profile.setCompany(new Company());
		user.setProfile(profile);
	}

	@Test
	public void regularTest() {

		List<String> nonMeaningfulQueries = Arrays.asList("", " ", "1313", "@$#^G", "the", "an", "is", "!@#$%^&*", " f ", "...");
		final Integer TOTAL_NEW_PROJECTS_SIZE = nonMeaningfulQueries.size();
		List<Project> emptyProjects = nonMeaningfulQueries.stream().map(x -> new Project()).collect(Collectors.toList());

		for(String query : nonMeaningfulQueries) {

			SearchDto currentRequest = new SearchDto(query);

			Mockito.when(queryUtils.isNull(currentRequest)).thenReturn(false);
			Mockito.when(queryUtils.hibernateSearch(currentRequest)).thenReturn(emptyProjects);
			Mockito.when(queryUtils.byStatus(emptyProjects, ProjectStatus.NEW)).thenReturn(emptyProjects);

			Page<Project> result = searchService.search(currentRequest, user);
			assertEquals(result.getData().size(), (int) TOTAL_NEW_PROJECTS_SIZE);
		}

	}




}
