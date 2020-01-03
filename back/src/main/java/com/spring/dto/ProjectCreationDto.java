package com.spring.dto;

import com.spring.entity.ProjectType;
import com.spring.entity.Skill;

import java.util.Set;

public class ProjectCreationDto extends ProjectDto {

  private Long projectTypeId;
  private String photoUrl;

   public Long getProjectTypeId() {
       return projectTypeId;
 }
   public void setProjectTypeId(Long projectTypeId) {
       this.projectTypeId = projectTypeId;

    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private Set<Skill> skills;
    public Set<Skill> getSkills() {
        return skills;
    }
    public void setSkills(Set<Skill> skills)
    {
        this.skills = skills;
    }


}
