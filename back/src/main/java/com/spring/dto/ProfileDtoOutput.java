package com.spring.dto;

public class ProfileDtoOutput extends ProfileDto {

    private UserDto user;
    private CompanyDto company;

    public ProfileDtoOutput() {
        //default empty constructor
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }
}
