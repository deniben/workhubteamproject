package com.spring.service;

import java.util.Optional;

import com.spring.dto.PageableResponse;

public interface IpControlService {

	Optional<String> checkAddress(String address);

	PageableResponse getAllBlockedAddresses(Integer page);

	void blockIp(String address, String reason);

	void unblockIp(Long id);
}
