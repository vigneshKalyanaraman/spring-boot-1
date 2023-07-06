package com.obs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import lombok.Data;

@Table(name="tbl_account_type")
@Entity
@Data
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class AccountType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id",nullable = false)
	private Long id;
	
	@Column(name="account_type")
	private String userType;
}
