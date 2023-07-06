package com.obs.messages;

public class ResponseStatusCode {
	private ResponseStatusCode(){}
	public static final int STATUS_OK 				= 200;
	public static final int STATUS_UNAUTHORIZED 	= 401;
	public static final int STATUS_INVALID 	= 400;
	public static final int STATUS_CONFLICT 		= 409;
	public static final int STATUS_GONE 	= 410;
	public static final int STATUS_INTERNAL_ERROR 	= 500;
	public static final int STATUS_REQUIRED 		= 230;
	public static final int STATUS_NOTMATCHED 		= 229;
	public static final int STATUS_NORECORD 		= 204;
	public static final int STATUS_OTP_EXPIRED		= 227;
	public static final int STATUS_OTP_ATTEMPT_EXCEEDED		= 228;
	public static final int STATUS_ALREADY_EXISTS	= 302;	
	public static final int STATUS_PULLFAIL		= 231;
	public static final int STATUS_PUSHFAIL		= 232;
	public static final int EXCESSAMOUNT		= 233;
	public static final int SOURCEAMOUNTLOW    = 234;
	public static final int INVALIDDEELLOCATION    = 235;
	public static final int REJECTTRANSACTIONSCORE    = 236;
	public static final int REVIEWTRANSACTIONSCORE    = 237;
	public static final int NOTVERIFIED    = 238;
	public static final int NODEFAULT    = 239;
	public static final int STATUS_FRAUD    = 240;
	public static final int STATUS_API_EXCEEDED		= 241;
	public static final int STATUS_TOKEN_EXPIRED		= 242;
	public static final int STATUS_NOTEXIST = 243;
	public static final int STATUS_NOTEXPIRED = 244;
	public static final int STATUS_NOTACCEPTABLE = 406;
}