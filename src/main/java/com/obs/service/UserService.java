package com.obs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obs.customdomain.UserRegistrationInput;
import com.obs.dao.IUserDao;
import com.obs.domain.Country;
import com.obs.domain.DeviceToken;
import com.obs.domain.User;
import com.obs.domain.UserProfile;
import com.obs.domain.UserSecurity;

@Service
public class UserService implements IUserService{
	
	@Autowired
	private IUserDao userDao;

	@Override
	public User registerUser(UserRegistrationInput input) {
		
		return null;
	}

	@Override
	public List<Country> getAllCountry() {
		return userDao.getAllCountry();
	}

	@Override
	public User getUserByUserNameOrEmailorMobile(String userName) {
		return userDao.getUserByUserNameOrEmailorMobile(userName);
	}

	@Override
	public UserSecurity saveOrUpdateUserSecurity(UserSecurity userSecurity) {
		
		return userDao.saveOrUpdateUserSecurity(userSecurity);
	}

	@Override
	public void deleteUser(User user) {
		userDao.deleteUser(user);
		
	}

	@Override
	public User getUserById(String name) {
		return userDao.getUserById(name);
	}

	@Override
	public UserSecurity getUserSecurityByUserName(String userId) {
		return userDao.getUserSecurityByUserName(userId);
	}

	@Override
	public User saveOrUpdateUser(User user) {
		return userDao.saveOrUpdateUser(user);
	}

	@Override
	public UserSecurity getMobileNoAndEmailBySecurity(String mobileNo, String email) {
		return userDao.getMobileNoAndEmailBySecurity(mobileNo, email);
	}

	@Override
	public UserSecurity getUserSecurityByEmail(String email) {
		return userDao.getUserSecurityByEmail(email);
	}

	@Override
	public UserProfile getByUserId(String userId) {
		return userDao.getByUserId(userId);
	}

	@Override
	public UserSecurity getMobileNoySecurity(String mobileNo) {
		return userDao.getMobileNoySecurity(mobileNo);
	}

	@Override
	public User saveUser(User user) {
		return userDao.saveUser(user);
	}


	@Override
	public User updateUserById(String userId) {
		return userDao.updateUserById(userId);
	}

	@Override
	public UserProfile getUserProfileByUserId(String userId) {
		return userDao.getUserProfileByUserId(userId);
	}

	@Override
	public UserSecurity getUserSecurityByUserId(String userId) {
		return userDao.getUserSecurityByUserId(userId);
	}
	
	@Override
	public UserSecurity getUserSecurityByVerifyLink(String verifyLink) {
		return userDao.getUserSecurityByVerifyLink(verifyLink);
	}
	
	public List<User> getUserListWithSearch(String search, int pageNumber, int pageSize) {
		return userDao.getUserListWithSearch(search,pageNumber, pageSize);
	}

	@Override
	public User getUserMobileNo(String mobileNo) {
		return userDao.getUserMobileNo(mobileNo);
	}

	@Override
	public DeviceToken getDeviceTokenByUserId(String userId) {
		
		return userDao.getDeviceTokenByUserId(userId);
	}
	

}
