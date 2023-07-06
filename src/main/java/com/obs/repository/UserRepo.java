package com.obs.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.obs.domain.User;

@Repository
public interface UserRepo extends CrudRepository<User, String> {
	
	User findByEmailId(String emailId);

	Optional<User> findByUserId(String userId);

}
