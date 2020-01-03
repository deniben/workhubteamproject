package com.spring.controller;


import com.google.common.base.Strings;
import com.spring.dto.CompanyDto;
import com.spring.dto.PageableResponse;
import com.spring.entity.Company;
import com.spring.entity.Skill;
import com.spring.enums.CompanyType;
import com.spring.service.CompanyService;
import com.spring.service.PhotosService;
import com.spring.utils.mapper.CompanyCreateMapper;
import com.spring.utils.mapper.CompanyMapper;
import io.swagger.models.auth.In;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);
    private CompanyService companyService;
    private CompanyMapper companyMapper;
    private CompanyCreateMapper companyCreateMapper;
    private PhotosService photosService;

    @Autowired
    public CompanyController(CompanyService companyService, CompanyMapper companyMapper, PhotosService photosService, CompanyCreateMapper companyCreateMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
        this.photosService = photosService;
        this.companyCreateMapper = companyCreateMapper;
    }

    @GetMapping
    public List<CompanyDto> getAllCompanies() {
        LOGGER.trace("in getAllCompanies()");
        return companyService.findAllUnblockedCompanies().stream().map(company -> {
            company.setPhotoUrl(photosService.getPhoto(company.getPhotoUrl()));
            return companyMapper.toDtoIgnoreSkills(company);
        }).collect(Collectors.toList());
    }

    @GetMapping("/allComp/{page}")
    public ResponseEntity<PageableResponse> getAllAdminCompanies(@PathVariable Integer page) {
        LOGGER.debug("in getAllCompanies({})", page);
        List<CompanyDto> companyDtos = companyService.findAllUnblockedCompanies(page, 8).stream().map(company -> {
            company.setPhotoUrl(photosService.getPhoto(company.getPhotoUrl()));
            return companyMapper.toDtoIgnoreSkills(company);
        }).collect(Collectors.toList());
        Integer pages = companyService.numberOfAllUnblockedCompanyPages();
        return ResponseEntity.ok(new PageableResponse(pages, companyDtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id) {
	    LOGGER.debug("in getCompanyById(), id: {}", id);
        Company company = companyService.findCompanyById(id);
        if (!Strings.isNullOrEmpty(company.getPhotoUrl())) {
            company.setPhotoUrl(photosService.getPhoto(company.getPhotoUrl()));
        }
        return ResponseEntity.ok(companyMapper.toDto(company));
    }

    @GetMapping("/my")
    public ResponseEntity<CompanyDto> getCurrentUserCompany() {
        LOGGER.trace("in getCurrentPrincipalUserCompany()");
        Company company = companyService.findMyCompany();
        if (!Strings.isNullOrEmpty(company.getPhotoUrl())) {
            company.setPhotoUrl(photosService.getPhoto(company.getPhotoUrl()));
        }
        CompanyDto companyDto = companyMapper.toDto(company);
        return ResponseEntity.ok(companyDto);
    }

    @GetMapping("/{id}/check-owner")
    public ResponseEntity<CompanyDto> getCompanyByIdWithCheckOwner(@PathVariable Long id) {
	    LOGGER.debug("in getCompanyByIdForUpdate(), id: {}", id);
        Company company = companyService.findCompanyByIdCheckOwner(id);
        if (!Strings.isNullOrEmpty(company.getPhotoUrl())) {
            company.setPhotoUrl(photosService.getPhoto(company.getPhotoUrl()));
        }
        return ResponseEntity.ok(companyMapper.toDto(company));
    }

    @PostMapping
    public ResponseEntity createCompany(@RequestBody CompanyDto companyDTO) {
	    LOGGER.debug("in createCompany(), companyDTO: {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        if (company.getType() != null && companyDTO.getType() != null) {
            CompanyType typeOfCompany = companyDTO.getType();
            //Convert all set of skillDto into Skill set
            if (typeOfCompany.equals(CompanyType.EMPLOYEE)) {
                Set<Skill> skillSet = companyDTO.getSkills().stream().map(skillDto -> modelMapper.map(skillDto, Skill.class)).collect(Collectors.toSet());
                company.setProposedSkills(skillSet);
            }
        }
        Company savedCompany = companyService.saveCompany(company);
        if (!Strings.isNullOrEmpty(company.getPhotoUrl())) {
            savedCompany.setPhotoUrl(photosService.getPhoto(savedCompany.getPhotoUrl()));
        }
        CompanyDto returnedDto = companyCreateMapper.toDto(savedCompany);
        return ResponseEntity.ok(returnedDto);
    }

	@DeleteMapping("/{companyId}")
	public ResponseEntity deleteCompany(@PathVariable Long companyId) {
		LOGGER.debug("in deleteCompany(), id: {}", companyId);
		return ResponseEntity.ok(companyService.deleteCompany(companyId));
	}

	@PutMapping
	public ResponseEntity updateCompany(@RequestBody CompanyDto companyDto) {
		LOGGER.debug("in updateCompany(), companyDto: {}", companyDto);
		Company company = companyService.findCompanyById(companyDto.getId());
		//Convert all set of skillDto into Skill set
		if(company.getType().equals(CompanyType.EMPLOYEE)) {
			Set<Skill> skillSet = companyDto.getSkills().stream().map(skillDto -> modelMapper.map(skillDto, Skill.class)).collect(Collectors.toSet());
			company.setProposedSkills(skillSet);
		}
		company.setName(companyDto.getName());
		company.setDescription(companyDto.getDescription());
		company.setPhotoUrl(companyDto.getPhotoUrl());
		return ResponseEntity.ok(companyMapper.toDto(companyService.updateCompany(company)));
	}

    @GetMapping("/admin/blockedCompanies/{page}")
    public ResponseEntity<PageableResponse> getBlockedCompaniesAdm(@PathVariable Integer page) {
        LOGGER.trace("in getBlockedCompaniesAdm()");
        List<CompanyDto> companyDtos = companyService.findAllBlockedCompanies(page, 8).stream().map(company -> {
            company.setPhotoUrl(photosService.getPhoto(company.getPhotoUrl()));
            return new CompanyDto(company);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(new PageableResponse(companyService.numberOfAllBlockedCompanyPages(), companyDtos));
    }

	@GetMapping("admin/block/unblock-company/{id}/{isBlocked}")
	public void blockCompany(@PathVariable Long id, @PathVariable boolean isBlocked) {
		LOGGER.debug("in blockCompany(), id: {}, isBlocked: {}", id, isBlocked);
		companyService.blockUnblockCompany(id, isBlocked);
	}

	@GetMapping(value = "/top-employer")
	public List<CompanyDto> topCompEmployer() {
		LOGGER.trace("in topCompEmployer()");
		List<Company> companies = companyService.topCompany(CompanyType.EMPLOYER).stream().map(x -> companyService.findCompanyById(x.getId()))
				.collect(Collectors.toList());

        List<CompanyDto> companyDtos = new ArrayList<>();
        for (Company company : companies) {
            CompanyDto companyDto = companyMapper.toDtoIgnoreSkills(company);
            companyDtos.add(companyDto);
        }
        return companyDtos;
    }

    @GetMapping("/top-employee")
    public List<CompanyDto> topCompEmployee() {
	    LOGGER.debug("in topCompEmployee()");
	    List<Company> companies = companyService.topCompany(CompanyType.EMPLOYEE).stream().map(x -> companyService.findCompanyById(x.getId()))
                .collect(Collectors.toList());
        List<CompanyDto> companyDtos = new LinkedList<>();
        for (Company company : companies) {
            CompanyDto companyDto = companyMapper.toDtoIgnoreSkills(company);
            companyDtos.add(companyDto);
        }
        return companyDtos;
    }

}
