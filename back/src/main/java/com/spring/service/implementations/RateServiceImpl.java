package com.spring.service.implementations;

import com.spring.enums.ProjectStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.component.UserContext;
import com.spring.dao.CompanyDao;
import com.spring.dao.ProjectDao;
import com.spring.entity.Company;
import com.spring.entity.Profile;
import com.spring.entity.Project;
import com.spring.entity.User;
import com.spring.enums.CompanyType;
import com.spring.exception.RestException;
import com.spring.service.RateService;

import java.util.Optional;

@Service
@Transactional
public class RateServiceImpl implements RateService {

    private final ProjectDao projectDao;
    private final CompanyDao companyDao;
    private final UserContext userContext;

    @Autowired
    public RateServiceImpl(ProjectDao projectDao, CompanyDao companyDao, UserContext userContext) {
        this.projectDao = projectDao;
        this.companyDao = companyDao;
        this.userContext = userContext;
    }

    @Override
    public void rate(Long projectId, Long rate, ProjectStatus status) {
        Optional<Project> project = projectDao.findById(projectId);

        User user = userContext.getCurrentUser();
        Profile profile = user.getProfile();
        Company company = profile.getCompany();

        if (project.isPresent()) {
            if (company.getType().equals(CompanyType.EMPLOYEE) && project.get().getEmployerMark() != null ||
                    company.getType().equals(CompanyType.EMPLOYER) && project.get().getEmployeeMark() != null) {
                throw new RestException("Project is already evaluated");
            }

            if (rate == null || rate < 1 || rate > 5) {
                throw new RestException("Rate has to be between 1 and 5");
            }

            if (company.getType().equals(CompanyType.EMPLOYEE)) {
                project.get().setEmployerMark(rate);
                companyDao.updateAverageRate(project.get().getCompanyCreator());

            } else {
                project.get().setEmployeeMark(rate);

                companyDao.updateAverageRate(project.get().getCompanyEmployee());
                projectDao.changeStatus(projectId, status);
            }

            projectDao.save(project.get());
        }
        else {
            throw new RestException("Project with such id do not exists");
        }




    }

}
