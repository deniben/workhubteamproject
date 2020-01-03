package com.spring.component;

import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
public class IndexerInitializer {

    @Autowired
    SessionFactory sessionFactory;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @PostConstruct
    @Transactional
    public void initIndexer() {

        FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.openSession());

        try {
            fullTextSession.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            logger.warn("Hibernate Search indexer initializar");
        } catch (Exception e) {
        	logger.debug(e.getMessage());
        }
    }

}
