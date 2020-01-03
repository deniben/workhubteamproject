package com.spring.service;

import java.util.List;
import java.util.Optional;

import com.spring.dto.PageableResponse;
import com.spring.entity.ProjectType;
import com.spring.entity.User;
import org.springframework.http.ResponseEntity;


public interface ProjectTypeService {

    List<ProjectType> findAll();

    PageableResponse getAll(Integer page, Optional<String> name);

    ProjectType save(ProjectType projectType) ;

    ProjectType delete(long id) ;

    ProjectType findById(long id) ;

    ProjectType findProjectTypeByName(String name);

    ProjectType updateProjectType(ProjectType projectType);

    ResponseEntity<Object> deleteType(long id);

    List<ProjectType> findAllNotBlocked();

    ResponseEntity<Object> unBlockedType(long id);

    }
