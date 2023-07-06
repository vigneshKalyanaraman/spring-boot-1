package com.obs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import com.obs.domain.DeviceToken;

public interface DeviceTokenRepo extends CrudRepository<DeviceToken, String> {

	@Query("from DeviceToken where userId =:userId")
	List<DeviceToken> getDeviceTokenByUserId(String userId);

}
