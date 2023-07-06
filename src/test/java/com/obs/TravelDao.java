package com.obs;

import static org.junit.Assert.assertNull;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;


import com.obs.domain.User;
import com.obs.domain.UserProfile;
import com.obs.domain.UserSecurity;
import com.obs.service.UserService;


class TravelDao extends TravelProAuthApplicationTests {

	private static final Logger logger = LogManager.getLogger(TravelDao.class);

	@Autowired
	UserService userService;



	@Test
	@Rollback
	void getUserSecurityByUserIdDAO() {
		try {
			UserSecurity list = userService.getUserSecurityByUserId(TESTSTRING);
			assertNull(list);

		} catch (Exception e) {
			logger.error("getUserSecurityByUserIdDAO ", e);
		}
	}


	@Test
	@Rollback
	void updateUserByIdDAO() {
		try {
			User list = userService.updateUserById(TESTSTRING);
			assertNull(list);

		} catch (Exception e) {
			logger.error("updateUserByIdDAO ", e);
		}
	}
	
	@Test
	@Rollback
	void getUserByIdDAO() {
		try {
			User list = userService.getUserById(TESTSTRING);
			assertNull(list);

		} catch (Exception e) {
			logger.error("getUserByIdDAO ", e);
		}
	}
	
	@Test
	@Rollback
	void getMobileNoySecurityDAO() {
		try {
			UserSecurity list = userService.getMobileNoySecurity(TESTSTRING);
			assertNull(list);

		} catch (Exception e) {
			logger.error("getMobileNoySecurity ", e);
		}
	}
	
	@Test
	@Rollback
	void getMobileNoAndEmailBySecurityDAO() {
		try {
			UserSecurity list = userService.getMobileNoAndEmailBySecurity(TESTSTRING, TESTSTRING);
			assertNull(list);

		} catch (Exception e) {
			logger.error("getMobileNoAndEmailBySecurityDAO ", e);
		}
	}
	
	@Test
	@Rollback
	void getUserSecurityByEmailDAO() {
		try {
			UserSecurity list = userService.getUserSecurityByEmail(TESTSTRING);
			assertNull(list);

		} catch (Exception e) {
			logger.error("getUserSecurityByEmailDAO ", e);
		}
	}

	@Test
	@Rollback
	void getUserSecurityByUserNameDAO() {
		try {
			UserSecurity list = userService.getUserSecurityByUserName(TESTSTRING);
			assertNull(list);

		} catch (Exception e) {
			logger.error("getUserSecurityByUserNameDAO ", e);
		}
	}
	
	@Test
	@Rollback
	void getUserProfileByUserIdDAO() {
		try {
			UserProfile list = userService.getUserProfileByUserId(TESTSTRING);
			assertNull(list);

		} catch (Exception e) {
			logger.error("getUserProfileByUserIdDAO ", e);
		}
	}
	
	@Test
	@Rollback
	void getByUserIdDAO() {
		try {
			UserProfile list = userService.getByUserId(TESTSTRING);
			assertNull(list);

		} catch (Exception e) {
			logger.error("getByUserIdDAO ", e);
		}
	}

}
