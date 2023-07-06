package com.obs.messages;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONObject;


@XmlRootElement(name="user")
public class ResponseMessageWithToken<T> {

		@XmlElement(name="status")
		public ResponseStatus status;

		@XmlElement(name="user1")
		private T entity;
		
		@XmlElement(name="login")
		private JSONObject login;

		public ResponseMessageWithToken(ResponseStatus status, T entity, JSONObject login) {
			super();
			this.status = status;
			this.entity = entity;
			this.login = login;
		}
		public T getEntity() {
			return entity;
		}
		public void setEntity(T entity) {
			this.entity = entity;
		}

		public ResponseMessageWithToken(){
			super();
		}

		public ResponseMessageWithToken(T entity){
			super();
			setEntity(entity);
		}

		public ResponseMessageWithToken(ResponseStatus status,T entity){
			super();
			this.status=status;
			this.entity=entity;
		}
		public JSONObject getLogin() {
			return login;
		}
		public void setLogin(JSONObject login) {
			this.login = login;
		}

	

}
