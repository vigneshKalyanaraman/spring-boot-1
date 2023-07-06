package com.obs.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name="tbl_user_profile")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class UserProfile {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="id",nullable = false)
	private String id;
	
	@OneToOne(targetEntity=User.class)
	@JoinColumn(name="user_id")
	private User userId;
	
	@OneToOne(targetEntity=Country.class)
	@JoinColumn(name="current_country")
	private Country currrentCountry;
	
	@Column(name="taxable_days")
	private Long taxableDays;
	
	@Column(name="definition_of_day")
	private String definitionOfDay;
	
	@Column(name="profile_image")
	private String profileImage;
	
	@Column(name="document")
	private String document;
	
	@Column(name="created_on")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date createdOn;
	
	@Column(name="updated_on")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date updatedOn;


	@Transient
	private String dob;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Long getTaxableDays() {
		return taxableDays;
	}

	public void setTaxableDays(Long taxableDays) {
		this.taxableDays = taxableDays;
	}

	public String getDefinitionOfDay() {
		return definitionOfDay;
	}

	public void setDefinitionOfDay(String definitionOfDay) {
		this.definitionOfDay = definitionOfDay;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public Country getCurrrentCountry() {
		return currrentCountry;
	}

	public void setCurrrentCountry(Country currrentCountry) {
		this.currrentCountry = currrentCountry;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}
	
}
