package com.spring.component;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.UserDao;
import com.spring.dto.OAuth2UserDto;
import com.spring.entity.User;
import com.spring.exception.RestException;

@Component
public class UserContext {

	@Autowired
	UserDao userDao;

	@Transactional
	public User getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication != null) {

			String username = null;

			if(authentication.getPrincipal() instanceof OAuth2UserDto) {
				username = ((OAuth2User) authentication.getPrincipal()).getName();
			}

			Optional<User> userOptional = userDao.findByUsername(username == null ? (String) authentication.getPrincipal() : username);

			if(userOptional.isPresent()) {
				return userOptional.get();
			}

		}
		throw new RestException("User is not authorized", HttpStatus.FORBIDDEN.value());
	}

}
