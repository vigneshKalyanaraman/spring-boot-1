package com.obs.dao;

import java.util.List;

import com.obs.domain.Country;
import com.obs.domain.DeviceToken;
import com.obs.domain.User;
import com.obs.domain.UserProfile;
import com.obs.domain.UserSecurity;

public interface IUserDao {

	UserSecurity getByOtp(String otp);

	User getByPhoneNo(String number);

	List<Country> getAllCountry();

	User getUserByUserNameOrEmailorMobile(String userName);

	UserSecurity saveOrUpdateUserSecurity(UserSecurity userSecurity);

	void deleteUser(User user);

	User getUserById(String name);
	
	UserSecurity getUserSecurityByUserName(String userId);
	
	User saveOrUpdateUser(User user);
	
	UserSecurity getMobileNoAndEmailBySecurity(String mobileNo,String email);

	UserSecurity getUserSecurityByEmail(String email);
	
	UserProfile getByUserId(String userId);

	UserSecurity getMobileNoySecurity(String mobileNo);
	
	User saveUser(User user);
	
	User updateUserById(String userId);

	UserProfile getUserProfileByUserId(String userId);

	UserSecurity getUserSecurityByUserId(String userId);

	UserSecurity getUserSecurityByVerifyLink(String verifyLink);
	
	List<User> getUserListWithSearch(String search, int pageNumber, int pageSize);
	
	User getUserMobileNo(String mobileNo);
	
	DeviceToken getDeviceTokenByUserId(String userId);
}

