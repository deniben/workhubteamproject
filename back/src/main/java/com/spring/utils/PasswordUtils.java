package com.spring.utils;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class PasswordUtils {

	private static LoadingCache<String, String> passwords = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.HOURS)
			.build(new CacheLoader<String, String>() {
				@Override
				public String load(String key) throws Exception {
					return null;
				}
			});

	public static void cache(String user, String password) {
		passwords.put(user, password);
	}

	public static String get(String user) {
		return passwords.getUnchecked(user);
	}

	public static void remove(String user) {
		passwords.invalidate(user);
	}

}
