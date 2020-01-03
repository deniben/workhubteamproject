package com.spring.dao;

import java.util.List;
import java.util.Optional;

import com.spring.dto.PageableResponse;
import com.spring.entity.ProjectType;

public interface ProjectTypeDao extends BaseDao<ProjectType> {

    Optional<ProjectType> findProjectTypeByName(String name);

    void deleteType(long id);

    List<ProjectType> findAllNotBlocked();

    void blockedType (long projectType);

    void unBlockedType (long projectType);

    PageableResponse findAll(Integer page, Optional<String> name, String currentUser);

}
