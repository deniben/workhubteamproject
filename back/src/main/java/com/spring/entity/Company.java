package com.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.enums.CompanyType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "companies")
@DynamicUpdate
@NamedQueries({
  @NamedQuery(name = "getCompanyByProjectId", query = "select C from Company C where C.id in ( select P.companyEmployee.id  from Project P where P.id = :id)"),
  @NamedQuery(name = "numberOfCompanyToMyProject", query = "select count(C) from Company C where C.id in  (select P.employee.id from ProjectRequest as P where P.project.id = :id )"),
  @NamedQuery(name = "getCompaniesForMyProject", query = "select C from Company  C  where C.id in  (select P.employee.id from ProjectRequest as P where P.project.id = :id ) order by C.avgMark"),
  @NamedQuery(name = "getBlockedCompanies", query = "select c from Company  c  where c.blocked = true"),
  @NamedQuery(name = "getAllUnblockedCompanies", query = "select c from Company  c  where c.blocked = false"),
  @NamedQuery(name = "getCompanyById", query = "select C.id  from Company C where C.id = :id"),
  @NamedQuery(name = "getCountOfUnblockedAllCompanies", query = "select count(C) from Company C where C.blocked = false"),
  @NamedQuery(name = "getCountOfBlockedAllCompanies", query = "select count(C) from Company C where C.blocked = true "),
})

public class Company {

  @Id
  @SequenceGenerator(name = "company_sequence", sequenceName = "companies_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_sequence")
  private Long id;

  @NotBlank(message = "name must be not blank")
  @Size(min = 3, max = 30, message = "name length is incorrect")
  @Pattern(regexp = "^[A-Za-z0-9? ?A-Za-z0-9]{3,30}$")
  private String name;

  @Size(min = 25, max = 2000, message = "min = 25, max = 2000")
  @Pattern(regexp = "^[\\s\\S]{0,2000}$")
  @Column(length = 2000)
  private String description;

  @Enumerated(EnumType.STRING)
  private CompanyType type = CompanyType.EMPLOYEE;

  @Column(name = "mark_avg")
  private Long avgMark;

  @Column(name = "blocked")
  private Boolean blocked;

  @Column(name = "photo_url")
  private String photoUrl;

  @ManyToMany
  @JoinTable(name = "proposed_skills",
    joinColumns = {@JoinColumn(name = "company_id")},
    inverseJoinColumns = {@JoinColumn(name = "skill_id")})
  private Set<Skill> proposedSkills = new HashSet<>();

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "owner_id")
  private Profile owner;

  @OneToMany(mappedBy = "company")
  private List<Profile> workers;

  public Company() {
    //empty company constructor
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public Set<Skill> getProposedSkills() {
    return proposedSkills;
  }

  public void setProposedSkills(Set<Skill> proposedSkills) {
    this.proposedSkills = proposedSkills;
  }

  @Override
  public String toString() {
    return "Company{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      ", type=" + type +
      ", avgMark=" + avgMark +
      ", proposedSkills=" + proposedSkills +
      '}';
  }

  public Boolean isMember(User user) {
    return owner.getUser().getId().equals(user.getId()) || workers.stream().anyMatch(x -> x.getUser().getId().equals(user.getId()));
  }

  public Boolean isBlocked() {
    return blocked;
  }

  public void setBlocked(Boolean blocked) {
    this.blocked = blocked;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CompanyType getType() {
    return type;
  }

  public void setType(CompanyType type) {
    this.type = type;
  }

  public Profile getOwner() {
    return owner;
  }

  public void setOwner(Profile owner) {
    this.owner = owner;
  }

  public Long getAvgMark() {
    return avgMark;
  }

  public void setAvgMark(Long avgMark) {
    this.avgMark = avgMark;
  }

  public List<Profile> getWorkers() {
    return workers;
  }

  public void setWorkers(List<Profile> workers) {
    this.workers = workers;
  }

}
