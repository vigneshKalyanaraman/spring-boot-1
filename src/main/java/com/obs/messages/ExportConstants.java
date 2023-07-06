package com.obs.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ExportConstants {

	private ExportConstants() {}
	/**
	 * Merchant Export constants
	 */
	public static final String CSV_EXTENSION = ".csv";
	public static final  char DEFAULT_SEPARATOR = ',';
	public static final String DELIMITER = ",";
	public static final  String LINE_SEPARATOR = "\n";

	public static final String VIEW_MANAGE_USER_HEADER = "UserId,firstName,lastName,UserName,gender,mobileNo,resident,emailId,dob ";


	public static String dateFormat(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	// vh5 start..
	public static String dateandTimeFormat(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
		return formatter.format(date);
	}
	// vh5 end..

	public static String convertZoneDateAndTime(String zoneName, Date date) {	
		DateFormat df = new SimpleDateFormat("dd MMM yyyy / h.mm a");
		df.setTimeZone(TimeZone.getTimeZone(zoneName));
		return df.format(date);
	}
}
