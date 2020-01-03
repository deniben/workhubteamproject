package com.spring.service.implementations;

import com.spring.component.UserContext;
import com.spring.dao.UserDao;
import com.spring.dto.PageableResponse;
import com.spring.entity.User;
import com.spring.enums.Role;
import com.spring.exception.RestException;
import com.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String USER_NOT_EXISTS_MESSAGE = "User with such id do not exists";
    private final UserDao userDao;
    private final MailService mailService;
    private final UserContext userContext;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, MailService mailService, UserContext userContext,
                           PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.mailService = mailService;
        this.userContext = userContext;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public void create(String email) {

        if (email == null) {
            throw new RestException("Email can not be empty");
        }

        if (!email.matches(".*@.*\\..*")) {
            throw new RestException("Invalid email format");
        }

        if (userDao.findByUsername(email).isPresent()) {
            throw new RestException("User with such email already exists");
        }

        String password = UUID.randomUUID().toString();

        User user = new User();
        user.setUsername(email);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(password));

        userDao.save(user);
        mailService.sendPasswordToCreatedUser(email, password);

        LOGGER.info("New user created. Credentials were sent to email/username {}", user.getUsername());
    }

    public User createUser(OAuth2User oAuth2User) {
        User user = new User();
        user.setUsername(oAuth2User.getName());
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setActive(true);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        user.setRoles(roles);

        save(user);
        return user;
    }


    @Override
    public void updateRole(Long id, String role) {
        User user = userDao.findById(id).get();
        Role roleObj = Role.valueOf(role);

        if (user == null) {
            throw new RestException(USER_NOT_EXISTS_MESSAGE);
        }

        if (roleObj.equals(Role.ADMIN)) {
            user.getProfile().setCompany(null);
            userDao.save(user);
        }

        user.setRoles(Collections.singleton(roleObj));
        userDao.save(user);

        LOGGER.info("User with id [{}] now has role [{}]", id, role);
    }

    @Override
    public void block(Long id) {
        User currentUser = userContext.getCurrentUser();
        User user = userDao.findById(id).get();

        if (user == null) {
            throw new RestException(USER_NOT_EXISTS_MESSAGE);
        }

        if (user.getId().equals(currentUser.getId()) || user.isAdmin() || user.getProfile() == null) {
            throw new RestException("Invalid operation");
        }

        user.setActive(false);
        userDao.save(user);

        LOGGER.info("User with id {} blocked", id);
    }

    @Override
    public void unblock(Long id) {
        User user = userDao.findById(id).get();

        if (user == null) {
            throw new RestException(USER_NOT_EXISTS_MESSAGE);
        }

        if (user.getActive().equals(true) || user.getProfile() == null) {
            throw new RestException("User is not blocked");
        }

        user.setActive(true);
        userDao.save(user);

        LOGGER.info("User with id {} unblocked", id);
    }

    @Override
    public List<String> getAllRoles() {
        return Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toList());
    }

    @Override
    public PageableResponse getAll(Integer page, Optional<String> username) {
        return userDao.findAll(page, username);
    }

}
