package com.obs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obs.domain.AccountStatus;

@Repository
public interface AccountStatusRepo extends JpaRepository<AccountStatus, Long> {

	AccountStatus getById(long l);

}
