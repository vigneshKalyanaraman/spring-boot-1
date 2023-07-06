package com.obs.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.obs.domain.UserProfession;

public interface UserProfessionRepo extends JpaRepository<UserProfession, Long> {

	UserProfession getById(long professionId);

}
