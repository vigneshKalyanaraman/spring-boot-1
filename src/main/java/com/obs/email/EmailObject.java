package com.obs.email;

public class EmailObject {

private String sendUserName;
	
	private String sendFirstName;
	
	private String sendLastName;
	
	private String sendSurname;
	
	private String sendEmail;
	
    private String recieveUserName;
	
	private String recieveFirstName;
	
	private String recieveLastName;
	
	private String recieveSurname;
	
	private String recieveEmail;
	
	private String passcode;
	
	private String logoUrl;
	
	private String adminUrl;
	
	private String adminEmail;
	
	private String message1;
	
	private String message2;
	
	private String message3;
	
	
	
	public EmailObject(String sendEmail, String recieveEmail, String logoUrl, String adminUrl) {
		super();
		this.sendEmail = sendEmail;
		this.recieveEmail = recieveEmail;
		this.logoUrl = logoUrl;
		this.adminUrl = adminUrl;
	}

	public EmailObject() {
		super();
	}

	private String title;
	
	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getAdminUrl() {
		return adminUrl;
	}

	public void setAdminUrl(String adminUrl) {
		this.adminUrl = adminUrl;
	}

	public String getMessage1() {
		return message1;
	}

	public void setMessage1(String message1) {
		this.message1 = message1;
	}

	public String getMessage2() {
		return message2;
	}

	public void setMessage2(String message2) {
		this.message2 = message2;
	}

	public String getMessage3() {
		return message3;
	}

	public void setMessage3(String message3) {
		this.message3 = message3;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getSendFirstName() {
		return sendFirstName;
	}

	public void setSendFirstName(String sendFirstName) {
		this.sendFirstName = sendFirstName;
	}

	public String getSendLastName() {
		return sendLastName;
	}

	public void setSendLastName(String sendLastName) {
		this.sendLastName = sendLastName;
	}

	public String getSendSurname() {
		return sendSurname;
	}

	public void setSendSurname(String sendSurname) {
		this.sendSurname = sendSurname;
	}

	public String getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}

	public String getRecieveUserName() {
		return recieveUserName;
	}

	public void setRecieveUserName(String recieveUserName) {
		this.recieveUserName = recieveUserName;
	}

	public String getRecieveFirstName() {
		return recieveFirstName;
	}

	public void setRecieveFirstName(String recieveFirstName) {
		this.recieveFirstName = recieveFirstName;
	}

	public String getRecieveLastName() {
		return recieveLastName;
	}

	public void setRecieveLastName(String recieveLastName) {
		this.recieveLastName = recieveLastName;
	}

	public String getRecieveSurname() {
		return recieveSurname;
	}

	public void setRecieveSurname(String recieveSurname) {
		this.recieveSurname = recieveSurname;
	}

	public String getRecieveEmail() {
		return recieveEmail;
	}

	public void setRecieveEmail(String recieveEmail) {
		this.recieveEmail = recieveEmail;
	}

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
}
