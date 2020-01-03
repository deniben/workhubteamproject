package com.spring.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ProjectRequestUtils {

	private static final Integer ATTEMPTS_LIMIT = 25;

	private static LoadingCache<Long, Set<Long>> longIntegerLoadingCache = CacheBuilder.newBuilder()
			.expireAfterAccess(1, TimeUnit.DAYS)
			.build(new CacheLoader<Long, Set<Long>>() {
				@Override
				public Set<Long> load(Long key) throws Exception {
					return new HashSet<>();
				}
			});

	public static boolean attempt(Long userId, Long companyId) {

		Set<Long> companies = longIntegerLoadingCache.getIfPresent(userId);

		if(companies == null) {
			companies = new HashSet<>();
		}

		if(!companies.contains(companyId)) {
			companies.add(companyId);
		}

		if(companies.size() <= ATTEMPTS_LIMIT) {
			longIntegerLoadingCache.put(userId, companies);
			return true;

		}

		return false;

	}

	public static void removeAttempt(Long userId, Long companyId) {

		Set<Long> companies = longIntegerLoadingCache.getIfPresent(userId);

		if(companies == null) {
			companies = new HashSet<>();
		}

		if(companies.contains(companyId)) {
			companies.remove(companyId);
		}
		longIntegerLoadingCache.put(userId, companies);
	}

}
