package com.spring.component;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReachableConstraints implements ConstraintValidator<Reachable, LocalDateTime> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
		logger.info("Validating date : [{}]", value);
		return value != null && value.isAfter(LocalDateTime.now());
	}
}
