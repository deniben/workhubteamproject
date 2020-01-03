package com.spring.service.implementations;

import com.spring.component.UserContext;
import com.spring.dao.ProjectDao;
import com.spring.dto.ChangePasswordDto;
import com.spring.dto.RegistrationDto;
import com.spring.dao.UserDao;
import com.spring.entity.Company;
import com.spring.entity.Event;
import com.spring.entity.Photo;
import com.spring.entity.Profile;
import com.spring.entity.Project;
import com.spring.exception.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class ValidationService {

	private final UserDao userRepository;
	private final ProjectDao projectDao;
	private final UserContext userContext;

	@Autowired
	public ValidationService(UserDao userRepository, ProjectDao projectDao,
	                         UserContext userContext) {
		this.userRepository = userRepository;
		this.projectDao = projectDao;
		this.userContext = userContext;
	}


	private <T> Map<String, String> validate(T t) {

		Map<String, String> errors = new HashMap<>();

		if(t == null) {
			errors.put("ValidationError", "User data is empty");
			return errors;
		}

		Set<ConstraintViolation<T>> constraintViolation = validator().validate(t);

		if(constraintViolation != null && constraintViolation.size() > 0) {
			constraintViolation.stream().forEach(x -> errors.put(x.getPropertyPath().toString() + "Error", x.getMessage()));
		}

		return errors;

	}

	public void registrationValidation(RegistrationDto registrationDto) {

		Map<String, String> validationResult = validate(registrationDto);

		if(!registrationDto.isPasswordConfirmed()) {
			validationResult.put("PasswordsDoNotMatchError", "Repeated password must be equals to password");
		} else if(userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
			validationResult.put("UsernameError", "User with such email already exists");
		}

		handle(validationResult);

	}

	public void validateEvent(Event event) {

		Map<String, String> result = validate(event);

		if(event.getStartTime() != null && event.getEndTime() != null &&
				( event.getEndTime().isBefore(event.getStartTime()) || event.getEndTime().isEqual(event.getStartTime()) ) ) {
			result.put("UnexpectedDateTimeFormat", "Date and time of the beginning must be before it's end's date and time");
		}

		if(event.getProjectId() == null) {
			result.put("ProjectIdError", "Please, provide project id");
		} else {
			Project project = projectDao.findById(event.getProjectId()).get();
			Company company = userContext.getCurrentUser().getProfile().getCompany();

			if(project == null || (!project.getCompanyEmployee().getId().equals(company.getId()) &&
					!project.getCompanyCreator().getId().equals(company.getId()))) {
				result.put("ProjectError", "Project with such id do not exists or invalid permission");
			}
		}

		handle(result);
	}

	private void handle(Map<String, String> validationResult) {
		if(validationResult.size() > 0) {
			throw new ValidationException(validationResult, HttpStatus.BAD_REQUEST.value());
		}
	}

	public void changePasswordValidation(ChangePasswordDto changePasswordDto) {

		Map<String, String> validationResult = validate(changePasswordDto);

		if(changePasswordDto.getNewPassword() != null && changePasswordDto.getOldPassword() != null &&
				changePasswordDto.getNewPassword().equals(changePasswordDto.getOldPassword())) {
			validationResult.put("NewOldDiffError", "Old and new passwords must be different");
		} else if (changePasswordDto.getNewPassword() != null && changePasswordDto.getRepeatedPassword() != null &&
				!changePasswordDto.getNewPassword().equals(changePasswordDto.getRepeatedPassword())) {
			validationResult.put("ConfirmationError", "New password and repeated passwords must match");
		}

		handle(validationResult);
	}

	public void profileValidation(Profile profile) {
		Map<String, String> errors = validate(profile);
		if(errors.size() != 0) {
			handle(errors);
		}
	}
	public void photoValidation(Photo photo){
		Map<String,String> errors = validate(photo);
		if(photo.getType().equals("image/gif")){
			errors.put("type","Gif type is forbidden");
		}
		if(errors.size()!=0){
			handle(errors);
		}
	}


	public void projectValidation(Project project) {
		Map<String, String> errors = validate(project);
		if(errors.size() != 0) {
			handle(errors);
		}
	}

	@Bean
	public Validator validator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

}
