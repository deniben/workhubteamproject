package com.spring.service.implementations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import javax.annotation.PostConstruct;

import com.spring.enums.ProjectStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.spring.component.UserContext;
import com.spring.dao.ProjectDao;
import com.spring.entity.Company;
import com.spring.entity.FileDocument;
import com.spring.entity.Project;
import com.spring.entity.User;
import com.spring.exception.RestException;
import com.spring.repository.FileDocumentRepository;
import com.spring.service.WorkspaceService;

@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {


	private final PropertiesService propertiesService;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private final ProjectDao projectDao;
	private final UserContext userContext;
	private final FileDocumentRepository fileDocumentRepository;

	public static final Integer MAX_FILE_SIZE = 10485760;
	private static final Integer PAGE_SIZE = 5;

	private static Path DOCUMENTS_DIR;

	@Autowired
	public WorkspaceServiceImpl(PropertiesService propertiesService, ProjectDao projectDao,
	                            UserContext userContext, FileDocumentRepository fileDocumentRepository) {
		this.propertiesService = propertiesService;
		this.projectDao = projectDao;
		this.userContext = userContext;
		this.fileDocumentRepository = fileDocumentRepository;
	}

	@PostConstruct
	public void init() throws IOException {
		DOCUMENTS_DIR = Paths.get(propertiesService.getDocumentsBasePath());
		if(Files.notExists(DOCUMENTS_DIR)) {
			Files.createDirectory(DOCUMENTS_DIR);
		}
	}

	@Override
	public void shareDocument(MultipartFile file, Long projectId) throws IOException {

		if(file == null || projectId == null) {
			throw new RestException("Input file can not be empty");
		}

		if(file.getSize() > MAX_FILE_SIZE) {
			throw new RestException("Maximum file size is 10 megabytes");
		}

		if(fileDocumentRepository.findByFileName(file.getOriginalFilename()) != null) {
			throw new RestException("File with such name already exists");
		}

		Project project = getProjectById(projectId);

		Path projectFolder = Paths.get(DOCUMENTS_DIR.toString() + "/" + project.getName());

		if(Files.notExists(projectFolder)) {
			Files.createDirectory(projectFolder);
		}

		Path fileLocation = Paths.get(projectFolder.toString() + "/" + file.getOriginalFilename());
		Files.copy(file.getInputStream(), fileLocation);

		FileDocument fileDocument = new FileDocument();
		fileDocument.setDate(LocalDate.now());
		fileDocument.setFileName(file.getOriginalFilename());
		fileDocument.setProjectId(projectId);
		fileDocument.setOwnerId(userContext.getCurrentUser().getProfile().getCompany().getId());

		fileDocumentRepository.save(fileDocument);
	}

	@Override
	public Page<FileDocument> getProjectDocuments(Long projectId, Long page) {

		Project project = getProjectById(projectId);

		User user = userContext.getCurrentUser();
		Company company = user.getProfile().getCompany();

		if(!project.getCompanyCreator().getId().equals(company.getId()) &&
				!project.getCompanyEmployee().getId().equals(company.getId())) {
			throw new RestException("Invalid permission", HttpStatus.FORBIDDEN.value());
		}

		return fileDocumentRepository.findByProjectId(projectId, PageRequest.of(page.intValue(), PAGE_SIZE));
	}

	@Override
	public void deleteDocument(String fileName) throws IOException {

		FileDocument fileDocument = fileDocumentRepository.findByFileName(fileName);

		if(fileDocument == null) {
			throw new RestException("File with such name do not exists");
		}

		Project project = getProjectById(fileDocument.getProjectId());

		fileDocumentRepository.delete(fileDocument);

		Path filePath = Paths.get(DOCUMENTS_DIR.toString() + "/" + project.getName() + "/" + fileName);

		if(Files.exists(filePath)) {
			Files.delete(filePath);
		}

	}

	@Override
	public byte[] download(String fileName) {
		FileDocument fileDocument = fileDocumentRepository.findByFileName(fileName);

		if(fileDocument == null) {
			throw new RestException("File with such name do not exists");
		}

		Project project = getProjectById(fileDocument.getProjectId());

		Path path = Paths.get(DOCUMENTS_DIR + "/" + project.getName() + "/" + fileName);

		if(Files.notExists(path)) {
			throw new RestException("Inconsistency error. File data was lost");
		}

		try {
			return Files.readAllBytes(path);
		} catch(IOException e) {
			LOGGER.error(e.getMessage());
			throw new RestException("Can not read file");
		}
	}

	private Project getProjectById(Long id) {
		Project project = projectDao.findById(id).get();

		if(project == null) {
			throw new RestException("Project with such id do not exists");
		}

		if(!project.getStatus().equals(ProjectStatus.IN_PROGRESS)) {
			throw new RestException("Workspace for this project do not exists");
		}

		return project;
	}

}
