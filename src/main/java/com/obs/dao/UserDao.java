package com.obs.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.obs.domain.Country;
import com.obs.domain.DeviceToken;
import com.obs.domain.User;
import com.obs.domain.UserProfile;
import com.obs.domain.UserSecurity;
import com.obs.messages.MessageConstants;
import com.obs.qurey.QueryConstants;
import com.obs.repository.CountryRepo;
import com.obs.repository.DeviceTokenRepo;
import com.obs.repository.UserRepo;
import com.obs.repository.UserSecurityRepo;

import lombok.extern.slf4j.Slf4j;

@Repository
@Transactional
@Slf4j
public class UserDao implements IUserDao {

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserSecurityRepo securityRepo;

	@Autowired
	CountryRepo countryRepo;

	@Autowired
	private EntityManager em;

	@Autowired
	DeviceTokenRepo deviceToken;

	@Override
	public UserSecurity getByOtp(String otp) {
		UserSecurity user = null;
		try {
			user = securityRepo.getByOtp(otp);
		} catch (Exception e) {
			log.error("UserDao::getByOtp::", e.toString());
		}
		return user;
	}

	@Override
	public User getByPhoneNo(String number) {
		User user = null;
		try {
			user = securityRepo.getByPhoneNo(number);
		} catch (Exception e) {
			log.error("UserDao::getByPhoneNo:: ", e.toString());
		}
		return user;
	}

	@Override
	public List<Country> getAllCountry() {
		List<Country> country = null;
		try {
			country = countryRepo.findAll();
		} catch (Exception e) {
			log.error("UserDao::getAllCountry:: ", e.toString());
		}
		return country;
	}

