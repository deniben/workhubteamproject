package com.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.BlockIpDto;
import com.spring.exception.RestException;
import com.spring.service.IpControlService;

@RestController
@RequestMapping("/admin/block/addresses")
public class BlockIPController {

	private final IpControlService ipControlService;
	private static final Logger LOGGER = LoggerFactory.getLogger(BlockIPController.class);


	@Autowired
	public BlockIPController(IpControlService ipControlService) {
		this.ipControlService = ipControlService;
	}

	@GetMapping("/{page}")
	public ResponseEntity getBlockedAddresses(@PathVariable Integer page) {
		LOGGER.debug("in getBlockedAddresses(), page: {}", page);
		return ResponseEntity.ok(ipControlService.getAllBlockedAddresses(page));
	}

	@PostMapping
	public ResponseEntity blockAddress(@RequestBody BlockIpDto blockIpDto) {
		LOGGER.debug("in blockAddress(), blockIpDto: {}", blockIpDto);
		if(blockIpDto == null) {
			throw new RestException("Input data can not be empty");
		}

		ipControlService.blockIp(blockIpDto.getAddress(), blockIpDto.getReason());
		return ResponseEntity.ok("Successfully blocked");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity unblockAddress(@PathVariable Long id) {
		LOGGER.debug("in unblockAddress(), id: {}", id);
		ipControlService.unblockIp(id);
		return ResponseEntity.ok("Successfully unblocked");
	}

}
