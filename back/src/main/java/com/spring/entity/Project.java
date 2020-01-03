package com.spring.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.spring.enums.ProjectStatus;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.spring.enums.ProjectStatus;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Indexed


@NamedQueries(
        {       @NamedQuery(name = "numberOfMyProjects", query = "select count(P) from Project P where P.status = :projectStatus and P.companyCreator.id =:companyId   "),
                @NamedQuery(name = "completingProject",query = "update Project P set P.status = 'COMPLETED' where P.id = :projectId"),
                @NamedQuery(name = "deleteProject", query = "delete Project P where P.id = :projectId"),
                @NamedQuery(name = "failedProject", query = "update Project P set P.status = 'FAILED' where P.id = :projectId"),
                @NamedQuery(name = "acceptCompanyToProject", query = "update Project P set P.companyEmployee.id = :employeeId , P.status = 'IN_PROGRESS' where P.id = :projectId "),
                @NamedQuery(name = "existProject",query = "select P.id from Project P where P.id = : projectId"),
                @NamedQuery(name="getSkillsId",query = "select P.requiredSkills from Project P where P.id=:projectId"),
                @NamedQuery(name = "Project_findBySkills", query = "select P from Project as P join P.requiredSkills rs on rs.id in :skills where P.status = 'NEW'"),
                @NamedQuery(name = "Project_findByCompany", query = "from Project as P where P.companyCreator = : idCompany"),
                @NamedQuery(name = "Project_findByType", query = "select P from Project as P where P.projectType = : idType"),
                @NamedQuery(name = "getProjectsByCompany", query = "select P from Project P where P.id =: id"),
                @NamedQuery(name = "changeStatus", query = "update Project project set project.status=:status where project.id=:projectId")
        }
)

public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize = 1, name = "idprog", sequenceName = "nameprog")
	private Long id;

	@Field
	@NotBlank(message = "Name can not be empty")
	@Length(min = 3, max = 40)
	private String name;

	@NotBlank(message = "Description can not be empty")
	@Column(length = 2000)
	private String description;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "type_id")
	private ProjectType projectType;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Company companyCreator;

    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Company companyEmployee;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "required_skills",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")})
    private Set<Skill> requiredSkills = new HashSet<>();

    @Field
    private Double budget;

    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private Long employeeMark;

    private Long employerMark;

    public Project() {}

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

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public Company getCompanyCreator() {
        return companyCreator;
    }

    public void setCompanyCreator(Company companyCreator) {
        this.companyCreator = companyCreator;
    }

    public Company getCompanyEmployee() {
        return companyEmployee;
    }

    public void setCompanyEmployee(Company companyEmployee) {
        this.companyEmployee = companyEmployee;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Long getEmployeeMark() {
        return employeeMark;
    }

    public void setEmployeeMark(Long employeeMark) {
        this.employeeMark = employeeMark;
    }

    public Long getEmployerMark() {
        return employerMark;
    }

    public void setEmployerMark(Long employerMark) {
        this.employerMark = employerMark;
    }

    public Set<Skill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<Skill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
}
