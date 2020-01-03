package com.spring.service.implementations;

import com.spring.component.UserContext;
import com.spring.dao.CompanyDao;
import com.spring.dao.ProfileDao;
import com.spring.entity.Company;
import com.spring.entity.Profile;
import com.spring.entity.User;
import com.spring.enums.CompanyType;
import com.spring.exception.RestException;
import com.spring.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private static final String PROFILE_NOT_FOUND_ERROR = "Unable to find user profile for user with '%s' id";
    private final CompanyDao companyDao;
    private final ProfileDao profileDao;
    private final UserContext userContext;
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

    @Autowired
    public CompanyServiceImpl(CompanyDao companyDao, ProfileDao profileDao, UserContext userContext) {
        this.companyDao = companyDao;
        this.profileDao = profileDao;
        this.userContext = userContext;
    }


    @Override
    public List<Company> findAllUnblockedCompanies(Integer firstResult, Integer maxResult) {
        LOGGER.trace("in findAllUnblockedCompanies()");
        return companyDao.allCompanies(firstResult, maxResult);
    }

    @Override
    public List<Company> findAllUnblockedCompanies() {
        LOGGER.trace("in findAllUnblockedCompanies()");
        return companyDao.allCompanies(0, companyDao.numberOfAllUnblockedCompanies());
    }

    @Override
    public List<Company> findAllBlockedCompanies(Integer firstResult, Integer maxResult) {
        LOGGER.trace("in findAllBlockedCompanies()");
        return companyDao.blockedCompanies(firstResult, maxResult);
    }

    @Override
    public Company findMyCompany() {
        LOGGER.trace("in findMyCompany()");
        User currentUser = userContext.getCurrentUser();
        Long currentUserId = currentUser.getId();
        Profile profile = currentUser.getProfile();

        if (profile == null) {
            throw new RestException(String.format(PROFILE_NOT_FOUND_ERROR, currentUserId), 404);
        }

        if (profile.getCompany() != null) {
            return findCompanyById(profile.getCompany().getId());
        } else {
            return null;
        }
    }


    @Override
    public Company saveCompany(Company company) {
        LOGGER.debug("in saveCompany(), company: {}", company);

        User user = userContext.getCurrentUser();

        Profile profile = user.getProfile();

        if (profile == null) {
            throw new RestException(String.format(PROFILE_NOT_FOUND_ERROR, user.getId()), 404);
        }

        if (profile.getCompany() != null) {
            throw new RestException(("You already have company"), 405);
        }

        if (company.getType() == null) {
            throw new RestException(String.format("Type of company didn't choose, chosen type: '%s'", company.getType()), 404);
        }
        company.setOwner(profile);
        company = companyDao.saveAndReturn(company);
        profile.setAccepted(true);
        profile.setCompany(company);
        profileDao.save(profile);
        LOGGER.trace("exit from saveCompany()");
        return company;
    }

    @Override
    public Company deleteCompany(long companyId) {
        LOGGER.debug("in deleteCompany(), id: {}", companyId);
        Company company = findCompanyById(companyId);
        if (company != null) {
            companyDao.delete(companyId);
            return company;
        } else {
            throw new RestException(String.format("Unable to find company by '%d' id", companyId), 404);
        }
    }

    @Override
    public Company updateCompany(Company companyFromUI) {
        LOGGER.trace("in updateCompany(), company: {}", companyFromUI);
        User user = userContext.getCurrentUser();
        Profile profile = user.getProfile();
	    if (profile == null) {
		    throw new RestException(String.format(PROFILE_NOT_FOUND_ERROR, user.getId()), 404);
	    }
        Company companyFromDb = findCompanyById(companyFromUI.getId());
        //check if company exist in DB, can be null
        if (companyFromDb == null) {
            throw new RestException(String.format("Company was not found. CompanyId: %s", companyFromUI.getId()), 404);
        }

        Profile ownerProfile = companyFromDb.getOwner();
        //check if profile is owner of company
        if (!profile.getId().equals(ownerProfile.getId()) && !profile.getUser().isAdmin()) {
            throw new RestException(String.format("This profile do not have permission to update this company. OwnerId: %s, ProfileId: %s",
                    ownerProfile.getId(), profile.getId()), 403);
        }
        companyFromDb.setProposedSkills(companyFromUI.getProposedSkills());
        companyFromDb.setDescription(companyFromUI.getDescription());
        companyFromDb.setName(companyFromUI.getName());
        if (companyFromUI.getPhotoUrl() == null) {
            companyFromDb.setPhotoUrl(companyFromDb.getPhotoUrl());
        } else {
            companyFromDb.setPhotoUrl(companyFromUI.getPhotoUrl());
        }
        if (companyFromUI.isBlocked() != null) {
            companyFromDb.setBlocked(companyFromUI.isBlocked());
        } else {
            companyFromDb.setBlocked(false);
        }
        companyDao.save(companyFromDb);
        LOGGER.trace("company updated successfully!");
        return companyFromDb;
    }

    @Override
    public void blockUnblockCompany(Long id, boolean isBlocked) {
        LOGGER.debug("in blockUnblockCompany(), id: {}, isBlocked: {}", id, isBlocked);
        Company company = findCompanyById(id);
        company.setBlocked(isBlocked);
        company.setName(company.getName());
        company.setDescription(company.getDescription());
        updateCompany(company);
    }

    @Override
    public List<Company> topCompany(CompanyType companyType) {
        LOGGER.debug("in topComp(), companyType: {}", companyType);
        return companyDao.topComp(companyType);
    }

    @Override
    public Company findCompanyById(long companyId) {
	    LOGGER.debug("in findCompanyById(), id: {}", companyId);
        Optional<Company> company = companyDao.findById(companyId);
        if (company.isPresent()) {
            company.get().getProposedSkills().size();
            return company.get();
        }
        throw new RestException(String.format("Company with '%s' id was not found", companyId), 404);
    }

  @Override
  public Company findCompanyByIdCheckOwner(long companyId) {
	  LOGGER.debug("in findCompanyById(), id: {}", companyId);
    User user = userContext.getCurrentUser();
    //get user profile who wants edit company
    Profile profile = user.getProfile();
    // get company from DB
    Company company = findCompanyById(companyId);

        if (company == null || profile.getCompany() == null || !company.getOwner().getId().equals(profile.getId())) {
            throw new RestException("Company was not found OR you don't have permission", 404);
        }
        company.getProposedSkills().size();
        return company;
    }

    @Override
    public List<Company> findAllCompaniesForMyProject(Long id, Integer page) {
        LOGGER.debug("in findAllCompaniesForMyProject(), id: {}, page: {}", id, page);
        return companyDao.findAllCompanyForMyProject(id, page);
    }


    @Override
    public Company getCompanyByProjectId(Long id) {
        LOGGER.trace("in getCompanyByProjectId()");
        return companyDao.getCompanyByProjectId(id);
    }

    public Optional<Company> findByName(String name) {
        return companyDao.findByName(name);
    }

    @Override
    public Integer numberOfAllCompanyToMyProjectPages(Long id) {
        LOGGER.trace("in numberOfCompanyPages()");
        Integer dataSize = companyDao.numberOfCompanyToMyProject(id);

        float pages = (float) dataSize / 4;

        if (pages * 10000 > ((int) pages) * 10000) {
            pages++;
        }

        return (int) pages;
    }

    @Override
    public Integer numberOfAllBlockedCompanyPages() {
        LOGGER.trace("in numberOfAllBlockedCompanyPages()");
        Integer dataSize = companyDao.numberOfAllBlockedCompanies();

        float pages = (float) dataSize / 8;

        if (pages * 10000 > ((int) pages) * 10000) {
            pages++;
        }
        return (int) pages--;
    }

    @Override
    public Integer numberOfAllUnblockedCompanyPages() {
        LOGGER.trace("in numberOfAllUnblockedCompanyPages()");
        Integer dataSize = companyDao.numberOfAllUnblockedCompanies();
        float pages = (float) dataSize / 8;

        if (pages * 10000 > ((int) pages)
                * 10000) {
            pages++;
        }
        return (int) pages--;
    }

}
