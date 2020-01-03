package com.spring.utils.mapper;

import com.spring.dto.CompanyDto;
import com.spring.dto.SkillDto;
import com.spring.entity.Company;
import com.spring.entity.Skill;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompanyMapper implements DTOMapper<Company, CompanyDto> {

    private SkillMapper skillMapper;
    private ModelMapper modelMapper;

    @Autowired
    public CompanyMapper(SkillMapper skillMapper, ModelMapper modelMapper) {
        this.skillMapper = skillMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public CompanyDto toDto(Company company) {
        if (company == null) {
            return null;
        }
        TypeMap<Company, CompanyDto> companyToCompanyDtoTypeMap = modelMapper.getTypeMap(Company.class, CompanyDto.class);
        if (companyToCompanyDtoTypeMap == null) {
            companyToCompanyDtoTypeMap = modelMapper.createTypeMap(Company.class, CompanyDto.class);
        }
        Converter<Set<Skill>, Set<SkillDto>> skillConverter =
                ctx -> ctx.getSource().stream().map(skill -> skillMapper.toDto(skill)).collect(Collectors.toSet());

        companyToCompanyDtoTypeMap.addMappings(mapper -> mapper.using(skillConverter).map(Company::getProposedSkills, CompanyDto::setSkills));
        return companyToCompanyDtoTypeMap.map(company);
    }

	@Override
    public Company toEntity(CompanyDto companyDto) {
        if (companyDto == null) {
            return null;
        }
        TypeMap<CompanyDto, Company> companyDtoToCompanyTypeMap = modelMapper.getTypeMap(CompanyDto.class, Company.class);
        if (companyDtoToCompanyTypeMap == null) {
            companyDtoToCompanyTypeMap = modelMapper.createTypeMap(CompanyDto.class, Company.class);
        }
        Converter<Set<SkillDto>, Set<Skill>> skillConverter =
                ctx -> ctx.getSource().stream().map(skillDto -> skillMapper.toEntity(skillDto)).collect(Collectors.toSet());

        Converter<String, String> newLineToBr =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().replace("\n", "<br>");

        companyDtoToCompanyTypeMap.addMappings(mapper -> mapper.using(skillConverter).map(CompanyDto::getSkills, Company::setProposedSkills));
        companyDtoToCompanyTypeMap.addMappings(mapper -> mapper.using(newLineToBr).map(CompanyDto::getDescription, Company::setDescription));
        return companyDtoToCompanyTypeMap.map(companyDto);
    }

	public CompanyDto toDtoIgnoreSkills(Company company) {
		TypeMap<Company, CompanyDto> companyToCompanyDtoTypeMap = modelMapper.getTypeMap(Company.class, CompanyDto.class);
		if (companyToCompanyDtoTypeMap == null) {
			companyToCompanyDtoTypeMap = modelMapper.createTypeMap(Company.class, CompanyDto.class);
		}
		companyToCompanyDtoTypeMap.addMappings(x -> x.skip(CompanyDto::setSkills));
		return companyToCompanyDtoTypeMap.map(company);
	}

}
