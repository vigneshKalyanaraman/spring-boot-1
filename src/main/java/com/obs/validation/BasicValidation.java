package com.obs.validation;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;



@Service
public class BasicValidation {



	private static final Logger logger = LogManager.getLogger(BasicValidation.class);

	public boolean checkStringnullandempty(String content)
	{
		return (content != null && !content.isEmpty());
	}
	
	public boolean isStringnullandempty(String content)
	{
		return (content == null || content.isEmpty());
	}

	public boolean checkListnullandsize(List<?> content)
	{
		return (content != null && !content.isEmpty());
	}

	public boolean emailValidation(String content) {
		return (content != null && !content.isEmpty() && isValidEmailAddress(content));
	}

	private boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}



	public boolean futureDateValidation(Date date)
	{
		if(date != null)
		{
			Instant givenInstant = date.toInstant();
			Instant currentdate = Instant.now();
			return currentdate.isAfter(givenInstant);
		}
		return false;
	}

	public boolean pastDateValidation(Date date)
	{
		if(date != null)
		{
			logger.info(date);
			Instant givenInstant = date.toInstant();
			Instant currentdate = Instant.now();
			return currentdate.isBefore(givenInstant);
		}
		return false;
	}
	private boolean isAlphaNumericWithspecialCharacters(String s){
		Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[A-Z])(?=.*[!?@#$%^&*()._-]).{7,20})");
		return pattern.matcher(s).matches();
	}

	public boolean passwordValidation(String password)
	{
		return (checkStringnullandempty(password) && isAlphaNumericWithspecialCharacters(password) );
	}

	public boolean isalphabets(String s)
	{
		Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
		return pattern.matcher(s).matches();
	}
	public boolean isalphaNumeric(String s)
	{
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
		
		return pattern.matcher(s).matches();
	}
	public boolean isNumeric(String s)
	{
		Pattern pattern = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");
		
		return pattern.matcher(s).matches();
	}
	
	
	public boolean isalphaChar(String s)
	{
		Pattern pattern = Pattern.compile("^[A-Za-z]{3,20}$");
		
		return pattern.matcher(s).matches();
	}
	public boolean isalphaCharLast(String s)
	{
		Pattern pattern = Pattern.compile("^[A-Za-z]{1,20}$");
		
		return pattern.matcher(s).matches();
	}

	public boolean nameValidation(String name)
	{
		if(name != null && !name.isEmpty())
		{
			Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
			return pattern.matcher(name).matches();
		}
		return false;
		
	}
	
	public boolean nameValidationwithspace(String name)
	{
		if(name != null && !name.isEmpty())
		{
			Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
			return pattern.matcher(name).matches();
		}
		return false;
		
	}

	public boolean nameLength(String name){
		return (name != null && name.length() <= 25);		
	}

	public String getonlydata(String s,int i)
	{
		Pattern pattern = null;
		switch(i)
		{
		case 1://alphabets
			pattern = Pattern.compile("[^a-zA-Z]");
			break;
		case 2://digits
			pattern = Pattern.compile("[^0-9]");
			break;
		case 3://alphanumeric
			pattern = Pattern.compile("[^a-zA-Z0-9]");
			break;
		default:
			break;
		}
		Matcher matcher = pattern != null?pattern.matcher(s):null;
		return matcher != null?matcher.replaceAll(""):s;
	}
	
	public boolean validateOtp(String otp) {
		
		return !checkStringnullandempty(otp) ;
	}
}
