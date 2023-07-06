package com.obs.domain;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "tbl_device_token")
@Getter
@Setter
public class DeviceToken 
{

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name="id" , nullable = false, unique = true )	
	private String id;
	
	@Column(name = "device_type", nullable = false)
	private String type;
	
	@Column(name = "device_token", nullable = true)
	private String token;
	
	@Column(name = "created_on", nullable = true)
	private Date createdOn;
	
	@Column(name = "updated_on", nullable = true)
	private Date updatedOn;
 
	private String userId;
	
	@Column(name = "device_location", nullable = true)
	private String locationToken;
	
}
