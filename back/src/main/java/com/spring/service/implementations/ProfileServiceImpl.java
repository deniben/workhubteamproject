package com.spring.service.implementations;


import com.spring.component.UserContext;
import com.spring.dao.ProfileDao;
import com.spring.entity.Company;
import com.spring.entity.FileDocument;
import com.spring.entity.Profile;
import com.spring.entity.User;
import com.spring.enums.PhotoType;
import com.spring.exception.OtherException;
import com.spring.service.PhotosService;
import com.spring.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private ProfileDao profileDao;

    private PhotosService photosService;

    private ValidationService validationService;

    private UserContext userContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Autowired
    public ProfileServiceImpl(ProfileDao profileDao, PhotosService photosService, ValidationService validationService, UserContext userContext) {
        this.userContext = userContext;
        this.profileDao = profileDao;
        this.photosService = photosService;
        this.validationService = validationService;
    }

    @Override
    public Profile save(Profile profile) {
        LOGGER.trace("in ProfileServiceImpl save()");
        validationService.profileValidation(profile);
        if (!photosService.isPhotoExist(profile.getPhotoUrl())) {
            photosService.downloadPhoto(profile.getPhotoUrl());
            profile.setPhotoUrl(photosService.getUniqueFileName(PhotoType.PROFILE));
        }
        User user = userContext.getCurrentUser();
        profile.setId(user.getId());
        profile.setUser(user);
        profile.setAccepted(false);
        profileDao.save(profile);
        return profile;
    }

    @Override
    public Profile update(Profile profile) {
        LOGGER.debug("in ProfileServiceImpl update({})", profile);
        validationService.profileValidation(profile);
        profileDao.save(profile);
        return profile;
    }

    @Override
    public void delete(long id) {
        LOGGER.debug("in ProfileServiceImpl delete({})", id);
        profileDao.delete(id);
    }

    @Override
    public Profile findById(long id) {
        LOGGER.debug("in ProfileServiceImpl findById({})", id);
        Optional<Profile> profile = profileDao.findById(id);
        if (profile.isPresent()) {
            return profile.get();
        }
        throw new OtherException("No profile with that id");
    }

    @Override
    public List<Profile> findApplicationsByCompany(Company company, boolean status, Integer page,Integer countOfItems) {
        LOGGER.debug("in PhotoServiceImpl findApplicationsByCompany({},{},{},{})", company, status, page,countOfItems);
        return profileDao.findApplicationsByCompany(company, status, page,countOfItems);
    }

    @Override
    public Integer getNumberOfPages(Long companyId, boolean accepted,Integer countOfItems) {
        LOGGER.trace("in PhotoServiceImpl getNumberOfPages()");
        Integer dataSize = profileDao.getCountOfProfilesByCompany(companyId, accepted);

        float pages = (float) dataSize / countOfItems;
        if (pages * 10000 > ((int) pages) * 10000) {
            pages++;
        }
        return (int) pages;
    }

    @Override
    public void useForeignAccount(OAuth2User oAuth2User) {
	    LOGGER.debug("in PhotoServiceImpl useForeignAccount({})", oAuth2User);
        Map<String, Object> profileData = oAuth2User.getAttributes();
        if (profileData.size() > 0) {
            Profile profile = new Profile();
            User user = userContext.getCurrentUser();
            profile.setId(user.getId());
            profile.setUser(user);
            profile.setAccepted(false);
            profile.setNickname((String) profileData.get("nickname"));
            profile.setFirstName(profileData.get("firstName") != null ? (String) profileData.get("firstName") : "User");
            profile.setLastName(profileData.get("lastName") != null ? (String) profileData.get("lastName") : profile.getNickname());
            profile.setPhotoUrl((String) profileData.get("photoUrl"));
            validationService.profileValidation(profile);
            save(profile);
        }
    }

	@Override
	public Boolean isUserDocumentOwner(FileDocument fileDocument) {
		LOGGER.trace("in PhotoServiceImpl isUserDocumentOwner()");
		User user = userContext.getCurrentUser();
		Profile profile = user.getProfile();
		return profile.getCompany().getId().equals(fileDocument.getOwnerId());
	}

    @Override
    public Boolean isUserOwner() {
        LOGGER.trace("in PhotoServiceImpl isUserOwner()");
        User user = userContext.getCurrentUser();
        Profile profile = user.getProfile();
        Profile companyOwner = profile.getCompany().getOwner();
        return profile.getId().equals(companyOwner.getId());
    }

}
