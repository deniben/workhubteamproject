package com.spring.service.implementations;


import com.spring.dao.UserDao;
import com.spring.dto.RegistrationDto;
import com.spring.entity.User;
import com.spring.enums.CacheType;
import com.spring.enums.Role;
import com.spring.exception.SecurityException;
import com.spring.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RegistrationService {

    private final UserDao userRepository;

    private final ValidationService validationService;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserDao userRepository, ValidationService validationService,
                               MailService mailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.validationService = validationService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void registerUser(RegistrationDto registrationDto) {

        validationService.registrationValidation(registrationDto);

        logger.info("Registration request accepted");

        User user = new User();

        user.setUsername(registrationDto.getUsername());

        String encodedPassword = passwordEncoder.encode(registrationDto.getPassword());

        user.setPassword(encodedPassword);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(Role.USER);

        user.setRoles(roleSet);
        user.setActive(false);

        userRepository.save(user);

        logger.info("New user with username: {}", user.getUsername());

        String token = TokenUtils.generateToken(user.getUsername(), CacheType.ACTIVATION);

        mailService.sendActivationMail(user.getUsername(), user.getId(), token);

    }

    public void activateAccount(Long userId, String token) {

        logger.info("Account activation attempt. User id : {}", userId);

        User user = userRepository.findById(userId).get();

        if (user == null) {
            throw new SecurityException(400, "Invalid id", "User with such id do not exists");
        }

        String trueToken = TokenUtils.getToken(user.getUsername(), CacheType.ACTIVATION);

        if (trueToken == null) {
            if (user.getActive()) {
                throw new SecurityException(400, "Invalid action", "User is already active");
            } else {
                userRepository.delete(userId);
                throw new SecurityException(400, "Invalid token", "Authentication token expired");
            }
        }

        if (!trueToken.equals(token)) {
            throw new SecurityException(400, "Invalid token", "Token is wrong");
        }

        TokenUtils.removeToken(user.getUsername(), CacheType.ACTIVATION);

        user.setActive(true);
        userRepository.save(user);

        logger.info("User successfully activated");
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

}
