package com.spring.service;

import com.spring.enums.ProjectStatus;

public interface RateService {

    void rate(Long projectId, Long rate, ProjectStatus status);

}
