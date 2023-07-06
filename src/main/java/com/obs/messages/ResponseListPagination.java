package com.obs.messages;
/**
 * @author Harihara_Subramanian
 */
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
public class ResponseListPagination<T> {

	@XmlElement(name="status")
	public ResponseStatus status;

	@XmlElement(name="count")
	private long count;
	
	@XmlElement(name="notificationCount")
	private long notificationCount;
	
	@XmlElement(name="user1")
	private Collection<T> entity;
	
	public ResponseListPagination(){
		super();
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Collection<T> getEntity() {
		return entity;
	}

	public void setEntity(Collection<T> entity) {
		this.entity = entity;
	}

	public ResponseListPagination(ResponseStatus status,Collection<T> entity){
		super();
		this.status=status;
		this.entity=entity;
	}
	
	public ResponseListPagination(ResponseStatus status,long count,Collection<T> entity){
		super();
		this.status=status;
		this.entity=entity;
		this.count = count;
	}
	public ResponseListPagination(ResponseStatus status,long count,long notificationCount,Collection<T> entity){
		super();
		this.status=status;
		this.entity=entity;
		this.count = count;
		this.notificationCount = notificationCount;
	}
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getNotificationCount() {
		return notificationCount;
	}

	public void setNotificationCount(long notificationCount) {
		this.notificationCount = notificationCount;
	}

}
