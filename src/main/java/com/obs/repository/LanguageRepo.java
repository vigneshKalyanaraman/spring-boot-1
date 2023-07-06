package com.obs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obs.domain.Language;

@Repository
public interface LanguageRepo extends JpaRepository<Language, Long> {

}
