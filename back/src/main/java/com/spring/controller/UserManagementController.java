package com.spring.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.spring.exception.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.spring.service.PhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.spring.dto.PageableResponse;
import com.spring.entity.User;
import com.spring.service.UserService;
import com.spring.service.implementations.MailService;
import com.spring.utils.mapper.FullUserMapper;

@RestController
@RequestMapping("/admin")
public class UserManagementController {

	private final UserService userService;
	private final FullUserMapper fullUserMapper;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementController.class);
    private final PhotosService photosService;

    @Autowired
    public UserManagementController(UserService userService, MailService mailService,
                                    PasswordEncoder passwordEncoder, FullUserMapper fullUserMapper, PhotosService photosService) {
        this.userService = userService;
        this.fullUserMapper = fullUserMapper;
        this.photosService = photosService;
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody Map<String, String> params) {
	    LOGGER.debug("in createUser(), params:{}", params);
	    if (!params.containsKey("email")) {
            throw new RestException("Email is required");
        }
        userService.create(params.get("email"));
        return ResponseEntity.ok("User was successfully created");
    }

    @GetMapping("/users")
    public ResponseEntity<PageableResponse> getAllUsers(Integer page, Optional<String> username) {
	    LOGGER.debug("in getAllUsers(), page:{}, username: {}", page, username);
	    PageableResponse result = userService.getAll(page, username);
        result.setItems(((List<User>) result.getItems()).stream()
                .map(fullUserMapper::toDto)
                .map(fullUserDto -> {
                    String photoUrl = fullUserDto.getPhotoUrl();
                    if (photoUrl != null && !photoUrl.isEmpty()) {
                        fullUserDto.setPhotoUrl(photosService.getPhoto(fullUserDto.getPhotoUrl()));
                    }
                    return fullUserDto;
                })
                .collect(Collectors.toList()));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/users/{id}/block")
    public ResponseEntity<String> blockUser(@PathVariable Long id) {
	    LOGGER.debug("in blockUser(), id:{}", id);
	    userService.block(id);
        return ResponseEntity.ok("User was successfully blocked");
    }

    @PostMapping("/users/{id}/unblock")
    public ResponseEntity<String> ublockUser(@PathVariable Long id) {
	    LOGGER.debug("in ublockUser(), id:{}", id);
	    userService.unblock(id);
        return ResponseEntity.ok("User was successfully unblocked");
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAllRoles() {
	    LOGGER.trace("in getAllRoles()");
	    return ResponseEntity.ok(userService.getAllRoles());
    }

    @PostMapping("/roles")
    public ResponseEntity<String> updateRole(Long id, String role) {
	    LOGGER.debug("in updateRole(), id:{}, role: {}", id, role);
	    userService.updateRole(id, role);
        return ResponseEntity.ok("User role was successfully updated");
    }
}
