package com.spring.utils;

import com.spring.dto.SearchDto;
import com.spring.entity.Project;
import com.spring.enums.ProjectStatus;

import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.search.Query;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchQueryUtils {

	public final String NAME_FIELD = "name";

	public final String BUDGET_FILED = "budget";

	public final Integer LEFT_BOUND = 0;

	public final Integer RIGHT_BOUND = 1;

	private final SessionFactory sessionFactory;

	@Autowired
	public SearchQueryUtils(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Query byName(QueryBuilder queryBuilder, String name) {

		name = name.toLowerCase();

		if(name.matches(".*\\s.*")) {

			return queryBuilder.phrase()
					.withSlop(2)
					.onField(NAME_FIELD)
					.sentence(name)
					.createQuery();

		} else {

			return queryBuilder.keyword()
					.wildcard()
					.onField(NAME_FIELD)
					.matching("*" + name + "*")
					.createQuery();

		}

	}

	public Query byBudget(QueryBuilder queryBuilder, Double budget, Integer boundType) {

		if(boundType.equals(RIGHT_BOUND)) {

			return queryBuilder.range().onField(BUDGET_FILED)
					.from(0.).to(budget).createQuery();

		} else if(boundType.equals(LEFT_BOUND)) {

			return queryBuilder.range().onField(BUDGET_FILED)
					.from(budget)
					.to(Double.MAX_VALUE).createQuery();

		} else throw new RuntimeException("Invalid bound type");

	}

	public Query byBudget(QueryBuilder queryBuilder, Double min, Double max) {

		return queryBuilder.range()
				.onField(BUDGET_FILED)
				.from(min)
				.to(max).createQuery();

	}

	@Transactional
	public List<Project> hibernateSearch(SearchDto searchDto) {

		FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
		QueryBuilder queryBuilder = fullTextSession.getSearchFactory()
				.buildQueryBuilder()
				.forEntity(Project.class)
				.get();

		BooleanJunction generalQueryBuilder = queryBuilder.bool();
		String name = searchDto.getName();

		if (name != null) {
			generalQueryBuilder.must(byName(queryBuilder, name));
		}

		if (searchDto.getMinBudget() != null && searchDto.getMaxBudget() != null) {
			generalQueryBuilder.must(byBudget(queryBuilder, searchDto.getMinBudget(), searchDto.getMaxBudget()));
		} else if (searchDto.getMaxBudget() != null) {
			generalQueryBuilder.must(byBudget(queryBuilder, searchDto.getMaxBudget(), RIGHT_BOUND));
		} else if (searchDto.getMinBudget() != null) {
			generalQueryBuilder.must(byBudget(queryBuilder, searchDto.getMinBudget(), LEFT_BOUND));
		}

		List result = fullTextSession.createFullTextQuery(generalQueryBuilder.createQuery(), Project.class).getResultList();

		if (result.size() > 0) {

			List<Project> successResult = (List<Project>) result;

			if (searchDto.getTypeId() != null) {
				successResult = byType(searchDto.getTypeId(), successResult);
			}

			return successResult;

		}

		return result;
	}

	public List<Project> byType(Long typeId, List<Project> allProjects) {
		return allProjects.stream().filter(x -> x.getProjectType().getId().equals(typeId)).collect(Collectors.toList());
	}

	public Boolean isTypeSearch(SearchDto searchDto) {
		return searchDto.getTypeId() != null && searchDto.getMinBudget() == null
				&& searchDto.getMaxBudget() == null && (searchDto.getName() == null || searchDto.getName().isEmpty());
	}

	public Boolean isNull(SearchDto searchDto) {

		if(searchDto == null) {
			return true;
		}

		if(searchDto.getName() != null && searchDto.getName().length() <= 1) {
			searchDto.setName(null);
		}

		return searchDto.getTypeId() == null && searchDto.getMinBudget() == null
				&& searchDto.getMaxBudget() == null && searchDto.getName() == null;
	}

	public List<Query> fuzzyQuery(QueryBuilder queryBuilder, String name) {

		List<Query> result = new ArrayList<>();

		String [] names = name.split("\\s");

		if(names.length > 0) {
			Arrays.stream(names).forEach(x -> {
				if(!StopAnalyzer.ENGLISH_STOP_WORDS_SET.contains(x)) {
					result.add(createFuzzyQuery(queryBuilder, x));
				}
			});
		} else if(!StopAnalyzer.ENGLISH_STOP_WORDS_SET.contains(name)) {
			result.add(createFuzzyQuery(queryBuilder, name));
		}

		if(result.size() == 0) {
			result.add(byName(queryBuilder, name));
		}

		return result;
	}

	private Query createFuzzyQuery(QueryBuilder queryBuilder, String name) {
		return queryBuilder.keyword()
				.fuzzy()
				.onField("name")
				.matching(name)
				.createQuery();
	}

	public List<Project> byStatus(List<Project> projects, ProjectStatus projectStatus) {
		return projects.stream().filter(x -> x.getStatus() != null && x.getStatus().equals(projectStatus)).collect(Collectors.toList());
	}

}
