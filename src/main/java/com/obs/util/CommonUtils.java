package com.obs.util;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obs.domain.User;
import com.obs.domain.UserProfile;
import com.obs.service.IUserService;



@Service
public class CommonUtils {

	/**
	 * Logger Instantiated for Log Management
	 */
	static final Logger logger = LogManager.getLogger(CommonUtils.class);

	static final char DEFAULT_SEPARATOR = ',';

	@Autowired
	IUserService userService;


	private Random random =  new Random();


	public String encode(String src) {
		try { 
			MessageDigest md;
			md = MessageDigest.getInstance( "SHA-512" ); 
			byte[] bytes = src.getBytes(StandardCharsets.UTF_8); 
			md.update(bytes, 0, bytes. length ); 
			byte[] sha1hash = md.digest(); 
			return convertToHex(sha1hash);
		}    
		catch(Exception e){ 
			logger.error(e);
		}
		return src; 
	} 
	public String generateRandomStringWithLength(int randomLen) {
		String alphabet= "abcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < randomLen; i++) {
			char c = alphabet.charAt(random.nextInt(26));
			sb.append(c);
		}
		return sb.toString();
	}

	public String generateRandomDigitwithlength(int randomLen)
	{
		StringBuilder sb = new StringBuilder("");
		int counter=0;
		while(counter++< randomLen) {
			sb.append(random.nextInt(9));
		}
		return sb.toString();
	}

	public String randomReferenceString(int length)
	{
		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			sb.append(chars[random.nextInt(chars.length)]);
		}
		sb.append((100 + random.nextInt(900)));
		return sb.toString();
	}

	public String referenceString()
	{
		int sysTrace = 10 + random.nextInt(90);
		return Long.toString(new Date().getTime()) + sysTrace;
	}
	private String convertToHex(byte[] sha1hash) {
		StringBuilder builder = new StringBuilder(); 
		for (int i = 0; i < sha1hash. length ; i++) { 
			byte c = sha1hash[i]; 
			addHex(builder, (c >> 4) & 0xf); 
			addHex(builder, c & 0xf);
		} 
		return builder.toString();
	} 
	private static void addHex(StringBuilder builder, int c) { 
		if (c < 10) 
			builder.append((char) (c + '0' ));
		else 
			builder.append((char) (c + 'a' - 10));
	}
	public void invalidateAccessToken(HttpServletRequest request)
	{
		try
		{
			String authHeader = request.getHeader("Authorization");
			if (authHeader != null) {
				String tokenValue = authHeader.replace("bearer", "").trim();
				logger.info(tokenValue);
			}
		}
		catch (Exception e) {
			logger.error(e);		}
	}

	public User getUser(final HttpServletRequest request){
		if(request != null && request.getUserPrincipal()!= null){
			Principal principal = request.getUserPrincipal();
			return userService.getUserById(principal.getName());
		} else {
			return null;
		}
	}
	
	private static String base64Encode(String token) {
		byte[] encodedBytes = Base64.encodeBase64(token.getBytes());
		return new String(encodedBytes, StandardCharsets.UTF_8);
	}


	/**
	 * Export CSV
	 */	  
	private static String followCVSformat(String value) {

		String result = value;
		if (result.contains("\"")) {
			result = result.replace("\"", "\"\"");
		}
		return result;

	}

	public static void writeLine(Writer w, List<String> values) throws IOException {
		writeLine(w, values, DEFAULT_SEPARATOR, ' ');
	}

	public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
		writeLine(w, values, separators, ' ');
	}

	public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

		boolean first = true;

		//default customQuote is empty

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuilder sb = new StringBuilder();
		for (String value : values) {
			value = value == null ? "" : value;
			if (!first) {
				sb.append(separators);
			}
			if (customQuote == ' ') {
				sb.append(followCVSformat(value));
			} else {
				sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
			}

			first = false;
		}
		sb.append("\n");
		w.append(sb.toString());
	}



	public String createExportDirectory(String exportFileLocation, String userId) {

		exportFileLocation = exportFileLocation + userId + "/";
		File directory = new File(exportFileLocation);

		if (!directory.exists())
			directory.mkdir();

		return exportFileLocation;
	}


	public double limitPrecision(double x, int maxDigitsAfterDecimal) {
		BigDecimal bd = BigDecimal.valueOf(x);
		bd = bd.setScale(maxDigitsAfterDecimal, RoundingMode.HALF_UP);
		logger.info(bd.doubleValue());
		return bd.doubleValue();
	}

	public Date getUTCDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		sdf.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
		String dateStr = sdf.format( new Date());
		Date currentDate= null;
		try {
			currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
		} catch (ParseException e) {
			logger.error(e);
		}
		logger.info(currentDate);
		return currentDate;
	}

	public boolean isSameDay(Date date)
	{
		boolean sameDay = false;
		if(date != null)
		{
			Calendar cal1 = Calendar.getInstance();
			cal1.setTimeZone(TimeZone.getTimeZone("UTC"));
			Calendar cal2 = Calendar.getInstance();
			cal2.setTimeZone(TimeZone.getTimeZone("UTC"));
			cal1.setTime(new Date());
			cal2.setTime(date);
			sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
					cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
		}
		return sameDay;

	}
	public int offsetMin(String local)
	{
		int lh = (local != null && local.split(":")!=null && local.split(":")[0]!=null)?Integer.parseInt(local.split(":")[0]):0;
		int ls = (local != null && local.split(":")!=null && local.split(":")[1]!=null)?Integer.parseInt(local.split(":")[1]):0;
		int clientoffset =(lh*60)+ls;
		clientoffset = clientoffset*-1;
		TimeZone tz = TimeZone.getDefault();
		int min = tz.getOffset(new Date().getTime()) / 1000 / 60;
		min = min+clientoffset;
		return min;
	}
	public Date calStartDateBasedRange(int calendarType, int rangeStart){
		Calendar calStart =  Calendar.getInstance();
		setTimeToBeginningOfDay(calStart);
		if(calendarType == 1){ // date
			calStart.set(Calendar.DATE, calStart.getActualMinimum(Calendar.DATE));
			calStart.add(Calendar.DATE, rangeStart >1 ?rangeStart-1 :0);

		}
		else if(calendarType == 2){ //week
			calStart.set(Calendar.DATE, calStart.getActualMinimum(Calendar.DATE));
			calStart.add(Calendar.DATE, rangeStart >1 ? (rangeStart-1)*7 : rangeStart-1);

		}
		else if(calendarType == 3){ //month
			calStart.set(Calendar.MONTH, calStart.getActualMinimum(Calendar.MONTH));
			calStart.set(Calendar.DATE, calStart.getActualMinimum(Calendar.DATE));
			calStart.add(Calendar.MONTH, rangeStart-1);

		}
		else if(calendarType == 4){ //year
			calStart.set(Calendar.MONTH, calStart.getActualMinimum(Calendar.MONTH));
			calStart.set(Calendar.DATE, calStart.getActualMinimum(Calendar.DATE));
			calStart.set(Calendar.YEAR, rangeStart);
		}
		else
		{
			calStart.set(Calendar.DATE, calStart.getActualMinimum(Calendar.DATE));
		}
		return calStart.getTime();


	}
	public Date calEndDateBasedRange(int calendarType, int rangeEnd){
		Calendar calEnd =  Calendar.getInstance();
		setTimeToEndofDay(calEnd);
		if(calendarType == 1){ // date
			calEnd.set(Calendar.DATE, calEnd.getActualMinimum(Calendar.DATE));
			calEnd.add(Calendar.DATE, rangeEnd-1);
		}
		else if(calendarType == 2){ //week
			calEnd.set(Calendar.DATE, calEnd.getActualMinimum(Calendar.DATE));
			calEnd.add(Calendar.DATE, (rangeEnd*7)-1);
		}
		else if(calendarType == 3){ //month
			calEnd.set(Calendar.MONTH, calEnd.getActualMinimum(Calendar.MONTH));
			calEnd.add(Calendar.MONTH, rangeEnd-1);
			calEnd.set(Calendar.DATE, calEnd.getActualMaximum(Calendar.DATE));
		}
		else if(calendarType == 4){ //year
			calEnd.set(Calendar.MONTH, calEnd.getActualMaximum(Calendar.MONTH));
			calEnd.set(Calendar.DATE, calEnd.getActualMaximum(Calendar.DATE));
			calEnd.set(Calendar.YEAR, rangeEnd);
		}
		return calEnd.getTime();

	}
	private static void setTimeToBeginningOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	private static void setTimeToEndofDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
	}

	public Date getServerTime(Date date)
	{
		Calendar c = Calendar.getInstance();
		if(date != null)
		{

			c.setTime(date);
			TimeZone tz = TimeZone.getDefault();
			int min = tz.getOffset(date.getTime()) / 1000 / 60;
			c.add(Calendar.MINUTE, min);
		}
		return (date != null)?c.getTime():null;
	}

	public static Calendar toCalendar(Date date){ 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static int getAge(Calendar dob) {	
		int age = 0;
		try
		{
			Calendar today = Calendar.getInstance();			   
			int curYear = today.get(Calendar.YEAR);			   
			int dobYear = dob.get(Calendar.YEAR);			   
			age = curYear - dobYear;			   
			// if dob is month or day is behind today's month or day			   
			// reduce age by 1			   
			int curMonth = today.get(Calendar.MONTH);			   
			int dobMonth = dob.get(Calendar.MONTH);			   
			if (dobMonth > curMonth) { // this year can't be counted!			   
				age--;			   
			} else if (dobMonth == curMonth) { // same month? check for day			   
				int curDay = today.get(Calendar.DAY_OF_MONTH);			   
				int dobDay = dob.get(Calendar.DAY_OF_MONTH);			   
				if (dobDay > curDay) { // this year can't be counted!			   
					age--;			   
				}			   
			}		
		}
		catch (Exception e) {
			logger.error(e);		
		}
		return age;			   
	}


	public static String  rounddecimalseperator(double value, int places,boolean commaseperation){
		if (places < 0) throw new IllegalArgumentException();
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		double x = (double) tmp / factor;
		StringBuilder sb  = new StringBuilder();
		String s = commaseperation?"%,.":"%.";
		sb.append(s);
		sb.append(places);
		sb.append("f");
		return  String.format(sb.toString(), x);
	}
	public String toTitleCase(String givenString) {
		String s="";
		if(givenString != null)
		{
			String[] arr = givenString.split(" ");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < arr.length; i++) {
				sb.append(Character.toUpperCase(arr[i].charAt(0)))
				.append(arr[i].substring(1).toLowerCase()).append(" ");
			}     
			s= sb.toString().trim();
		}
		else
		{
			s= null;
		}
		return s;
	} 


	public boolean isNumeric(String strNum) {
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		if (strNum == null || strNum.isEmpty()) {
			return false; 
		}
		return pattern.matcher(strNum).matches();
	}

	public String generateUserName(String email)
	{
		String username = email.split("@")[0];
		String unameSub = username.substring(0, username.length()>4?3:username.length());
		return unameSub+String.valueOf (random.nextInt(999999));
	}
	public static Object getBasicAuthHeaderforlogin(String clientId, String secretId) {
		return "Basic " + base64Encode(clientId + ":" + secretId);
	} 

	public static Object getAuthHeaderforlogin(String clientId, String secretId) {
		return base64Encode(clientId + ":" + secretId);
	}
	
	public UserProfile getUserProfile(final HttpServletRequest request) {
		if(request != null && request.getUserPrincipal()!= null){
			Principal principal = request.getUserPrincipal();
			return userService.getUserProfileByUserId(principal.getName());
		} else {
			return null;
		}
	}
}
