package com.spring.dto;

import com.spring.entity.Company;
import com.spring.enums.CompanyType;

import java.util.Set;

public class CompanyDto {

    private Long id;
    private String name;
    private String description;
    private CompanyType type;
    private Set<SkillDto> skills;
    private Long avgMark;
    private Boolean blocked;
    private String photoUrl;

    public CompanyDto() {

    }

    public CompanyDto(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.description = company.getDescription();
        this.type = company.getType();
        this.avgMark = company.getAvgMark();
        this.photoUrl = company.getPhotoUrl();
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Set<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillDto> skills) {
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CompanyType getType() {
        return type;
    }

    public void setType(CompanyType type) {
        this.type = type;
    }

    public Long getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(Long avgMark) {
        this.avgMark = avgMark;
    }

}
