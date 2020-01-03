package com.spring.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.spring.dto.CompanyDto;
import com.spring.dto.DocumentDto;
import com.spring.entity.Company;
import com.spring.service.CompanyService;
import com.spring.service.ProfileService;
import com.spring.utils.mapper.CompanyMapper;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.dto.EventDto;
import com.spring.dto.PageableResponse;
import com.spring.entity.FileDocument;
import com.spring.service.EventsService;
import com.spring.service.WorkspaceService;
import com.spring.utils.mapper.DocumentMapper;
import com.spring.utils.mapper.EventsMapper;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

	private final WorkspaceService workspaceService;
	private final EventsService eventsService;
	private final EventsMapper eventsMapper;
	private final DocumentMapper documentMapper;
	private final Tika tika;
	private final CompanyMapper companyMapper;
	private final ProfileService profileService;
	private final CompanyService companyService;
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkspaceController.class);

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService, EventsService eventsService, EventsMapper eventsMapper,
                               DocumentMapper documentMapper, Tika tika, CompanyMapper companyMapper,
                               ProfileService profileService, CompanyService companyService) {
        this.workspaceService = workspaceService;
        this.eventsService = eventsService;
        this.eventsMapper = eventsMapper;
        this.documentMapper = documentMapper;
        this.tika = tika;
        this.companyMapper = companyMapper;
        this.profileService = profileService;
        this.companyService = companyService;
    }

    @PostMapping("/documents")
    public ResponseEntity<String> shareDocument(MultipartFile multipartFile, Long projectId) {
        LOGGER.debug("Sharing file with name : [{}] for project with id [{}]", multipartFile.getOriginalFilename(), projectId);
        try {
            workspaceService.shareDocument(multipartFile, projectId);
        } catch (IOException io) {
            LOGGER.debug(io.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body("File management error. Please try later");
        }
        return ResponseEntity.ok("Successfully shared document");
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<PageableResponse> getProjectDocuments(@PathVariable Long id, Long page) {
	    LOGGER.debug("in getProjectDocuments(), id: {}, page: {}", id, page);
	    Page<FileDocument> documents = workspaceService.getProjectDocuments(id, page);
        List<DocumentDto> documentDtosList = new ArrayList<>();
        for (FileDocument fileDocument : documents) {
            DocumentDto documentDto = documentMapper.toDto(fileDocument);

            if (fileDocument.getOwnerId() != null) {
                Company company = companyService.findCompanyById(fileDocument.getOwnerId());
                CompanyDto companyOwnerDto = companyMapper.toDtoIgnoreSkills(company);
                documentDto.setCompanyOwner(companyOwnerDto);
            }
            documentDto.setOwner(profileService.isUserDocumentOwner(fileDocument));
            documentDtosList.add(documentDto);
        }

        return ResponseEntity.ok(new PageableResponse(documents.getTotalPages(), documentDtosList));
    }

	@GetMapping("/download/documents")
	public ResponseEntity<byte[]> downloadDocument(String name) {
		LOGGER.debug("in downloadDocument(), name: {}", name);
		return ResponseEntity.ok()
				.header("filename", name)
				.header("Content-Type", tika.detect(name))
				.body(workspaceService.download(name));
	}

    @DeleteMapping("/documents")
    public ResponseEntity<String> deleteDocument(String fileName) {
	    LOGGER.debug("in deleteDocument(), fileName: {}", fileName);
	    try {
            workspaceService.deleteDocument(fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body("File management error. Please try later");
        }
        return ResponseEntity.ok("File was successfully deleted");
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<List<EventDto>> getProjectEvents(@PathVariable Long id, Optional<Long> month) {
	    LOGGER.debug("in getProjectEvents(), id: {}, month: {}", id, month);
	    return ResponseEntity.ok(eventsService.getAll(id, month).stream().map(eventsMapper::toDto).collect(Collectors.toList()));
    }

    @PostMapping("/events")
    public ResponseEntity<String> createEvent(@RequestBody EventDto eventDto) {
	    LOGGER.debug("in createEvent(), eventDto: {}", eventDto);
	    eventsService.createEvent(eventsMapper.toEntity(eventDto));
        return ResponseEntity.ok("Event was successfully created");
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable String id) {
	    LOGGER.debug("in deleteEvent(), id: {}", id);
	    eventsService.deleteEvent(id);
        return ResponseEntity.ok("Event was successfully deleted");
    }

}
