package com.spring.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.UserDao;
import com.spring.dto.Credentials;
import com.spring.entity.User;
import com.spring.exception.SecurityException;

@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserDao userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);

		if(!user.isPresent()) {
			throw new UsernameNotFoundException("User with such username do not exists");
		}

		return new Credentials(user.get());
	}

}
