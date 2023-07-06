package com.obs.twilio;

import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SendSMS {

	private static final String ACCOUNT_SID = "AC516f6f9aa5107288b4bee2be60361747";
	private static final String AUTH_ID = "9deadc245d7f59aeb73341a44336b8b7";

	static {
		Twilio.init(ACCOUNT_SID, AUTH_ID);
	}

	public void sendSMS(String toNumber, String msg) {
		Message.creator(new PhoneNumber(toNumber), new PhoneNumber("+16205089373"), (msg)).create();
	}

}
