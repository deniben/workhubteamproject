package com.spring.service.implementations;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.service.SearchService;
import com.spring.dao.UserDao;
import com.spring.dao.implementations.BaseImpl;
import com.spring.dto.SearchDto;
import com.spring.entity.Company;
import com.spring.entity.Profile;
import com.spring.entity.Project;
import com.spring.entity.ProjectType;
import com.spring.entity.Skill;
import com.spring.entity.User;
import com.spring.enums.ProjectStatus;
import com.spring.service.SkillsService;
import com.spring.utils.Page;
import com.spring.utils.SearchQueryUtils;

@Service
@Transactional
public class SearchServiceImpl implements SearchService {

    private final UserDao userDao;
    private final BaseImpl<Project> projectDao;
    private final BaseImpl<ProjectType> projectTypeDao;
    private final SkillsService skillsService;
    private final SessionFactory sessionFactory;
    private final SearchQueryUtils queryUtils;

    private static final Integer PAGE_SIZE = 9;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public SearchServiceImpl(UserDao userDao, BaseImpl<Project> projectDao,
                             BaseImpl<ProjectType> projectTypeDao, SkillsService skillsService,
                             SessionFactory sessionFactory, SearchQueryUtils queryUtils) {
        this.userDao = userDao;
        this.projectDao = projectDao;
        this.projectTypeDao = projectTypeDao;
        this.skillsService = skillsService;
        this.sessionFactory = sessionFactory;
        this.queryUtils = queryUtils;
    }

    public Page<Project> search(SearchDto searchDto, User user) {

        List<Project> projects = null;

        if (queryUtils.isNull(searchDto)) {
            projects = projectDao.findAll();
        } else if (queryUtils.isTypeSearch(searchDto)) {
            projects = queryUtils.byType(searchDto.getTypeId(), projectDao.findAll());
        } else {
            projects = queryUtils.hibernateSearch(searchDto);
        }

        if (projects.size() > 0) {
            projects = queryUtils.byStatus(projects, ProjectStatus.NEW);
        }

        Profile profile = userDao.findByUsername(user.getUsername()).get().getProfile();
        Company company = profile.getCompany();

        Set<Skill> proposedSkills = company.getProposedSkills();

        if (proposedSkills.size() > 0) {
            projects.sort(Comparator.comparingInt(x -> skillsService.calculateMatch(x, proposedSkills)));
        }

        return new Page<>(projects, PAGE_SIZE);

    }


}