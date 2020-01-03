package com.spring.service;

import com.spring.component.UserContext;
import com.spring.dao.CompanyDao;
import com.spring.dao.ProfileDao;
import com.spring.entity.Company;
import com.spring.entity.Profile;
import com.spring.entity.Skill;
import com.spring.entity.User;
import com.spring.enums.CompanyType;
import com.spring.service.implementations.CompanyServiceImpl;
import com.spring.service.implementations.JoinCompanyServiceImpl;
import com.spring.service.implementations.MailService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JoinCompanyServiceTest {
  @Captor
  ArgumentCaptor<Profile> argCaptor;

  @Captor
  ArgumentCaptor<Company> compCaptor;

  @InjectMocks
  JoinCompanyServiceImpl joinCompanyService;

  @Mock
  CompanyDao companyDao;

  @Mock
  Company company;

  @Mock
  ProfileDao profileDao;

  @Mock
  MailService mailService;

  @Mock
  UserContext userContext;

  @Spy
  User userSpy;

  @Spy
  User userSpyApply;

  @Spy
  Profile profileSpy;

  @Spy
  Profile profileSpyApply;

  @Spy
  Company companySpy;

  @Spy
  Skill skillSpy;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void acceptMemberTest() {
    Long userId = 300L;
    Long userIdMember = 200L;

    userSpy.setUsername("companyUser");
    userSpy.setId(userIdMember);

    companySpy.setId(2L);

    profileSpy.setAccepted(true);
    profileSpy.setId(userId);
    profileSpy.setCompany(companySpy);

    Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

    userSpyApply.setUsername("AnotherUser");
    userSpyApply.setId(userId);

    profileSpyApply.setAccepted(false);
    profileSpyApply.setId(userId);
    profileSpyApply.setCompany(companySpy);

    Mockito.when(profileSpy.getCompany()).thenReturn(companySpy);
    Mockito.when(profileDao.findById(anyLong())).thenReturn(Optional.of(profileSpy));
    Mockito.when(userSpy.getProfile()).thenReturn(profileSpy);

    argCaptor = ArgumentCaptor.forClass(Profile.class);
    joinCompanyService.acceptMembership(userId);

    verify(profileDao, times(1)).save(argCaptor.capture());

    Profile profile = argCaptor.getValue();
    assertEquals("User wasn't join to the company", true, profile.getAccepted());
  }

  @Test
  public void rejectMemberTest() {
    Long userId = 300L;
    Long userIdMember = 200L;

    userSpy.setUsername("companyUser");
    userSpy.setId(userIdMember);

    companySpy.setId(2L);

    profileSpy.setAccepted(true);
    profileSpy.setId(userId);
    profileSpy.setCompany(companySpy);

    Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

    userSpyApply.setUsername("AnotherUser");
    userSpyApply.setId(userId);

    profileSpyApply.setAccepted(true);
    profileSpyApply.setId(userId);
    profileSpyApply.setCompany(companySpy);

    Mockito.when(profileSpy.getCompany()).thenReturn(companySpy);
    Mockito.when(profileDao.findById(anyLong())).thenReturn(Optional.of(profileSpy));
    Mockito.when(userSpy.getProfile()).thenReturn(profileSpy);

    argCaptor = ArgumentCaptor.forClass(Profile.class);
    joinCompanyService.acceptMembership(userId);

    verify(profileDao, times(1)).save(argCaptor.capture());

    Profile profile = argCaptor.getValue();
    assertEquals("User wasn't join to the company", true, profile.getAccepted());
  }

  @Test
  public void joinCompanyTest() {
    Long userId = 300L;
    Long companyId = 200L;

    userSpy.setUsername("companyUser");
    userSpy.setId(userId);

    Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

    profileSpy.setAccepted(true);
    profileSpy.setId(userId);

    Mockito.when(userSpy.getProfile()).thenReturn(profileSpy);
    companySpy.setId(companyId);
    companySpy.setName("TestCompany");

    Mockito.when(companyDao.findByName(any())).thenReturn(java.util.Optional.of(companySpy));

    argCaptor = ArgumentCaptor.forClass(Profile.class);
    joinCompanyService.joinCompany(companySpy.getName());

    verify(profileDao, times(1)).save(argCaptor.capture());

    Profile profile = argCaptor.getValue();
    assertEquals("User wasn't join to the company", companySpy, profile.getCompany());
  }

}
