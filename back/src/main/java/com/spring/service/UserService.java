package com.spring.service;

import com.spring.dto.PageableResponse;
import com.spring.entity.User;
import com.spring.enums.Role;

import java.util.List;
import java.util.Optional;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {
    Optional<User> findByUsername(String username);
    void save(User user);
    void create(String email);
    void updateRole(Long id, String role);
    void block(Long id);
    void unblock(Long id);
    PageableResponse getAll(Integer page, Optional<String> username);
    List<String> getAllRoles();
	User createUser(OAuth2User oAuth2User);
}
