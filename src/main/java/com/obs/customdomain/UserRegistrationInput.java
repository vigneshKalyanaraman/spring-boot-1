package com.obs.customdomain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserRegistrationInput {
	
	
	private String firstName;
	
	private String lastName;
	
	private String mobileNo;
	
	private String countryCode;
	
	private String email;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
	private Date dob;
	
	private String gender;
	
	private String country;
	
	private String countryId;
	
	private String profileImage;
	
	private String resident;
	
	private String userId;
	
	private String userType;
	
	private String password;
	
	private String confirmPassword;
	
	private String profession;
	
	private String mobileOtp;
	
	private String emailOtp;
	
	private String googleId;
	
	private String faceBookId;
	
	private String appleId;
	
	private boolean isMobile;
	
	private boolean isEmailId;
	
	private String registerType;
	
	private String socialId;
	
	private String socialType;
	
	private String oldPassword;
	
	private String verifyLink;
	
	private String userName;

	
	
}
