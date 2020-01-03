package com.spring.configuration;

import com.spring.service.implementations.PropertiesService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.spring.configuration")
public class JpaConfig {

    @Autowired
    PropertiesService propertiesService;

    @Bean
    public LocalSessionFactoryBean localSessionFactoryBean() {

        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();

        localSessionFactoryBean.setDataSource(dataSource());
        localSessionFactoryBean.setPackagesToScan("com.spring.entity");
        localSessionFactoryBean.setHibernateProperties(jpaProperties());

        return localSessionFactoryBean;

    }

    @Bean
    @Autowired
    public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {

        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactory);

        return hibernateTransactionManager;

    }


    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();

        Map<String, String> properties = propertiesService.getConnectionProperties();

        driverManagerDataSource.setDriverClassName(properties.get("driver"));
        driverManagerDataSource.setUrl(properties.get("url"));
        driverManagerDataSource.setUsername(properties.get("username"));
        driverManagerDataSource.setPassword(properties.get("password"));

        return driverManagerDataSource;

    }

    @Bean
    public Properties jpaProperties() {

        Properties properties = new Properties();

        Map<String, String> configs = propertiesService.getHibernateProperties();

        properties.setProperty("hibernate.hbm2ddl.auto", configs.get("ddl-auto"));
        properties.setProperty("hibernate.dialect", configs.get("dialect"));
        properties.setProperty("hibernate.search.default.directory_provider", configs.get("search.provider"));
        properties.setProperty("hibernate.search.default.indexBase", configs.get("search.dir"));
        properties.setProperty("hibernate.show_sql", configs.get("show_sql"));


        return properties;

    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(104857600000000l);
        multipartResolver.setMaxUploadSizePerFile(1048576000000l);
        return multipartResolver;
    }


}
