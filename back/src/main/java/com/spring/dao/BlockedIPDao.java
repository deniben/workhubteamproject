package com.spring.dao;

import java.util.Optional;

import com.spring.dto.PageableResponse;
import com.spring.entity.BlockedIP;

public interface BlockedIPDao extends BaseDao<BlockedIP> {
	Optional<BlockedIP> findByAddress(String address);
	PageableResponse findAll(Integer page);
}
