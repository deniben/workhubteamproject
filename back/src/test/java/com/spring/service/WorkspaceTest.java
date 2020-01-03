package com.spring.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.spring.component.UserContext;
import com.spring.dao.ProjectDao;
import com.spring.entity.Company;
import com.spring.entity.FileDocument;
import com.spring.entity.Profile;
import com.spring.entity.Project;
import com.spring.entity.User;
import com.spring.enums.ProjectStatus;
import com.spring.exception.RestException;
import com.spring.service.implementations.PropertiesService;
import com.spring.service.implementations.WorkspaceServiceImpl;
import com.spring.repository.FileDocumentRepository;
import com.spring.test.objects.MultipartTestFile;

public class WorkspaceTest {

	private WorkspaceService workspaceService;

	@Mock
	PropertiesService propertiesService;

	@Mock
	ProjectDao projectDao;

	@Mock
	UserContext userContext;

	@Mock
	FileDocumentRepository fileDocumentRepository;

	private static final String FILE_NAME = "hello.world";
	private static final Long NEW_PROJECT_ID = (long) 0;
	private static final Long IN_PROGRESS_PROJECT_ID = (long) 1;
	private static final Long NULL_PROJECT_ID = (long) 2;
	private static final Long COMPANY_ID = (long) 0;
	private static final String TEST_DOCUMENTS_PATH = "C:/Users/mpala/Documents";

	@BeforeClass
	public void init() throws IOException {

		MockitoAnnotations.initMocks(this);

		Mockito.when(fileDocumentRepository.findByFileName(FILE_NAME)).thenReturn(getFileDocument());
		Mockito.when(projectDao.findById(IN_PROGRESS_PROJECT_ID)).thenReturn(Optional.of(getProjectInProgress()));
		Mockito.when(projectDao.findById(NEW_PROJECT_ID)).thenReturn(Optional.of(getProjectNew()));
		Mockito.when(projectDao.findById(NULL_PROJECT_ID)).thenReturn(null);
		Mockito.when(userContext.getCurrentUser()).thenReturn(getCurrentUser());
		Mockito.when(propertiesService.getDocumentsBasePath()).thenReturn(TEST_DOCUMENTS_PATH);

		this.workspaceService = new WorkspaceServiceImpl(propertiesService, projectDao, userContext, fileDocumentRepository);
		this.workspaceService.init();
	}

	@Test
	public void shareDocument() throws IOException {

		MultipartTestFile file = getTestFile();
		Path filePath = Paths.get(TEST_DOCUMENTS_PATH + "/" + getProjectInProgress().getName() + "/" + file.getOriginalFilename());

		if(Files.exists(filePath)) {
			Files.delete(filePath);
		}

		workspaceService.shareDocument(getTestFile(), IN_PROGRESS_PROJECT_ID);
		Assert.assertTrue(true);
	}

	@Test(expectedExceptions = RestException.class)
	public void shareEmptyDocument() throws IOException {
		workspaceService.shareDocument(null, IN_PROGRESS_PROJECT_ID);
	}

	@Test(expectedExceptions = RestException.class)
	public void shareLargeDocument() throws IOException {
		MultipartTestFile testFile = new MultipartTestFile("test.file", WorkspaceServiceImpl.MAX_FILE_SIZE + (long) 1);
		workspaceService.shareDocument(testFile, IN_PROGRESS_PROJECT_ID);
	}

	@Test(expectedExceptions = RestException.class)
	public void shareExistingDocument() throws IOException {
		MultipartTestFile testFile = new MultipartTestFile(FILE_NAME, (long) 1024);
		workspaceService.shareDocument(testFile, IN_PROGRESS_PROJECT_ID);
	}

	@Test(expectedExceptions = RestException.class)
	public void shareToNewProject() throws IOException {
		MultipartTestFile testFile = new MultipartTestFile("test.txt", (long) 313);
		workspaceService.shareDocument(testFile, NEW_PROJECT_ID);
	}

	@Test(expectedExceptions = RestException.class)
	public void shareToNullProject() throws IOException {
		MultipartTestFile testFile = new MultipartTestFile("test.txt", (long) 222);
		workspaceService.shareDocument(testFile, NULL_PROJECT_ID);
	}

	@Test(expectedExceptions = RestException.class)
	public void deleteNullDocument() throws IOException {
		workspaceService.deleteDocument(null);
	}

	@Test
	public void deleteDocument() throws IOException {
		String fileName = "test.txt";
		Mockito.when(fileDocumentRepository.findByFileName(fileName)).thenReturn(getFileDocument());
		workspaceService.deleteDocument(fileName);
		Assert.assertTrue(true);
	}

	@Test
	public void getProjectDocuments() throws IOException {
		workspaceService.getProjectDocuments(IN_PROGRESS_PROJECT_ID, (long) 0);
		Assert.assertTrue(true);
	}

	@Test(expectedExceptions = RestException.class)
	public void getProjectDocumentsWithoutPermission() throws IOException {
		Mockito.when(projectDao.findById(IN_PROGRESS_PROJECT_ID)).thenReturn(Optional.of(getProjectInProgress(COMPANY_ID + 2)));
		workspaceService.getProjectDocuments(IN_PROGRESS_PROJECT_ID, (long) 0);
	}

	@Test(expectedExceptions = RestException.class)
	public void downloadNullDocument() {
		workspaceService.download(null);
	}

	@Test(expectedExceptions = RestException.class)
	public void downloadWithWrongPath() {
		String fileName = UUID.randomUUID().toString();
		workspaceService.download(fileName);
	}

	public User getCurrentUser() {
		User currentUser = new User();
		Profile profile = new Profile();
		Company company = new Company();

		company.setId(COMPANY_ID);
		profile.setCompany(company);
		currentUser.setProfile(profile);

		return currentUser;
	}

	public MultipartTestFile getTestFile() {

		try {
			String fileName = "test_file.txt";
			String filePath = TEST_DOCUMENTS_PATH + "/" + fileName;

			File file = new File(filePath);

			if(!file.exists()) {
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write("Testing data".getBytes());
				fileOutputStream.close();
			}

			MultipartTestFile multipartTestFile = new MultipartTestFile(fileName, new File(filePath).length());
			multipartTestFile.setInputStream(new FileInputStream(filePath));

			return multipartTestFile;
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public FileDocument getFileDocument() {
		FileDocument fileDocument = new FileDocument();
		fileDocument.setId(UUID.randomUUID().toString());
		fileDocument.setDate(LocalDate.now());
		fileDocument.setFileName(FILE_NAME);
		fileDocument.setOwnerId((long) 0);
		fileDocument.setProjectId(IN_PROGRESS_PROJECT_ID);
		return fileDocument;
	}

	public Project getProjectNew() {
		Project newProject = new Project();
		newProject.setId(NEW_PROJECT_ID);
		newProject.setStatus(ProjectStatus.NEW);
		return newProject;
	}

	public Project getProjectInProgress() {
		return getProjectInProgress(COMPANY_ID);
	}

	public Project getProjectInProgress(Long companyId) {
		Project inProgressProject = new Project();
		inProgressProject.setId(IN_PROGRESS_PROJECT_ID);
		inProgressProject.setName("WorkHub");
		inProgressProject.setStatus(ProjectStatus.IN_PROGRESS);

		Company companyCreator = new Company();
		companyCreator.setId(COMPANY_ID + 1);

		Company companyEmployee = new Company();
		companyEmployee.setId(companyId);

		inProgressProject.setCompanyCreator(companyCreator);
		inProgressProject.setCompanyEmployee(companyEmployee);
		return inProgressProject;
	}

}
