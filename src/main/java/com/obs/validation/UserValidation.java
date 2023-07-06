package com.obs.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.obs.customdomain.MyProfileInput;
import com.obs.customdomain.TokenInput;
import com.obs.customdomain.UserRegistrationInput;
import com.obs.messages.MessageConstants;

@Service
public class UserValidation {

	@Autowired
	public BasicValidation basicValidation;
	
	@Autowired
	PasswordEncoder passwordencoder;

	public String validateUser(UserRegistrationInput input) {

		if (!basicValidation.checkStringnullandempty(input.getFirstName())) {
			return MessageConstants.FIRSTNAME;
		}
		if (!basicValidation.nameValidation(input.getFirstName())) {
			return MessageConstants.FIRSTNAME;
		}
		if (!basicValidation.isalphaChar(input.getFirstName())) {
			return MessageConstants.FIRSTNAME;
		}
		if (!basicValidation.checkStringnullandempty(input.getLastName())) {
			return MessageConstants.LASTNAME;
		}
		if (!basicValidation.nameValidation(input.getLastName())) {
			return MessageConstants.LASTNAME;
		}
		
		if (!basicValidation.checkStringnullandempty(input.getCountryCode())) {
			return MessageConstants.COUNTRYCODE;
		}
		if (basicValidation.isStringnullandempty(input.getEmail())) {
			return MessageConstants.EMAILREQUIRED;
		}
		if(!basicValidation.emailValidation(input.getEmail())) {
			return MessageConstants.EMAIL;
		}
		if (!basicValidation.checkStringnullandempty(input.getMobileNo())) {
			return MessageConstants.MOBILENO;
		}

		if (!basicValidation.isNumeric(input.getMobileNo())) {
			return MessageConstants.MOBILENO;
		}
		
		return MessageConstants.SUCCESS;

	}

	public String validateOtp(UserRegistrationInput input) {

		if (!basicValidation.checkStringnullandempty(input.getEmail())
				&& !basicValidation.emailValidation(input.getEmail())) {
			return MessageConstants.EMAIL;
		}

		if (!basicValidation.checkStringnullandempty(input.getMobileNo())) {
			return MessageConstants.MOBILENO;
		}

		if (!basicValidation.validateOtp(input.getEmailOtp())) {
			return MessageConstants.MOBILEOTP;
		}

		if (!basicValidation.validateOtp(input.getEmailOtp())) {
			return MessageConstants.EMAILOTP;
		}

		return MessageConstants.SUCCESS;

	}

	public String validateEmail(String email) {
		if (!basicValidation.checkStringnullandempty(email) && !basicValidation.emailValidation(email)) {
			return MessageConstants.EMAIL;
		}
		return MessageConstants.SUCCESS;
	}

	public String validatePassword(UserRegistrationInput input) {

		if (!basicValidation.checkStringnullandempty(input.getEmail())
				&& !basicValidation.emailValidation(input.getEmail())) {
			return MessageConstants.EMAIL;
		}

		if (!basicValidation.passwordValidation(input.getPassword())) {
			return MessageConstants.PASSWORD;
		}
		if (!basicValidation.passwordValidation(input.getConfirmPassword())) {
			return MessageConstants.CONFIRMPASSWORD;
		}

		if (!input.getConfirmPassword().equals(input.getPassword())) {
			return MessageConstants.MISSMATCHPASS;
		}
		return MessageConstants.SUCCESS;
	}

	public String mobileAndEmailValidation(UserRegistrationInput input) {

		if (basicValidation.isStringnullandempty(input.getEmail())
				&& basicValidation.isStringnullandempty(input.getMobileNo())) {
			return MessageConstants.EMAILORMOBILENO;
		}
		return MessageConstants.SUCCESS;
	}

	public String otpRequiredValidation(UserRegistrationInput input) {
		if (basicValidation.isStringnullandempty(input.getEmailOtp())
				&& basicValidation.isStringnullandempty(input.getMobileOtp())) {
			return MessageConstants.EMAILORMOBILEOTP;
		}
		return MessageConstants.SUCCESS;
	}

