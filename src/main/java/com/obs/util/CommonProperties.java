package com.obs.util;

import java.util.ResourceBundle;


public class CommonProperties {

	private CommonProperties()
	{
	}
	private static ResourceBundle commonBundle = getResourceBundle();
	public static final String COMMON = "application";
	private static ResourceBundle getResourceBundle() {
		return getResourceBundle(COMMON);
	}
	private static ResourceBundle getResourceBundle(String bundleName) {
		ResourceBundle bundle = null;
		bundle = ResourceBundle.getBundle(bundleName);
		return ResourceBundle.getBundle(bundleName+"-"+bundle.getString("spring.profiles.active"));
	}
	private static String getString(String key) {
		return commonBundle.getString(key);
	}

	public static String getBaseURL() {
		return getString("baseURL");
	}

	public static String getBasePath() {
		return getString("basePath");
	}

	public static String getContextPath() {
		return getString("contextPath");
	}

	public static String getUiPath() {
		return getString("uiPath");
	}
	public static String getTempFilePath() {
		return getString("tempFile");
	}

	public static String getUserProfileImagePath() {
		return getString("userProfileImagePath");
	}

	public static String getImageContextPath() {
		return getString("imageContextPath");
	}

	public static String getImagePath() {
		return getString("imagePath");
	}
	public static String getsocketPath() {
		return getString("socketpath");
	}
	public static String getDocumentPath() {
		return getString("documentpath");
	}
	public static String getBucketName() {
		return getString("bucketname");
	}
	public static String getCollectionName() {
		return getString("collectionname");
	}
	public static String getUiDomain() {
		return getString("uiDomain");
	}
	public static String getUiInvite() {
		return getString("uiInvite");
	}
	public static String getPublicBaseURL() {
		return getString("publicBaseURL");
	}
	public static String getForgetPassword() {
		return getString("forgetPassword");
	}
	
}

