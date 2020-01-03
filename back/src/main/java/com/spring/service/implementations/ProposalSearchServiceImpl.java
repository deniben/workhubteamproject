package com.spring.service.implementations;

import com.spring.service.ProposalSearchService;
import com.spring.service.RecommendationsService;
import com.spring.service.SearchService;
import com.spring.component.UserContext;
import com.spring.dao.ProjectTypeDao;
import com.spring.dto.SearchDto;
import com.spring.dto.SearchProposalDto;
import com.spring.entity.Project;
import com.spring.entity.User;
import com.spring.utils.Page;
import com.spring.utils.SearchQueryUtils;
import com.spring.utils.mapper.ProjectMapper;

import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProposalSearchServiceImpl implements ProposalSearchService {

	private final SessionFactory sessionFactory;
	private final ProjectTypeDao projectTypeDao;
	private final ProjectMapper projectMapper;
	private final UserContext userContext;
	private final RecommendationsService recommendationsService;
	private final SearchService searchService;
	private final SearchQueryUtils queryUtils;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    public ProposalSearchServiceImpl(SessionFactory sessionFactory, ProjectTypeDao projectTypeDao,
									 ProjectMapper projectMapper, UserContext userContext,
									 RecommendationsService recommendationsService, SearchService searchService,
									 SearchQueryUtils queryUtils) {
		this.sessionFactory = sessionFactory;
		this.projectTypeDao = projectTypeDao;
		this.projectMapper = projectMapper;
		this.userContext = userContext;
		this.recommendationsService = recommendationsService;
		this.searchService = searchService;
		this.queryUtils = queryUtils;
	}

    @Override
	public SearchProposalDto searchWithProposal(SearchDto searchDto) {

		User user = userContext.getCurrentUser();

		logger.info("Searching for project with name " + searchDto.getName() +
				" type (id) " + searchDto.getTypeId() + " budget in range" + searchDto.getMinBudget() + " - " + searchDto.getMaxBudget());

		if(searchDto != null && searchDto.getTypeId() != null) {
			recommendationsService.updateValuation(projectTypeDao.findById(searchDto.getTypeId()).get(), RecommendationsService.SEARCH);
		}

		SearchProposalDto response = new SearchProposalDto();

		Page<Project> page = searchService.search(searchDto, user);
		List<Project> projects = page.getItemsOnPage(searchDto.getPage());

		logger.info("Found projects for query : " + projects.size());

		response.setResult(projects.stream().map(x -> projectMapper.toDto(x)).collect(Collectors.toList()));
		response.setPagesCount(page.countPages());

		if(searchDto != null && searchDto.getName() != null) {

			String correctionResult = findProposal(searchDto, projects.size());

			SearchDto proposalDto = new SearchDto(searchDto);
			proposalDto.setName(correctionResult);

			if(!correctionResult.equals(searchDto.getName())) {
				response.setProposalName(correctionResult);
				logger.info("Input correction prediction : " + correctionResult);
			}

		}

		return response;

	}

	private String findProposal(SearchDto searchDto, Integer resultCount) {

		User user = userContext.getCurrentUser();

		FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
		QueryBuilder queryBuilder = fullTextSession.getSearchFactory()
				.buildQueryBuilder()
				.forEntity(Project.class)
				.get();

		BooleanJunction booleanJunction = queryBuilder.bool();

		queryUtils.fuzzyQuery(queryBuilder, searchDto.getName()).stream()
				.forEach(booleanJunction::must);

		if(searchDto.getMinBudget() != null && searchDto.getMaxBudget() != null) {
			booleanJunction.must(queryUtils.byBudget(queryBuilder, searchDto.getMinBudget(), searchDto.getMaxBudget()));
		} else if(searchDto.getMinBudget() != null) {
			booleanJunction.must(queryUtils.byBudget(queryBuilder, searchDto.getMinBudget(), queryUtils.LEFT_BOUND));
		} else if(searchDto.getMaxBudget() != null) {
			booleanJunction.must(queryUtils.byBudget(queryBuilder, searchDto.getMaxBudget(), queryUtils.RIGHT_BOUND));
		}

		List resutl = fullTextSession.createFullTextQuery(booleanJunction.createQuery()).getResultList();

		if(resutl.size() > 0) {

			List<Project> projectsFound = (List<Project>) resutl;

			if(searchDto.getTypeId() != null && searchDto.getTypeId() >= 0) {

				projectsFound = queryUtils.byType(searchDto.getTypeId(), projectsFound);

				if(projectsFound.size() == 0) {
					return searchDto.getName();
				}

			}

			SearchDto proposalSearchDto = new SearchDto(searchDto);

			Map<String, Integer> proposalResult = new HashMap<>();

			for(Project project : projectsFound) {

				proposalSearchDto.setName(project.getName());

				Integer sizeOfResult = searchService.search(proposalSearchDto, user).getData().size();

				if(sizeOfResult > resultCount) {
					proposalResult.put(project.getName(), sizeOfResult);
				}

			}

			if(proposalResult.size() > 0) {
				return proposalResult.entrySet().stream()
						.max(Comparator.comparingInt(Map.Entry::getValue))
						.get()
						.getKey();
			}

		}

		return searchDto.getName();

	}

}
