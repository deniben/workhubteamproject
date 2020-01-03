package com.spring.service;

import com.spring.component.UserContext;
import com.spring.dao.CompanyDao;
import com.spring.dao.ProfileDao;
import com.spring.entity.Company;
import com.spring.entity.Profile;
import com.spring.entity.Skill;
import com.spring.entity.User;
import com.spring.enums.CompanyType;
import com.spring.exception.RestException;
import com.spring.service.implementations.CompanyServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class CompanyServiceTest {
    public static final String COMPANY_NOT_FOUND_MESSAGE = "Company with '%s' id was not found";

    @Captor
    ArgumentCaptor<Company> argCaptor;

    @InjectMocks
    CompanyServiceImpl companyService;

    @Mock
    CompanyDao companyDao;

    @Mock
    ProfileDao profileDao;

    @Mock
    UserContext userContext;

    @Spy
    User userSpy;

    @Spy
    Profile profileSpy;

    @Spy
    Profile otherProfileSpy;

    @Spy
    Company companySpy;

    @Spy
    Company oldCompany;

    @Spy
    Skill skillSpy;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCompany() {
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        profileSpy.setId(20L);
        profileSpy.setFirstName("test");
        profileSpy.setLastName("test");
        profileSpy.setNickname("test");
        userSpy.setProfile(profileSpy);

        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));

        companyService.saveCompany(companySpy);
        verify(companyDao, times(1)).saveAndReturn(companySpy);
        verify(profileDao, times(1)).save(profileSpy);
        verifyNoMoreInteractions(companyDao, profileDao);
    }

    @Test
    public void testCreateCompanyNegative() {
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));

        try {
            companyService.saveCompany(companySpy);
            fail("Exception was not thrown");
        } catch (Exception exc) {
            assertTrue("Wrong exception type was found. Expected RestException, but found:" + exc.getClass(),
                    exc instanceof RestException);
            assertEquals("Wrong exception message was found",
                    String.format("Unable to find user profile for user with '%s' id", userSpy.getId()), exc.getMessage());
        }

//        BDDCatchException.when(() -> companyService.saveCompany(companySpy));
//        then(caughtException()).isInstanceOf(RestException.class);

        verify(profileDao, never()).save(profileSpy);
        verify(companyDao, never()).save(companySpy);
    }

    @Test
    public void testBlockCompany() {
        Long companyId = 200L;
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);

        profileSpy.setId(20L);
        profileSpy.setFirstName("test");
        profileSpy.setLastName("test");
        profileSpy.setNickname("test");
