package com.obs.domain;

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

@Data
@Entity
@Table(name="tbl_user_security")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class UserSecurity {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="id",nullable = false)
	private String id;
	
	@OneToOne(targetEntity=User.class,fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User userId;
	
	@Column(name="password")
	private String password;
	
	@Column(name="mobile_no")
	private String mobileNo;
	
	@Column(name="email_id")
	private String email;

	@Column(name="otp")
	private Long otp;
	
	@Column(name="otp_creation_date")
	private Date otpCreationDate;
	
	@Column(name="email_otp")
	private Long emailOtp;
	
	@Column(name="email_otp_creation_date")
	private Date emailOtpCreationDate;
	
	@Column(name="created_no")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date createdOn;
	
	@Column(name="updated_on")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Date updatedOn;
	
	@Column(name="social_password")
	private String socialPassword;
	
	@Column(name="is_social",columnDefinition = "tinyint(1) default 0")
	private boolean isSocial;
	
	@Column(name = "social_type")
	private String socialType;
	
	@Column(name ="verify_link")
	private String verifyLink;
}