	@Override
	public User getUserByUserNameOrEmailorMobile(String userName) {
		List<User> list = null;
		try {
			TypedQuery<User> query = em.createQuery(
					"from User where emailId =:username or mobileNo =:username and deleted=false and active=true ",
					User.class).setParameter("username", userName);
			list = query.getResultList();
		} catch (Exception e) {
			log.error("UserDao::getUserByUserNameOrEmailorMobile:: ", e.toString());
		}
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public UserSecurity saveOrUpdateUserSecurity(UserSecurity userSecurity) {
		UserSecurity security = null;
		try {
			security = securityRepo.save(userSecurity);
		} catch (Exception e) {
			log.error("UserDao::saveOrUpdateUserSecurity:: ", e.toString());
		}
		return security;
	}

	@Override
	public void deleteUser(User user) {
		try {
			userRepo.delete(user);
		} catch (Exception e) {
			log.error("UserDao::deleteUser::", e.toString());
		}

	}

	@Override
	public User getUserById(String name) {
		User user = null;
		try {
			Optional<User> userOptional = userRepo.findByUserId(name);
			user = userOptional.isPresent() ? userOptional.get() : null;
		} catch (Exception e) {
			log.error("UserDao::getUserById:: ", e.toString());
		}
		return user;
	}

	@Override
	public UserSecurity getUserSecurityByUserName(String username) {
		UserSecurity security = null;
		try {
			security = securityRepo.findByUserId(username);
		} catch (Exception e) {
			log.error("UserDao::getUserSecurityByUserName:: ", e.toString());
		}
		return security;
	}

	@Override
	public UserSecurity getUserSecurityByEmail(String email) {
		List<UserSecurity> list = null;
		try {
			TypedQuery<UserSecurity> query = em
					.createQuery("from UserSecurity where userId.emailId =: email", UserSecurity.class)
					.setParameter(MessageConstants.EMAILID, email);
			list = query.getResultList();

		} catch (Exception e) {
			log.error("UserDao::getUserSecurityByEmail:: ", e.toString());
		}
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public User saveOrUpdateUser(User user1) {
		User user = null;
		try {
			user = userRepo.save(user1);
		} catch (Exception e) {
			log.error("UserDao::getUserById:: ", e.toString());
		}
		return user;
	}

	@Override
	public UserSecurity getMobileNoAndEmailBySecurity(String mobileNo, String email) {
		List<UserSecurity> list = null;
		try {

			TypedQuery<UserSecurity> query = em
					.createQuery("from UserSecurity where mobileNo =:mobileNo and email =: email", UserSecurity.class)
					.setParameter("mobileNo", mobileNo).setParameter(MessageConstants.EMAILID, email);
			list = query.getResultList();

		} catch (Exception e) {
			log.error("UserDao::getMobileNoAndEmailBySecurity:: ", e.toString());
		}
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public UserSecurity getMobileNoySecurity(String mobileNo) {
		List<UserSecurity> list = null;
		try {

			TypedQuery<UserSecurity> query = em
					.createQuery("from UserSecurity where userId.mobileNo =:mobileNo", UserSecurity.class)
					.setParameter("mobileNo", mobileNo);

			list = query.getResultList();

		} catch (Exception e) {
			log.error("UserDao::getMobileNoySecurity:: ", e.toString());
		}
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public UserProfile getByUserId(String userId) {
		List<UserProfile> list = null;
		try {

			TypedQuery<UserProfile> query = em
					.createQuery("from UserProfile where userId.userId =:userId", UserProfile.class)
					.setParameter("userId", userId);
			list = query.getResultList();

		} catch (Exception e) {
			log.error("UserDao::getByUserId:: ", e.toString());
		}
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public User saveUser(User user) {

		User user1 = null;
		try {
			user1 = userRepo.save(user);
		} catch (Exception e) {
			log.error("UserDao::saveUser:: ", e.toString());
		}
		return user1;
	}

	@Override
	public User updateUserById(String userId) {
		User user = null;
		try {
			Optional<User> ousr = userRepo.findById(userId);
			user = ousr.isPresent() ? ousr.get() : null;
		} catch (Exception e) {
			log.error("UserDao::updateUserById:: ", e.toString());
		}
		return user;
	}

	@Override
	public UserProfile getUserProfileByUserId(String userId) {
		List<UserProfile> list = null;
		try {

			TypedQuery<UserProfile> query = em
					.createQuery("from UserProfile where userId.userId =:userId", UserProfile.class)
					.setParameter("userId", userId);
			list = query.getResultList();

		} catch (Exception e) {
			log.error("UserDao::getUserProfileByUserId:: ", e.toString());
		}
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public UserSecurity getUserSecurityByUserId(String userId) {
		UserSecurity userSecurity = null;
		try {
			userSecurity = securityRepo.findByUserId(userId);
		} catch (Exception e) {
			log.error("UserDao::getUserSecurityByUserId:: ", e.toString());
		}
		return userSecurity;
	}

	@Override
	public UserSecurity getUserSecurityByVerifyLink(String verifyLink) {
		UserSecurity userSecurity = null;
		try {
			userSecurity = securityRepo.findByVerifyLink(verifyLink);
		} catch (Exception e) {
			log.error("UserDao::getUserSecurityByVerifyLink:: ", e.toString());
		}
		return userSecurity;
	}

	@Override
	public List<User> getUserListWithSearch(String search, int pageNumber, int pageSize) {
		List<User> user = null;
		try {
			String queryString = "select s from User s where s.active = true";

			if (search != null) {
				queryString = queryString
						+ " AND ((s.gender LIKE CONCAT('%',:search,'%') OR :search is null) OR (s.firstName LIKE CONCAT('%',:search, '%') OR :search is null) OR (s.lastName LIKE CONCAT('%',:search, '%') OR :search is null) OR (s.userName LIKE CONCAT('%',:search, '%') OR :search is null) OR (s.emailId LIKE CONCAT('%',:search, '%') OR :search is null) OR (s.resident LIKE CONCAT('%',:search, '%') OR :search is null))";
			}
			TypedQuery query = em.createQuery(queryString + " order by s.userId desc ", User.class);

			if (search != null && !search.isEmpty()) {
				query.setParameter("search", "%" + search + "%");
			}
			if (pageNumber > 0 && pageSize > 0) {
				query.setFirstResult((pageNumber - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			user = query.getResultList();

		} catch (Exception e) {
			log.error("UserDao::getUserListWithSearch ", e);
		}
		return user != null && !user.isEmpty() ? user : null;
	}

	@Override
	public User getUserMobileNo(String mobileNo) {
		List<User> list = null;
		try {
			TypedQuery<User> query = em.createQuery(QueryConstants.MOBILENO, User.class)
					.setParameter(MessageConstants.MOBILENOS, mobileNo);
			list = query.getResultList();
		} catch (Exception e) {
			log.error("UserDao::getUserMobileNo ", e);
		}
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public DeviceToken getDeviceTokenByUserId(String userId) {
		List<DeviceToken> list = null;
		try {
			list = deviceToken.getDeviceTokenByUserId(userId);
		} catch (Exception e) {
			log.error("UserDao::getDeviceTokenByUserId ", e);
		}
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

}
