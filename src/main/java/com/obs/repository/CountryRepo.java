package com.obs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obs.domain.Country;

@Repository
public interface CountryRepo extends JpaRepository<Country, Long> {
	
  Boolean existsByCountryDialCode(String countryDialCode);
}
