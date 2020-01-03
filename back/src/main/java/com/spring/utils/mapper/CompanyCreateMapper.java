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
public class CompanyCreateMapper {
    private SkillMapper skillMapper;
    private ModelMapper modelMapper;

    @Autowired
    public CompanyCreateMapper(SkillMapper skillMapper, ModelMapper modelMapper) {
        super();
        this.skillMapper = skillMapper;
        this.modelMapper = modelMapper;
    }

    public CompanyDto toDto(Company company) {
        if (company == null) {
            return null;
        }
        TypeMap<Company, CompanyDto> companyToCompanyDtoWithSkills = modelMapper.getTypeMap(Company.class, CompanyDto.class);
        if (companyToCompanyDtoWithSkills == null) {
            companyToCompanyDtoWithSkills = modelMapper.createTypeMap(Company.class, CompanyDto.class);
        }
        Converter<Set<Skill>, Set<SkillDto>> skillConverter =
                ctx -> ctx.getSource().stream().map(skill -> skillMapper.toDto(skill)).collect(Collectors.toSet());
        companyToCompanyDtoWithSkills.addMappings(mapper -> mapper.using(skillConverter).map(Company::getProposedSkills, CompanyDto::setSkills));
        return companyToCompanyDtoWithSkills.map(company);
    }
}
