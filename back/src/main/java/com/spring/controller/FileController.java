package com.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.enums.PhotoType;
import com.spring.service.PhotosService;


@RestController
@RequestMapping("/download-file/type")
public class FileController {

	private PhotosService photosService;

	private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

	@Autowired
	public FileController(PhotosService photosService) {
		this.photosService = photosService;
	}

	@PostMapping("/{type}")
	public ResponseEntity downloadFile(@RequestParam("file") MultipartFile multipartFile, @PathVariable PhotoType type) {
		LOGGER.debug("in FileController downloadFile({}, {})", multipartFile, type);
		return ResponseEntity.ok(photosService.uploadPhoto(multipartFile, type));
	}
}
