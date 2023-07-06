package com.obs.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Table(name = "tbl_user")
@Entity
@Data
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "dob")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
	private Date dob;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "mobile_verification")
	private boolean mobileVerification;

	@Column(name = "email_verification")
	private boolean emailVerification;
	
	@Column(columnDefinition="tinyint(1) default 0")
	private boolean socialAccount;
	
	@Column(name = "country_code")
	private String countryCode;
	
	@OneToOne(targetEntity = AccountStatus.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "account_status")
	private AccountStatus accountStatus;

	@OneToOne(targetEntity = AccountType.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "account_type")
	private AccountType accountType;

	@OneToOne(targetEntity = UserProfession.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "profession")
	private UserProfession profession;
	
	@Column(name = "gender")
	private String gender;

	@Column(name = "created_on")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date createdOn;

	@Column(name = "updated_on")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date updatedOn;

	@Column(name = "active")
	private boolean active;
	
	@Column(name = "deleted")
	private boolean deleted;

	@OneToOne(targetEntity = Country.class)
	@JoinColumn(name = "country")
	private Country country;

	@Column(name = "invalid_attempts")
	private Long inValidAttempts;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "register_type")
	private String registerType;
	
	private String facebookId;
	
	private String googleId;
	
	private String appleId;
	
	private String socialLoginType;
	
	@Column(name ="profile_image")
	private String profileImage;
	
	@Column(name ="resident")
	private String resident;


	


	
}
