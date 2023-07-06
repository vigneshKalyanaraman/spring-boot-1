package com.obs.messages;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseStatus {

	@XmlElement(name="status")
	public int status=0;

	@XmlElement(name="message")
	public String message="";
	
	

	public ResponseStatus(){
		super();
	}

	public ResponseStatus(int status, String message){
		super();
		this.status =status;
		this.message=message;
	}

	public ResponseStatus(ResponseStatus status2) {
		super();
		if(status2 != null)
		{
		this.status =status2.status;
		this.message=status2.message;
		}
	}
	
}
