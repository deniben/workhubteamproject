package com.spring.service.implementations;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.BlockedIPDao;
import com.spring.dto.PageableResponse;
import com.spring.entity.BlockedIP;
import com.spring.exception.RestException;
import com.spring.service.IpControlService;

@Service
@Transactional
public class IpControlServiceImpl implements IpControlService {

	private final BlockedIPDao blockedIPDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(IpControlServiceImpl.class);


	@Autowired
	public IpControlServiceImpl(BlockedIPDao blockedIPDao) {
		this.blockedIPDao = blockedIPDao;
	}


	@Override
	public Optional<String> checkAddress(String address) {
		LOGGER.debug("in checkAddress(address: {})", address);
		Optional<BlockedIP> blockedIP = blockedIPDao.findByAddress(address);

		if(blockedIP.isPresent()) {
			if(blockedIP.get().getReason() != null) {
				return Optional.of(blockedIP.get().getReason());
			}
			return Optional.of("Dangerous activity");
		}

		return Optional.empty();

	}

	@Override
	public PageableResponse getAllBlockedAddresses(Integer page) {
		LOGGER.debug("in getAllBlockedAddresses(page: {})", page);

		return blockedIPDao.findAll(page);
	}

	@Override
	public void blockIp(String address, String reason) {
		LOGGER.debug("in blockIp(address: {}, reason: {})", address, reason);
		if(!address.matches("(\\d{1,3}\\.){3}\\d{1,3}")) {
			throw new RestException("Invalid address format");
		}

		if(blockedIPDao.findByAddress(address).isPresent()) {
			throw new RestException("Address is already blocked");
		}

		if(reason == null || reason.isEmpty()) {
			throw new RestException("Reason field can not be empty");
		}

		BlockedIP blockedIP = new BlockedIP();
		blockedIP.setAddress(address);
		blockedIP.setReason(reason);
		blockedIP.setDate(LocalDate.now());

		blockedIPDao.save(blockedIP);
	}

	@Override
	public void unblockIp(Long id) {
		LOGGER.debug("in unblockIp(id: {})", id);
		Optional<BlockedIP> blockedIP = blockedIPDao.findById(id);

		blockedIP.ifPresent(ip -> blockedIPDao.delete(ip.getId()));
		throw new RestException("Address is not blocked");

	}

}
