package com.obs.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Table(name="tbl_guest_user")
@Entity
@Data
public class GuestUser {
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid",strategy="org.hibernate.id.UUIDGenerator")
	@Column(name="guest_user_id",nullable = false)
	private String guestUserId;
	
	@Column(name="device_id")
	private String deviceId;
	
	@Column(name="country")
	private String country;

}
