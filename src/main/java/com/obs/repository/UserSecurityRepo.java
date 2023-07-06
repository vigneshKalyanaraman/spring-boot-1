package com.obs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.obs.domain.User;
import com.obs.domain.UserSecurity;

@Repository
public interface UserSecurityRepo extends JpaRepository<UserSecurity, String> {
	
	@Query("select userId from UserSecurity where otp =:otp")
	UserSecurity getByOtp(String otp);
	
	@Query("select userId from UserSecurity where mobileNo =:number")
	User getByPhoneNo(String number);
	
	
	UserSecurity findByEmail(String email);
	
	@Query("from UserSecurity where userId.userId =:userId")
	UserSecurity findByUserId(String userId);
	
	
	@Query("from UserSecurity where email =:email")
	UserSecurity getByEmail(String email);
	
	@Query("from UserSecurity where verifyLink =:verifyLink")
	UserSecurity findByVerifyLink(String verifyLink);
	


}
