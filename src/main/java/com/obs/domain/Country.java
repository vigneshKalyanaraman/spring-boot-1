package com.obs.domain;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_country")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "country_id", nullable = false)
	private Long countryId;

	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "country_code_three_letter")
	private String countryCodeThreeLetter;

	@Column(name = "country_dial_code")
	private String countryDialCode;

	@Column(name = "country_name")
	private String countryName;
	
	
}
