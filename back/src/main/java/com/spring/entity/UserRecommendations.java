package com.spring.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "user_recommendations")
public class UserRecommendations {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "user_recom", allocationSize = 1, sequenceName = "user_recommendations_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;

	private Float valuation;

	@ManyToOne
	@JoinColumn(name = "project_type_id")
	private ProjectType projectType;

	public UserRecommendations() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Float getValuation() {
		return valuation;
	}

	public void setValuation(Float valuation) {
		this.valuation = valuation;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
}
