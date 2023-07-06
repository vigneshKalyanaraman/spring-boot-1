package com.obs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.obs.domain.UserProfile;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, String> {

	@Query("from UserProfile where userId.userId =:userId")
	UserProfile findByUserId(String userId);
}
