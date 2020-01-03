package com.spring.service.implementations;

import com.spring.component.UserContext;
import com.spring.entity.Company;
import com.spring.entity.Profile;
import com.spring.entity.User;
import com.spring.dao.CompanyDao;
import com.spring.dao.ProfileDao;
import com.spring.exception.RestException;
import com.spring.service.JoinCompanyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JoinCompanyServiceImpl implements JoinCompanyService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final CompanyDao companyDao;
    private final MailService mailService;
    private final ProfileDao profileDao;
    private final UserContext userContext;

	public JoinCompanyServiceImpl(CompanyDao companyDao, MailService mailService, ProfileDao profileDao, UserContext userContext) {
		this.companyDao = companyDao;
		this.mailService = mailService;
		this.profileDao = profileDao;
		this.userContext = userContext;
	}

	@Override
    public List<Profile> getMembershipApplications(Integer page) {

        LOGGER.info("getMembershipApplications() used");

        Company company = userContext.getCurrentUser().getProfile().getCompany();
        List<Profile> profiles = profileDao.findApplicationsByCompany(company, false, page,1);

        LOGGER.info(profiles.size() + " membership applications were returned for Company: " + company.getName() + " with id: " + company.getId());
        return profiles;
    }


    @Override
    public void acceptMembership(Long userId) {
        Company company = userContext.getCurrentUser().getProfile().getCompany();
        Profile acceptedUser = profileDao.findById(userId).get();

        if (acceptedUser == null) {
            throw new RestException("User with such id do not exists", HttpStatus.BAD_REQUEST.value());
        }

        if (!acceptedUser.getCompany().getId().equals(company.getId())) {
            throw new RestException("User did not ask for membership", HttpStatus.BAD_REQUEST.value());
        }

        acceptedUser.setAccepted(true);
        profileDao.save(acceptedUser);
        mailService.sendAcception(userId);

        LOGGER.info("User with id " + acceptedUser.getUser() + " accepted to company with id " + acceptedUser.getCompany().getId());
    }

    @Override
    public void rejectMembership(Long userId) {

        Company companyU = userContext.getCurrentUser().getProfile().getCompany();
        Profile rejectedUser = profileDao.findById(userId).get();

        if (rejectedUser == null) {
            throw new RestException("User with such id do not exists", HttpStatus.BAD_REQUEST.value());
        }

        if (!rejectedUser.getCompany().getId().equals(companyU.getId())) {
            throw new RestException("User did not ask for membership", HttpStatus.BAD_REQUEST.value());
        }
        rejectedUser.setAccepted(null);
        rejectedUser.setCompany(null);
        profileDao.save(rejectedUser);

    }


    @Override
    public void joinCompany(String name) {

        User user = userContext.getCurrentUser();

        Profile profile = user.getProfile();

        if (profile.getCompany() != null) {
            throw new RestException("User works for some company");
        }

        Optional<Company> company = companyDao.findByName(name);

        if (company.isPresent()) {

            profile.setCompany(company.get());
            profileDao.save(profile);

            LOGGER.info("User with id " + user.getId() + " applied to join company with id " + company.get().getId());
            return;

        }

        throw new RestException("Company with such name do not exists", HttpStatus.BAD_REQUEST.value());
    }

}

