package com.spring.service;

import static org.testng.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.spring.dto.RegistrationDto;
import com.spring.service.implementations.ValidationService;


public class ValidationServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    ValidationService validationService;

    @Test
    public void testRegistrationValidation() {

        RegistrationDto registrationDto = new RegistrationDto();

        registrationDto.setUsername("test@mail.com");
        registrationDto.setPassword("123456");
        registrationDto.setRepeatedPassword("123456");

        validationService.registrationValidation(registrationDto);

        assertTrue(true);

    }

    @Test
    public void testPasswordValidation() {

        RegistrationDto registrationDto = new RegistrationDto();

        registrationDto.setUsername("test@mail.com");

        registrationDto.setPassword("123");
        registrationDto.setRepeatedPassword("123");

        validationService.registrationValidation(registrationDto);
        assertTrue(true);

        registrationDto.setPassword("1234567");
        registrationDto.setRepeatedPassword("1234456");

        validationService.registrationValidation(registrationDto);

        assertTrue(true);

        registrationDto.setPassword(null);
        registrationDto.setRepeatedPassword(null);

    }

    @Test
    public void testEmailValidation() {

        RegistrationDto registrationDto = new RegistrationDto();

        registrationDto.setUsername("fdafa");
        registrationDto.setPassword("123456");
        registrationDto.setRepeatedPassword("123456");

        validationService.registrationValidation(registrationDto);

        assertTrue(true);

        registrationDto.setUsername("michaeller.2012@gmail.com");

        validationService.registrationValidation(registrationDto);

        assertTrue(true);

    }

}
