package com.obs.email;

import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailManager {

	private EmailManager() {
	}

	private static final Logger logger = LogManager.getLogger(EmailManager.class);

	protected static EmailConfiguration emailConfig1 = new EmailConfiguration();

	static Thread emailThread;

	@SuppressWarnings("unused")
	private static String getProfile() {
		ResourceBundle bundlePrf = null;
		bundlePrf = ResourceBundle.getBundle("application");
		return bundlePrf.getString("spring.profiles.active");
	}

	public static boolean userVerificationMail(String emailAddress, String msg, String firstname) {
		final String emailAddressFinal = emailAddress;
		final String firstnameFinal = firstname;

		emailThread = new Thread() {
			@Override
			public void run() {
				try {
					logger.info("email user verification");
					EmailConfiguration emailConfig = new EmailConfiguration();
					EmailSender sender = new EmailSender();
					String message = emailConfig.userVerificationMail(emailAddressFinal, msg, firstnameFinal);
					sender.sendFromCustomerService(emailAddressFinal, emailConfig.getVerifyEmailSubject(), message);

				} catch (Exception e) {
					logger.error("Error while sending Email at verify email ", e);
				} finally {
					emailThread.interrupt();
					emailThread = null;
				}
			}
		};
		emailThread.start();
		return true;
	}

	public static boolean forgotPassword(String email, String msg, String firstName, String passcode, String link) {

		final String emailAddressFinal = email;
		final String firstnameFinal = firstName;
		final String linkFinal = link;
		emailThread = new Thread() {
			@Override
			public void run() {
				try {
					EmailConfiguration emailConfig = new EmailConfiguration();
					EmailSender sender = new EmailSender();
					String message = emailConfig.forgotPasswordMail(emailAddressFinal, msg, firstnameFinal, passcode,
							linkFinal);
					sender.sendFromCustomerService(emailAddressFinal, emailConfig.getForgotPasswordSubject(), message);
				} catch (Exception e) {
					logger.error("Error while sending Email at forgotPasswordMail", e);
				} finally {
					emailThread.interrupt();
					emailThread = null;
				}
			}
		};
		emailThread.start();
		return true;

	}

}