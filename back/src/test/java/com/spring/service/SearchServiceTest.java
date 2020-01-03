package com.spring.service;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.spring.configuration.ApplicationConfig;
import com.spring.dao.ProjectDao;
import com.spring.dao.UserDao;
import com.spring.dto.SearchDto;
import com.spring.dto.SearchProposalDto;
import com.spring.entity.User;

@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationConfig.class })
public class SearchServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    private static final String USERNAME = "michaeller.2012@gmail.com";

    @Autowired
    private ProposalSearchService searchService;

    @Autowired
    private UserDao userDao;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    private ProjectDao projectDao;

    private User user;

    private int projectsInSystem;

    @BeforeClass
    public void init() {
        this.user = userDao.findByUsername(USERNAME).get();
        this.projectsInSystem = projectDao.findAll().size();
    }

    @BeforeMethod
    public void startTrans() {
	    sessionFactory.getCurrentSession().getTransaction().begin();
    }


    @AfterMethod
    public void stopTrans() {
    	sessionFactory.getCurrentSession().getTransaction().commit();
    }

    @Test
    public void testSearch() {

        SearchDto searchDto = new SearchDto();

        searchDto.setName("");

        assertEquals(searchService.searchWithProposal(searchDto).getResult().size(), projectsInSystem);

        searchDto.setName("   ");
        assertEquals(searchService.searchWithProposal(searchDto).getResult().size(), projectsInSystem);

        searchDto.setName(">%#^$@:%#!");
        assertEquals(searchService.searchWithProposal(searchDto).getResult().size(), 0);

        searchDto.setName("d");
        assertEquals(searchService.searchWithProposal(searchDto).getResult().size(), projectsInSystem);

    }

    @Test
    public void testByTypes() {

        SearchDto searchDto = new SearchDto();
        searchDto.setTypeId(0l);

        assertTrue(searchService.searchWithProposal(searchDto).getResult().size() > 0);

        searchDto.setTypeId(-1l);
        assertTrue(searchService.searchWithProposal(searchDto).getResult().size() > 0);

    }

    @Test
	public void testBudget() {

    	SearchDto searchDto = new SearchDto();
    	searchDto.setMinBudget(-13.);
    	searchDto.setMinBudget(100031531.);

    	SearchProposalDto result = searchService.searchWithProposal(searchDto);

    	assertEquals(result.getResult().size(), 0);

    	searchDto.setMinBudget(1353153616136.);
    	result = searchService.searchWithProposal(searchDto);

	    assertEquals(result.getResult().size(), 0);
    }

}
