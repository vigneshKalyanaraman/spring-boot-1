package com.obs.messages;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
public class ResponseListMessage<T> {

	@XmlElement(name="status")
	public ResponseStatus status;
	
	@XmlElement(name="user1")
	private Collection<T> entity;
	
	public ResponseListMessage(){
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

	public ResponseListMessage(ResponseStatus status,Collection<T> entity){
		super();
		this.status=status;
		this.entity=entity;
	}
	

}
