package com.obs.messages;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
public class ResponseMessage<T> {

	@XmlElement(name="status")
	public ResponseStatus status;

	@XmlElement(name="user1")
	private T entity;
	
	public T getEntity() {
		return entity;
	}
	public void setEntity(T entity) {
		this.entity = entity;
	}

	public ResponseMessage(){
		super();
	}

	public ResponseMessage(T entity){
		super();
		setEntity(entity);
	}

	public ResponseMessage(ResponseStatus status,T entity){
		super();
		this.status=status;
		this.entity=entity;
	}

}
