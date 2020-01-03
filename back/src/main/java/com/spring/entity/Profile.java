package com.spring.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@NamedQuery(name = "getCountOfProfilesByCompany", query = "select count(*) from Profile P where P.company.id = :companyId and P.accepted = :accepted")
public class Profile {

	@Id
	private Long id;

	@Column(name = "first_name")
	@NotBlank(message = "First name can not be empty")
	@Length(min = 2, max = 25, message = "First name mast not be shorter than 2 characters, and longer than 25 characters")
	@Pattern(regexp = "^[a-zA-Z](.)*", message = "First name mast starts with latin letters only")
	@Pattern(regexp = "^[a-zA-Z-']*$", message = "First name may contains latin letters, hyphen and apostrophe only")
	@Pattern(regexp = "(.)*[a-zA-Z]$", message = "First name mast ends with latin letters only")
	@Pattern(regexp = "^((?!.*(--)|('-)|('')|(-').*).)*$", message = "In first name and last name apostrophes and hyphens cannot be near each other or be duplicated")
	private String firstName;

	@Column(name = "last_name")
	@NotBlank(message = "Last name can not be empty")
	@Length(min = 2, max = 25, message = "Last name mast not be shorter than 2 characters, and longer than 25 characters")
	@Pattern(regexp = "^[a-zA-Z](.)*", message = "Last name mast starts with latin letters only")
	@Pattern(regexp = "^[a-zA-Z-']*$", message = "Last name may contains latin letters, hyphen and apostrophe only")
	@Pattern(regexp = "(.)*[a-zA-Z]$", message = "Last name mast ends with latin letters only")
	@Pattern(regexp = "^((?!.*(--)|('-)|('')|(-').*).)*$", message = "In last name apostrophes and hyphens cannot be near each other or be duplicated")
	private String lastName;

	@NotBlank(message = "Nickname can not be empty")
	@Length(min = 3, max = 25, message = "Nick mast not be shorter than 3 characters, and longer than 25 characters")
	private String nickname;

	@Column(name = "photo_url")
	private String photoUrl;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	private Boolean accepted;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	public Profile() {
		//empty profile constructor
	}

	@Override
	public String toString() {
		return "Profile{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", nickname='" + nickname + '\'' +
				", photoUrl='" + photoUrl + '\'' +
				", user=" + user +
				", accepted=" + accepted +
				", company=" + company +
				'}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
