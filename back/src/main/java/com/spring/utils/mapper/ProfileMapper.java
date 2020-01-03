package com.spring.utils.mapper;

import com.spring.dto.ProfileDto;
import com.spring.dto.ProfileDtoOutput;
import com.spring.entity.Profile;
import com.spring.service.PhotosService;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper implements DTOMapper<Profile, ProfileDto> {

	private final PhotosService photosService;
	private ModelMapper modelMapper;
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileMapper.class);

	@Autowired
	public ProfileMapper(PhotosService photosService, ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.photosService = photosService;
	}

	public Profile toEntity(ProfileDto profileDto) {
		LOGGER.debug("in ProfileMapper toEntity({})", profileDto);
		if(profileDto == null) {
			return null;
		}
		return modelMapper.map(profileDto, Profile.class);
	}

	public ProfileDto toDto(Profile profile) {
		LOGGER.debug("in ProfileMapper toDto({})", profile);
		if(profile == null) {
			return null;
		}
		TypeMap<Profile, ProfileDto> toDtoTypeMap = modelMapper.getTypeMap(Profile.class, ProfileDto.class);
		if(toDtoTypeMap == null) {
			toDtoTypeMap = modelMapper.createTypeMap(Profile.class, ProfileDto.class);
		}
		toDtoTypeMap.addMappings(x -> x.skip(ProfileDto::setAdmin));

		ProfileDto profileDto = toDtoTypeMap.map(profile);
		profileDto.setAdmin(profile.getUser().isAdmin());

		if(profileDto.getPhotoUrl() != null && !profileDto.getPhotoUrl().startsWith("http")) {
			profileDto.setPhotoUrl(photosService.getPhoto(profileDto.getPhotoUrl()));
		}

		return profileDto;
	}

	public ProfileDtoOutput toDtoOutput(Profile profile) {
		LOGGER.debug("in ProfileMapper toDtoOutput({})", profile);
		if(profile == null) {
			return null;
		}
		if(photosService.isPhotoExist(profile.getPhotoUrl())) {
			profile.setPhotoUrl(photosService.getPhoto(profile.getPhotoUrl()));
		}
		if(photosService.isPhotoExist(profile.getCompany().getPhotoUrl())) {
			profile.getCompany().setPhotoUrl(photosService.getPhoto(profile.getCompany().getPhotoUrl()));
		}
		return modelMapper.map(profile, ProfileDtoOutput.class);
	}

	public Profile toEntity(ProfileDto profileDto, Profile profile) {
		LOGGER.debug("in ProfileMapper toEntity({},{})", profileDto, profile);
		if(profile == null || profileDto == null) {
			return null;
		}
		modelMapper.map(profileDto, profile);
		return profile;
	}
}
