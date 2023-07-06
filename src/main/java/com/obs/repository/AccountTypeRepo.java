package com.obs.repository;

import org.springframework.data.repository.CrudRepository;

import com.obs.domain.AccountType;

public interface AccountTypeRepo extends CrudRepository<AccountType, Long> {

	AccountType getById(long l);
}
