package com.spring.entity;

import javax.persistence.*;

@Entity
@Table(name = "project_request")
@NamedQueries({
@NamedQuery(name = ProjectRequest.NUMBER_OF_INVITE_QUERY,query = "select count(PR) from ProjectRequest PR where PR.project.id in (select p.id from Project p where p.id = :projectId)"),
@NamedQuery(name = "rejectCompany",query = "delete from ProjectRequest P where P.employee.id = :employeeId and P.project.id = : projectId"),
        @NamedQuery(name = "checkingEmployeeRequest", query = "select count(*) from ProjectRequest P where P.employee.id = :employeeId and P.project.id = : projectId")
})
public class ProjectRequest {

    public static final String NUMBER_OF_INVITE_QUERY = "numberOfInvite";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "project_req_gen", sequenceName = "project_req_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Company employee;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Company employer;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private Boolean accepted;

    public ProjectRequest() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getEmployee() {
        return employee;
    }

    public void setEmployee(Company employee) {
        this.employee = employee;
    }

    public Company getEmployer() {
        return employer;
    }

    public void setEmployer(Company employer) {
        this.employer = employer;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}
}
