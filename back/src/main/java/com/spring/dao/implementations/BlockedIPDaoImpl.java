package com.spring.dao.implementations;

import java.util.Optional;

import javax.persistence.NoResultException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.spring.controller.CompanyController;
import com.spring.dao.BlockedIPDao;
import com.spring.dto.PageableResponse;
import com.spring.entity.BlockedIP;
import com.spring.utils.Page;

@Repository
public class BlockedIPDaoImpl extends BaseImpl<BlockedIP> implements BlockedIPDao {

	private final static Integer PAGE_SIZE = 20;
	private static final Logger LOGGER = LoggerFactory.getLogger(BlockedIPDaoImpl.class);

	@Override
	public Optional<BlockedIP> findByAddress(String address) {
		LOGGER.debug("in findByAddress(), address: {}", address);
		Session session = sessionFactory.getCurrentSession();
		try {
			return Optional.of(session.createQuery("from BlockedIP bip where bip.address = :address", BlockedIP.class)
					.setParameter("address", address)
					.getSingleResult());
		} catch(NoResultException e) {
			return Optional.empty();
		}
	}


	@Override
	public PageableResponse findAll(Integer page) {
		LOGGER.debug("in findAll(), page: {}", page);

		Session session = sessionFactory.getCurrentSession();
		Integer totalSize = session.createQuery("select count(*) from BlockedIP", Long.class)
				.getSingleResult()
				.intValue();

		return new PageableResponse(Page.countPages(totalSize, PAGE_SIZE),
				session.createQuery("from BlockedIP", BlockedIP.class)
						.setFirstResult(page * PAGE_SIZE)
						.setMaxResults(PAGE_SIZE)
						.getResultList());
	}
}
