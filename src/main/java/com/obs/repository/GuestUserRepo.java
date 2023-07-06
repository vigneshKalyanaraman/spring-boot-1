package com.obs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.obs.domain.GuestUser;

public interface GuestUserRepo extends JpaRepository<GuestUser, String> {

}
