package com.spring.service.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class PropertiesService {

    public static final String MAIL_USERNAME = "username";
    public static final String MAIL_PASSWORD = "password";
    public static final String MAIL_HOST = "host";
    public static final String MAIL_PORT = "port";

    public static final String ACTIVATION_MESSAGE = "activationMessage";
    public static final String PASSWORD_CHANGED_MESSAGE = "passwordChangedMessage";
    public static final String PASSWORD_RECOVERY_MESSAGE = "recoverPasswordMessage";
    public static final String USER_CREATED_BY_ADMIN_MAIL = "adminCreatedUserMessage";
    public static final String CONFIRM_PASSWORD_CHANGE_MESSAGE = "confirmPasswordChangeMessage";

    public static final String OAUTH2_CLIENT_ID = "clientId";
    public static final String OAUTH2_CLIENT_SECRET = "clientSecret";
    public static final String GOOGLE_CLIENT = "google";
    public static final String GITHUB_CLIENT = "github";
    public static final String GITHUB_USER_INFO_EMAIL_SEGMENT = "/emails";
    public static final String GITHUB_PRIMARY_FIELD = "primary";
    public static final String GITHUB_EMAIL_FIELD = "email";

    public static final String MONGODB_HOST = "mongo_host";
    public static final String MONGODB_DATABASE_NAME = "mongo_db_name";

    private static final String MAIL_SENDER_ADDRESS = "work@hub.com";

    private final Logger logger = LoggerFactory.getLogger(getClass());


    private Properties openProperties(String path) {

        InputStream inputStream = PropertiesService.class.getResourceAsStream(path);

        Properties properties = new Properties();

        try {

            properties.load(inputStream);

        } catch (Exception e) {
	        logger.error(e.toString());
        }

        return properties;

    }

    @Bean
    public Properties getProperties() {
        return openProperties("/application.properties");
    }

    @Bean
    public Properties mailProperties() {
        return openProperties("/mail.properties");
    }

    public Map<String, String> getConnectionProperties() {

       Properties properties = getProperties();

        Map<String, String> propMap = new HashMap<>();

        propMap.put("url", properties.getProperty("jpa.url"));
        propMap.put("username", properties.getProperty("jpa.username"));
        propMap.put("password", properties.getProperty("jpa.password"));
        propMap.put("driver", properties.getProperty("jpa.driver"));

        return propMap;

    }

    public Map<String, String> getHibernateProperties() {

        Properties properties = getProperties();
        Map<String, String> propMap = new HashMap<>();

        propMap.put("ddl-auto", properties.getProperty("hibernate.hbm2ddl.auto"));
        propMap.put("dialect", properties.getProperty("hibernate.dialect"));
        propMap.put("search.provider", properties.getProperty("hibernate.search.provider"));
        propMap.put("search.dir", properties.getProperty("hibernate.search.dir"));
        propMap.put("show_sql", properties.getProperty("hibernate.show_sql"));

        return propMap;

    }

    public String getJwtSecret() {
        return getProperties().getProperty("jwt.secret");
    }

    public Map<String, String> getMailProperties() {

        Properties properties = mailProperties();
        Map<String, String> mailProperties = new HashMap<>();

        mailProperties.put(MAIL_USERNAME, properties.getProperty("mail.username"));
        mailProperties.put(MAIL_PASSWORD, properties.getProperty("mail.password"));
        mailProperties.put(MAIL_HOST, properties.getProperty("mail.host"));
        mailProperties.put(MAIL_PORT, properties.getProperty("mail.port"));

        return mailProperties;

    }

    public Map<String, String> getMailMessages() {

        Properties properties = mailProperties();
        Map<String, String> mailProperties = new HashMap<>();

        mailProperties.put(ACTIVATION_MESSAGE, properties.getProperty("mail.activation.message"));
        mailProperties.put(PASSWORD_CHANGED_MESSAGE, properties.getProperty("mail.changed_password.message"));
        mailProperties.put(PASSWORD_RECOVERY_MESSAGE, properties.getProperty("mail.recover_password.message"));
        mailProperties.put(USER_CREATED_BY_ADMIN_MAIL, properties.getProperty("mail.account_created_by_admin.message"));
		mailProperties.put(CONFIRM_PASSWORD_CHANGE_MESSAGE, properties.getProperty("mail.confirm_password_change.message"));

        return mailProperties;

    }

    public String getClientUrl() {

        return getProperties().getProperty("app.client.url");

    }

    public String getSenderAddress() {

        String address = getProperties().getProperty("mail.sender.address");

        return address != null ? address : MAIL_SENDER_ADDRESS;

    }

    public String getPhotosBasePath() {
    	return getProperties().getProperty("app.photos");
    }

    public String getDocumentsBasePath() {
    	return getProperties().getProperty("app.documents");
    }

    public Map<String, String> getOAuthClientCredentials(String client) {

        String base = "authorization.oauth2." + client;
        Map<String, String> oAuth2Properties = new HashMap<>();

        oAuth2Properties.put(OAUTH2_CLIENT_ID, getProperties().getProperty(base + ".client_id"));
        oAuth2Properties.put(OAUTH2_CLIENT_SECRET, getProperties().getProperty(base + ".client_secret"));

        return oAuth2Properties;
    }

    public Map<String, String> getMongoDBProperties() {
    	Properties allProperties = getProperties();

    	Map<String, String> mongoProperties = new HashMap<>();
	    mongoProperties.put(MONGODB_DATABASE_NAME, allProperties.getProperty("mongo.database.name"));
	    mongoProperties.put(MONGODB_HOST, allProperties.getProperty("mongo.host"));

	    return mongoProperties;
    }

}
