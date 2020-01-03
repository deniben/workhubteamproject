package com.spring.utils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.spring.enums.CacheType;
import com.spring.exception.RestException;

public class TokenUtils {

	private static LoadingCache<String, String> activationTokens = CacheBuilder.newBuilder()
			.expireAfterWrite(4, TimeUnit.HOURS)
			.build(CacheLoader.from(x -> ""));

	private static LoadingCache<String, String> recoveryTokens = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.HOURS)
			.build(CacheLoader.from(x -> ""));

	public static String generateToken(String username, CacheType cacheType) {

		String token = UUID.randomUUID().toString();

		getCacheByType(cacheType).put(username, token);
		return token;

	}

	public static String getToken(String username, CacheType cacheType) {
		return getCacheByType(cacheType).getIfPresent(username);
	}

	private static LoadingCache<String, String> getCacheByType(CacheType cacheType) {

		if(cacheType.equals(CacheType.ACTIVATION)) {

			return activationTokens;

		} else if (cacheType.equals(CacheType.RECOVERY)) {

			return recoveryTokens;

		}

		throw new IndexOutOfBoundsException("Invalid cache type");

	}

	public static void removeToken(String username, CacheType cacheType) {
		getCacheByType(cacheType).invalidate(username);
	}

}
