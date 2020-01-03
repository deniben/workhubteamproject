package com.spring.dao;

import java.util.Optional;

import com.spring.dto.PageableResponse;
import com.spring.entity.User;
import com.spring.utils.Page;

public interface UserDao extends BaseDao<User> {
    Optional<User> findByUsername(String username);
    void changePassword(String password);
    PageableResponse findAll(Integer page, Optional<String> username);
}
