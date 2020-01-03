package com.spring.service;

import org.springframework.web.multipart.MultipartFile;

import com.spring.entity.Photo;
import com.spring.enums.PhotoType;

public interface PhotosService {
	void downloadPhoto(String photoName);
	boolean isPhotoExist(String photoName);
	String getPhoto(String url);
	Photo uploadPhoto(MultipartFile multipartFile, PhotoType typeOfPhoto);
	String getUniqueFileName(PhotoType typeOfPhoto);
}
