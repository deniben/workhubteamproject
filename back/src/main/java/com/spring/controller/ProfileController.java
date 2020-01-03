package com.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.component.UserContext;
import com.spring.dto.ProfileDto;
import com.spring.dto.ProfileDtoOutput;
import com.spring.entity.Profile;
import com.spring.service.ProfileService;
import com.spring.utils.mapper.ProfileMapper;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

	private ProfileMapper profileMapper;

	private UserContext userContext;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

	private ProfileService profileService;

	@Autowired
	public ProfileController(ProfileMapper profileMapper, UserContext userContext, ProfileService profileService) {
		this.profileMapper = profileMapper;
		this.userContext = userContext;
		this.profileService = profileService;
	}

	@PostMapping
	public ResponseEntity<ProfileDto> createProfile(@RequestBody ProfileDto profileDto) {
		LOGGER.debug("in ProfileController crateProfile({})", profileDto);
		return ResponseEntity.ok(profileMapper.toDto(profileService.save(profileMapper.toEntity(profileDto))));
	}

	@PutMapping
	public ResponseEntity<ProfileDto> updateProfile(@RequestBody ProfileDto profileDto) {
		LOGGER.debug("in ProfileController updateProfile({})", profileDto);
		return ResponseEntity.ok(profileMapper.toDto(profileService.update(profileMapper.toEntity(profileDto, userContext.getCurrentUser().getProfile()))));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProfile(@PathVariable long id) {
		LOGGER.debug("in ProfileController deleteProfile({})", id);
		profileService.delete(id);
		return ResponseEntity.ok("Successfully deleted");
	}

	@GetMapping("/data")
	public ResponseEntity<ProfileDto> getProfileData() {
		LOGGER.trace("in ProfileController getProfileData()");
		return ResponseEntity.ok(profileMapper.toDto(userContext.getCurrentUser().getProfile()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProfileDtoOutput> findProfileById(@PathVariable long id) {
		LOGGER.debug("in ProfileController findProfileById({})", id);
		Profile profile = profileService.findById(id);
		return ResponseEntity.ok(profileMapper.toDtoOutput(profile));
	}

	@GetMapping("/isOwner")
	public ResponseEntity<Boolean> isCurrentUserCompanyOwner() {
        LOGGER.trace("in ProfileController isCurrentUserCompanyOwner()");
		Boolean isOwner = profileService.isUserOwner();
		return ResponseEntity.ok(isOwner);
	}

}