	public String emailValidation(UserRegistrationInput input) {
		if (basicValidation.isStringnullandempty(input.getEmail())) {
			return MessageConstants.EMAIL;
		}
		return MessageConstants.SUCCESS;
	}

	public String mobileNoValidation(UserRegistrationInput input) {
		if (basicValidation.isStringnullandempty(input.getMobileNo())) {
			return MessageConstants.MOBILENO;
		}
		return MessageConstants.SUCCESS;
	}

	public String changePasswordValidation(UserRegistrationInput input, String password) {
		if(!basicValidation.passwordValidation(input.getOldPassword()))
		{
			return MessageConstants.OLDPASSWORD;
		}
		if(!basicValidation.checkStringnullandempty(input.getPassword()) )
		{
			return MessageConstants.PASSWORDS;
		}
		if(!basicValidation.checkStringnullandempty(input.getConfirmPassword()))
		{
			return MessageConstants.CONFIRMPASSWORD;
		}
		if(!passwordencoder.matches(input.getOldPassword(), password))
		{
			return MessageConstants.PASSWORD;
		}
		if(!input.getPassword().equalsIgnoreCase(input.getConfirmPassword()))
		{
			return MessageConstants.MISSMATCHPASS;
		}
		if(input.getOldPassword().equalsIgnoreCase(input.getPassword()))
		{
			return MessageConstants.SAMEPASSWORD;
		}
		return  MessageConstants.SUCCESS;

	}
	
	public String editUserRequired(UserRegistrationInput input) {
		if (basicValidation.isStringnullandempty(input.getEmail())) {
			return MessageConstants.EMAILREQUIRED;
		}
		if (basicValidation.isStringnullandempty(input.getLastName())) {
			return MessageConstants.LASTNAME_REQUIRED;
		}
		if (basicValidation.isStringnullandempty(input.getFirstName())) {
			return MessageConstants.FIRSTNAME_REQUIRED;
		}
		if (basicValidation.isStringnullandempty(input.getUserName())) {
			return MessageConstants.USERNAME_REQUIRED;
		}
		if (basicValidation.isStringnullandempty(input.getGender())) {
			return MessageConstants.GENDER_REQUIRED;
		}
		if (basicValidation.isStringnullandempty(input.getMobileNo())) {
			return MessageConstants.MOBILENO_REQUIRED;
		}
		if (basicValidation.isStringnullandempty(input.getProfileImage())) {
			return MessageConstants.IMAGE_REQUIRED;
		}
		
		return MessageConstants.SUCCESS;

}

	public String validateEditUser(MyProfileInput profileObj) {
		if (!basicValidation.checkStringnullandempty(profileObj.getFirstName())) {
			return MessageConstants.FIRSTNAME;
		}
//		if (!basicValidation.isalphaChar(profileObj.getFirstName())) {
//			return MessageConstants.FIRSTNAME;
//		}
		if (!basicValidation.checkStringnullandempty(profileObj.getLastName())) {
			return MessageConstants.LASTNAME;
		}
		if (basicValidation.isStringnullandempty(profileObj.getEmail())) {
			return MessageConstants.EMAILREQUIRED;
		}
		if(!basicValidation.emailValidation(profileObj.getEmail())) {
			return MessageConstants.EMAIL;
		}
		if (!basicValidation.checkStringnullandempty(profileObj.getMobileNo())) {
			return MessageConstants.MOBILENO;
		}
		if (!basicValidation.isNumeric(profileObj.getMobileNo())) {
			return MessageConstants.MOBILENO;
		}
		return MessageConstants.SUCCESS;
	}
	
	public String updateDeviceTokenRequired(TokenInput input) {
		if (basicValidation.isStringnullandempty(input.getType())) {
			return MessageConstants.TOKENREQUIRED;
		}
		return MessageConstants.SUCCESS;
	}
}
