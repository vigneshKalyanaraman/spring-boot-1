package com.obs.email;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import com.obs.messages.MessageConstants;
import com.obs.messages.PropertyConstants;
import com.obs.util.CommonProperties;
import com.obs.util.IoUtils;



public class EmailConfiguration {

	private static ResourceBundle config = getResourceBundle();

	private static final Logger logger = LogManager.getLogger(EmailConfiguration.class);	
	
	private static final String BUNDLENAME = "emailConfig";
	
	private static final String VERIFICATION = "verification.fileName";
	
	private static ResourceBundle getResourceBundle() {
		ResourceBundle bundle = null;
		bundle = ResourceBundle.getBundle(BUNDLENAME);
		return bundle;
	}

	
	private static String getString(String key) {
		return config.getString(key);
	}
	
	public boolean isAuth() {
		String isAuthStr = getString("isAuth");
		return ("YES".equalsIgnoreCase(isAuthStr));
	}

	public String smtpHost() {
		return getString("smtpHost");
	}

	public int smtpPort() {
		String strPort = getString("smtpPort");
		return Integer.parseInt(strPort);
	}

	public String smtpUser() {
		return getString("smtpUser");
	}

	public String smtpPassword() {
		return getString("smtpPassword");
	}
	
	public String getVerifyEmailSubject() {
		return getString("verification.subject");
	}
	
	public String getVerifyEmailFile() {
		return getString(VERIFICATION);
	}
		
	public String getUserServiceEmailAddress() {
		return getString("userServiceEmailAddress");
	}
	
	  public String getForgotPasswordSubject() {
	        return getString("forgotpassword.subject");
	    }
	
	  public String getForgotPasswordFile() {
			return getString("forgotpassword.fileName");
		}
	@SuppressWarnings("rawtypes")
	private String readFile(String messageFile, Map tokens) throws IOException {
		String filePath = CommonProperties.getBasePath()+CommonProperties.getContextPath()+messageFile;
		try {
			String message = new IoUtils().read(filePath);
			return replaceTokens(message, tokens);
		} catch (IOException e) {
			logger.info( "error message ", e);
		return e.getMessage();
		}
	}

	@SuppressWarnings("rawtypes")
	private String replaceTokens(String message, Map tokens) {
		for (Iterator iterator = tokens.keySet().iterator(); iterator.hasNext();) {
			String token = (String) iterator.next();
			message = StringUtils.replace(message, token, (String)tokens.get(token));
		}
		return message;
	}
	
	
	@SuppressWarnings("unused")
	private HashMap<String,String> setBasicmapInfo(EmailObject emailObj)
	{
		HashMap<String,String> mapBasic = new HashMap<>();
		mapBasic.put(PropertyConstants.EMAIL, emailObj.getRecieveEmail());
		mapBasic.put(PropertyConstants.USERNAME, emailObj.getRecieveUserName());
		mapBasic.put(PropertyConstants.FIRSTNAME, emailObj.getRecieveFirstName());
		mapBasic.put("${password}", emailObj.getPasscode());
		mapBasic.put(PropertyConstants.URL, emailObj.getLogoUrl());
		mapBasic.put(PropertyConstants.LOGOURL, emailObj.getLogoUrl());
		return mapBasic;
	}

	public String userVerificationMail(String emailAddress,String msg,String username) throws IOException{
		HashMap<String,String> map = new HashMap<>();
		map.put("${emailAddress}", emailAddress);
		map.put("${firstName}", username);		
		map.put("${msg}", msg);

		String messageFile = config.getString(VERIFICATION);
		return readFile(messageFile, map);
	}


	public String forgotPasswordMail(String emailAddressFinal, String msg, String firstnameFinal, String passcode,
			String linkFinal) throws IOException {

		 HashMap<String, String> map = new HashMap<>();
	        map.put(MessageConstants.EMAILADDRESSMAIL, emailAddressFinal);
	        map.put(MessageConstants.USERNAME, firstnameFinal);
	        map.put(MessageConstants.FIRSTNAMEMAIL, firstnameFinal);
	        map.put(MessageConstants.LINKMAIL, linkFinal);
	        map.put(MessageConstants.MSGMAIL, msg);
	        map.put(MessageConstants.PASSCODE, passcode);

	        String messageFile = config.getString("forgotpassword.fileName");
	        return readFile(messageFile, map);
	}
		

	

}