package com.spring.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.spring.service.implementations.PropertiesService;

@Configuration
@EnableMongoRepositories({"com.spring.repository"})
public class MongoConfig extends AbstractMongoConfiguration {

	private final String CONNECTION_URL;
	private final String DB_NAME;

	@Autowired
	public MongoConfig(PropertiesService propertiesService) {
		Map<String, String> props = propertiesService.getMongoDBProperties();
		CONNECTION_URL = props.get(PropertiesService.MONGODB_HOST);
		DB_NAME = props.get(PropertiesService.MONGODB_DATABASE_NAME);
	}

	@Override
	public MongoClient mongoClient() {
		return new MongoClient(new MongoClientURI(CONNECTION_URL));
	}

	@Override
	protected String getDatabaseName() {
		return DB_NAME;
	}

	@Override
	protected Collection<String> getMappingBasePackages() {
		return Collections.singleton("com.spring.entity");
	}
}
