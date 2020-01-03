package com.spring.service.implementations;

import com.spring.service.PhotosService;
import com.spring.component.UserContext;
import com.spring.entity.Photo;
import com.spring.enums.PhotoType;
import com.spring.exception.OtherException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class PhotosServiceImpl implements PhotosService {

    private String basePath;

    private Photo photo = new Photo();

    private static final String PHOTO_FORMAT = "jpg";

    private static final String BASE_PREFIX = "data:image/png;base64,";

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private ValidationService validationService;

    private UserContext user;

    @Autowired
    public PhotosServiceImpl(PropertiesService propertiesService, ValidationService validationService, UserContext user) {
        this.basePath = propertiesService.getPhotosBasePath();
        this.validationService = validationService;
        this.user = user;
    }

    @Override
    public String getPhoto(String url) {
        LOGGER.debug("in PhotoServiceImpl getPhoto({})", url);
        final String DIRECTORY_SEPARATOR = "/";
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(basePath + DIRECTORY_SEPARATOR + url));
            String base64 = new String(Base64.getEncoder().encode(bytes));
            return BASE_PREFIX + base64;
        } catch (Exception e) {
            throw new OtherException(e.getMessage(), e);
        }

    }

    @Override
    public Photo uploadPhoto(MultipartFile multipartFile, PhotoType typeOfPhoto) {
        LOGGER.debug("in PhotoServiceImpl uploadPhoto({},{})", multipartFile, typeOfPhoto);
        File uploadedFile;
        if (!multipartFile.isEmpty()) {
            try {
                StringBuilder fileName = new StringBuilder(getUniqueFileName(typeOfPhoto));
                photoMapper(multipartFile, fileName);
                validationService.photoValidation(photo);
                uploadedFile = uploadFile(basePath, fileName.toString());
                writeFileIntoDirectory(uploadedFile, multipartFile.getBytes());
            } catch (IOException e) {
                throw new OtherException("Failed to upload " + multipartFile.getOriginalFilename() + " " + e.getMessage());
            }
        }
        return photo;
    }

    @Override
    public boolean isPhotoExist(String photoName) {
        LOGGER.debug("in PhotoServiceImpl isPhotoExist({})", photoName);
        StringBuilder path = new StringBuilder(basePath);
        path.append(File.separator).append(photoName);
        boolean isExist = new File(path.toString()).exists();
        return isExist;
    }

    @Override
    public void downloadPhoto(String photoName) {
        LOGGER.debug("in PhotoServiceImpl downloadPhoto({})", photoName);
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(photoName));
            ImageIO.write(bufferedImage, PHOTO_FORMAT, new File(basePath + File.separator + getUniqueFileName(PhotoType.PROFILE)));
        } catch (IOException e) {
            throw new OtherException(e.getMessage(), e);
        }
    }

    public String getUniqueFileName(PhotoType typeOfPhoto) {
        LOGGER.debug("in PhotoServiceImpl uniqueFileName({})", typeOfPhoto);
        final String WORD_SEPARATOR = "_";
        final String DOT_BEFORE_FILE_FORMAT = ".";
        final String INVALID_SYMBOLS = "[@.]";
        StringBuilder fileName = new StringBuilder(typeOfPhoto.name());
        fileName.append(WORD_SEPARATOR).append(String.join(WORD_SEPARATOR, user.getCurrentUser().
                getUsername().split(INVALID_SYMBOLS)));
        if (Objects.equals(typeOfPhoto, PhotoType.PROJECT)) {
            fileName.append(generateNameForPhoto());
        }
        fileName.append(DOT_BEFORE_FILE_FORMAT).append(PHOTO_FORMAT);
        return fileName.toString();
    }

    private String generateNameForPhoto() {
        LOGGER.trace("in PhotoServiceImpl generateNameForPhoto()");
        final int GENERATED_STRING_LENGTH = 5;
        final SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        char[] alphabet = IntStream.rangeClosed('A', 'Z')
                .mapToObj(c -> "" + (char) c).collect(Collectors.joining()).toCharArray();
        for (int i = 0; i < GENERATED_STRING_LENGTH; ++i) {
            sb.append(alphabet[(random.nextInt(GENERATED_STRING_LENGTH))]);
        }
        return sb.toString();
    }

    private void photoMapper(MultipartFile multipartFile, StringBuilder name) {
        LOGGER.debug("in PhotoServiceImpl photoMapper({},{})", multipartFile, name);
        photo.setName(name.toString());
        photo.setType(multipartFile.getContentType());
        photo.setSize(multipartFile.getSize());
    }

    private File uploadFile(String uploaderFolder, String fileName) {
        LOGGER.debug("in PhotoServiceImpl uploadFile({},{})", uploaderFolder, fileName);
        File dir = new File(uploaderFolder);
        if (!dir.exists())
            dir.mkdirs();

        return new File(dir.getAbsolutePath() + File.separator +
                fileName);
    }

    private BufferedImage compressPhoto(byte[] bytes) throws IOException {
        LOGGER.debug("in PhotoServiceImpl compressPhoto({})", bytes);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(inputStream);
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        return result;
    }

    private void writeFileIntoDirectory(File uploadedFile, byte[] bytes) throws IOException {
        LOGGER.debug("in PhotoServiceImpl writeFileIntoDirectory({},{})", uploadedFile, bytes);
        ImageIO.write(compressPhoto(bytes), PHOTO_FORMAT, uploadedFile);
    }

}