//        profileSpy.setCompany(companySpy);
        userSpy.setProfile(profileSpy);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        companySpy.setId(companyId);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setBlocked(false);
        companySpy.setOwner(profileSpy);

        Mockito.when(companyDao.findById(anyLong())).thenReturn(Optional.of(companySpy));

        argCaptor = ArgumentCaptor.forClass(Company.class);
        companyService.blockUnblockCompany(companyId, true);

        verify(companyDao, times(2)).findById(companyId);
        verify(companyDao, times(1)).save(argCaptor.capture());

        Company returnedCompany = argCaptor.getValue();
        assertEquals("Company  was't blocked", true, returnedCompany.isBlocked());
    }

    @Test
    public void testUpdateCompany() {
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        userSpy.setProfile(profileSpy);

        profileSpy.setId(20L);
        profileSpy.setFirstName("test");
        profileSpy.setLastName("test");
        profileSpy.setNickname("test");
        profileSpy.setUser(userSpy);
        Mockito.when(userSpy.getProfile()).thenReturn(profileSpy);

        companySpy.setId(25L);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setPhotoUrl("214238428675");
        companySpy.setOwner(profileSpy);
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));


        oldCompany.setId(25L);
        oldCompany.setName("Old data");
        oldCompany.setDescription("Old data");
        oldCompany.setPhotoUrl("dahjdfkhaskljaf");
        oldCompany.setOwner(profileSpy);
        argCaptor = ArgumentCaptor.forClass(Company.class);
        Mockito.when(companyDao.findById(companySpy.getId())).thenReturn(Optional.of(oldCompany));

        companyService.updateCompany(companySpy);
        verify(companyDao, times(1)).save(argCaptor.capture());
        verify(companyDao, times(1)).findById(companySpy.getId());
        verifyNoMoreInteractions(companyDao, profileDao);

        Company returnedCompany = argCaptor.getValue();
        assertEquals("Names were not equal", companySpy.getName(), returnedCompany.getName());
        assertEquals("Descriptions were not equal", companySpy.getDescription(), returnedCompany.getDescription());
        assertEquals("Photos were not equal", companySpy.getPhotoUrl(), returnedCompany.getPhotoUrl());
        assertEquals("Skills were not equal", companySpy.getProposedSkills(), returnedCompany.getProposedSkills());

    }

    @Test
    public void testUpdateCompanyNegativeWithOldCompany() {
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        userSpy.setProfile(profileSpy);

        profileSpy.setId(20L);
        profileSpy.setFirstName("test");
        profileSpy.setLastName("test");
        profileSpy.setNickname("test");
        profileSpy.setUser(userSpy);
        Mockito.when(userSpy.getProfile()).thenReturn(profileSpy);

        companySpy.setId(25L);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setPhotoUrl("214238428675");
        companySpy.setOwner(profileSpy);
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));


        oldCompany.setId(25L);
        oldCompany.setName("Old data");
        oldCompany.setDescription("Old data");
        oldCompany.setPhotoUrl("dahjdfkhaskljaf");
        oldCompany.setOwner(profileSpy);
        argCaptor = ArgumentCaptor.forClass(Company.class);
        Mockito.when(companyDao.findById(companySpy.getId())).thenReturn(Optional.empty());

        try {
            companyService.updateCompany(companySpy);
            fail("Exception was not thrown");
        } catch (Exception exc) {
            assertTrue("Wrong exception type was found. Expected RestException, but found: " + exc.getClass(),
                    exc instanceof RestException);
            assertEquals("Wrong exception message was found",
                    String.format(COMPANY_NOT_FOUND_MESSAGE, companySpy.getId()), exc.getMessage());
        }

        verify(companyDao, times(1)).findById(companySpy.getId());
        verify(companyDao, never()).save(argCaptor.capture());
    }

    @Test
    public void testUpdateCompanyNegativeWithNullProfile() {
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        userSpy.setProfile(profileSpy);

        profileSpy.setId(20L);
        profileSpy.setFirstName("test");
        profileSpy.setLastName("test");
        profileSpy.setNickname("test");
        profileSpy.setUser(userSpy);
        Mockito.when(userSpy.getProfile()).thenReturn(null);

        companySpy.setId(25L);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setPhotoUrl("214238428675");
        companySpy.setOwner(profileSpy);
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));


        oldCompany.setId(25L);
        oldCompany.setName("Old data");
        oldCompany.setDescription("Old data");
        oldCompany.setPhotoUrl("dahjdfkhaskljaf");
        oldCompany.setOwner(profileSpy);
        argCaptor = ArgumentCaptor.forClass(Company.class);
        Mockito.when(companyDao.findById(companySpy.getId())).thenReturn(Optional.of(oldCompany));

        try {
            companyService.updateCompany(companySpy);
            fail("Exception was not thrown");
        } catch (Exception exc) {
            assertTrue("Wrong exception type was found", exc instanceof RestException);
            assertEquals("Wrong exception message was found", exc.getMessage(),
                    String.format("Unable to find user profile for user with '%s' id", userSpy.getId()));
        }

        verify(companyDao, never()).save(argCaptor.capture());
    }

    @Test
    public void testUpdateCompanyNegativeWithOtherProfile() {
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        userSpy.setProfile(profileSpy);

        otherProfileSpy.setId(223L);
        otherProfileSpy.setFirstName("Other profile");
        otherProfileSpy.setLastName("Other profile");
        otherProfileSpy.setNickname("Other profile");

        profileSpy.setId(20L);
        profileSpy.setFirstName("test");
        profileSpy.setLastName("test");
        profileSpy.setNickname("test");
        profileSpy.setUser(userSpy);
        Mockito.when(userSpy.getProfile()).thenReturn(profileSpy);

        companySpy.setId(25L);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setPhotoUrl("214238428675");
        companySpy.setOwner(profileSpy);
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));


        oldCompany.setId(25L);
        oldCompany.setName("Old data");
        oldCompany.setDescription("Old data");
        oldCompany.setPhotoUrl("dahjdfkhaskljaf");
        oldCompany.setOwner(otherProfileSpy);
        argCaptor = ArgumentCaptor.forClass(Company.class);
        Mockito.when(companyDao.findById(companySpy.getId())).thenReturn(Optional.of(oldCompany));

        try {
            companyService.updateCompany(companySpy);
            fail("Exception was not thrown");
        } catch (Exception exc) {
            assertTrue("Wrong exception type was found", exc instanceof RestException);
            assertEquals("Wrong exception message was found", exc.getMessage(),
                    String.format("This profile do not have permission to update this company. OwnerId: %s, ProfileId: %s",
                            otherProfileSpy.getId(), profileSpy.getId()));
        }

        verify(companyDao, times(1)).findById(companySpy.getId());
        verify(companyDao, never()).save(argCaptor.capture());
    }

    @Test
    public void testUpdateCompanyNegativeWhenCompanyIsBlocked() {
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        userSpy.setProfile(profileSpy);

        profileSpy.setId(20L);
        profileSpy.setFirstName("test");
        profileSpy.setLastName("test");
        profileSpy.setNickname("test");
        profileSpy.setUser(userSpy);
        Mockito.when(userSpy.getProfile()).thenReturn(profileSpy);

        companySpy.setId(25L);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setPhotoUrl("214238428675");
        companySpy.setOwner(profileSpy);
        companySpy.setBlocked(true);
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));


        oldCompany.setId(25L);
        oldCompany.setName("Old data");
        oldCompany.setDescription("Old data");
        oldCompany.setPhotoUrl("dahjdfkhaskljaf");
        oldCompany.setOwner(profileSpy);
        argCaptor = ArgumentCaptor.forClass(Company.class);
        Mockito.when(companyDao.findById(companySpy.getId())).thenReturn(Optional.of(oldCompany));

        companyService.updateCompany(companySpy);

        verify(companyDao, times(1)).findById(companySpy.getId());
        verify(companyDao, times(1)).save(argCaptor.capture());
        verifyNoMoreInteractions(companyDao, profileDao);

        Company returnedCompany = argCaptor.getValue();
        assertEquals("state isBlocked was not equal", companySpy.isBlocked(), returnedCompany.isBlocked());
    }

    @Test
    public void findCompanyById() {
        companySpy.setId(25L);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setPhotoUrl("214238428675");
        companySpy.setOwner(profileSpy);
        companySpy.setBlocked(true);
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));

        Mockito.when(companyDao.findById(companySpy.getId())).thenReturn(Optional.of(companySpy));
        companyService.findCompanyById(companySpy.getId());

        verify(companyDao, times(1)).findById(companySpy.getId());
        verifyNoMoreInteractions(companyDao, profileDao);
    }

    @Test
    public void findCompanyByIdNegativeWithNullCompanyFromDb() {
        companySpy.setId(25L);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setPhotoUrl("214238428675");
        companySpy.setOwner(profileSpy);
        companySpy.setBlocked(true);
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));

        Mockito.when(companyDao.findById(companySpy.getId())).thenReturn(Optional.empty());
        try {
            companyService.findCompanyById(companySpy.getId());
            fail("Exception was not thrown");
        } catch (Exception exc) {
            assertTrue("Wrong exception type was found. Expected RestException, but found: " + exc.getClass(), exc instanceof RestException);
            assertEquals("Wrong exception message was found", String.format(COMPANY_NOT_FOUND_MESSAGE, companySpy.getId()), exc.getMessage());
        }

        verify(companyDao, times(1)).findById(companySpy.getId());
    }

    @Test
    public void findCompanyByIdCheckOwner() {
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        userSpy.setProfile(profileSpy);

        profileSpy.setId(20L);
        profileSpy.setFirstName("test");
        profileSpy.setLastName("test");
        profileSpy.setNickname("test");
        profileSpy.setUser(userSpy);
        profileSpy.setCompany(companySpy);
        Mockito.when(userSpy.getProfile()).thenReturn(profileSpy);

        companySpy.setId(25L);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setPhotoUrl("214238428675");
        companySpy.setOwner(profileSpy);
        companySpy.setBlocked(true);
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));

        Mockito.when(companyDao.findById(companySpy.getId())).thenReturn(Optional.of(companySpy));

        companyService.findCompanyByIdCheckOwner(companySpy.getId());

        verify(companyDao, times(1)).findById(companySpy.getId());
        verifyNoMoreInteractions(companyDao, profileDao);
    }

    @Test
    public void findCompanyByIdCheckOwnerWithNullCompanyFromDb() {
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        userSpy.setProfile(profileSpy);

        profileSpy.setId(20L);
        profileSpy.setFirstName("test");
        profileSpy.setLastName("test");
        profileSpy.setNickname("test");
        profileSpy.setUser(userSpy);
        profileSpy.setCompany(companySpy);
        Mockito.when(userSpy.getProfile()).thenReturn(profileSpy);

        companySpy.setId(25L);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setPhotoUrl("214238428675");
        companySpy.setOwner(profileSpy);
        companySpy.setBlocked(true);
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));

        Mockito.when(companyDao.findById(companySpy.getId())).thenReturn(Optional.empty());
        try {
            companyService.findCompanyByIdCheckOwner(companySpy.getId());
            fail("Exception was not thrown");
        } catch (Exception exc) {
            assertTrue("Wrong exception type was found. Expected RestException, but found: " + exc.getClass(), exc instanceof RestException);
            assertEquals("Wrong exception message was found",
                    String.format(COMPANY_NOT_FOUND_MESSAGE, companySpy.getId()), exc.getMessage());
        }

        verify(companyDao, times(1)).findById(companySpy.getId());
        verifyNoMoreInteractions(companyDao, profileDao);
    }

    @Test
    public void findCompanyByIdCheckOwnerWithNullCompanyProfile() {
        userSpy.setUsername("companyUser");
        userSpy.setId(300L);
        Mockito.when(userContext.getCurrentUser()).thenReturn(userSpy);

        userSpy.setProfile(profileSpy);

        profileSpy.setId(20L);
        profileSpy.setFirstName("test");
        profileSpy.setLastName("test");
        profileSpy.setNickname("test");
        profileSpy.setUser(userSpy);
//		profileSpy.setCompany(companySpy); profile don't have company
        Mockito.when(userSpy.getProfile()).thenReturn(profileSpy);

        companySpy.setId(25L);
        companySpy.setName("TestCompany");
        companySpy.setType(CompanyType.EMPLOYEE);
        companySpy.setDescription("Test company description");
        companySpy.setPhotoUrl("214238428675");
        companySpy.setOwner(profileSpy);
        companySpy.setBlocked(true);
        skillSpy.setId(2L);
        skillSpy.setName("test skill");
        companySpy.setProposedSkills(Collections.singleton(skillSpy));

        Mockito.when(companyDao.findById(companySpy.getId())).thenReturn(Optional.of(companySpy));
        try {
            companyService.findCompanyByIdCheckOwner(companySpy.getId());
            fail("Exception was not thrown");
        } catch (Exception exc) {
            assertTrue("Wrong exception type was found", exc instanceof RestException);
            assertEquals("Wrong exception message was found",
                    "Company was not found OR you don't have permission", exc.getMessage());
        }

        verify(companyDao, times(1)).findById(companySpy.getId());
        verifyNoMoreInteractions(companyDao, profileDao);
    }

}
