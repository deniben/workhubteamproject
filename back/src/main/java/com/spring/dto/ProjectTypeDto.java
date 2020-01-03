package com.spring.dto;


import com.spring.entity.ProjectType;

public class ProjectTypeDto {

        private Long id;
        private String name;

        public ProjectTypeDto() {
        }

        public ProjectTypeDto(ProjectType projectType) {
            this.id = projectType.getId();
            this.name = projectType.getName();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

