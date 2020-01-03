package com.spring.service;

import com.spring.dto.SearchDto;
import com.spring.entity.Project;
import com.spring.entity.User;
import com.spring.utils.Page;

import java.util.List;

public interface SearchService {

    Page<Project> search(SearchDto searchDto, User user);

}
