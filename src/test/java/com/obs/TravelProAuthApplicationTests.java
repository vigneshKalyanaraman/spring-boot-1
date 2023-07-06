package com.obs;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.obs.messages.ResponseStatus;
import com.obs.messages.ResponseStatusCode;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TravelProAuthApplication.class)
class TravelProAuthApplicationTests {

	private static final Logger logger = LogManager.getLogger(TravelProAuthApplicationTests.class);

	public static final Object STATUS = "status";
	public static final String TESTSTRING = "test";
	public static final boolean TESTBOOLEAN = false;

	@Autowired
	WebApplicationContext wac;
	protected MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	JSONParser parser = null;
	JSONObject json = null;
	ResponseStatus status = null;
	HashMap<String, String> input = null;
	String content = "";
	RequestBuilder requestBuilder = null;
	MvcResult result = null;
	LinkedMultiValueMap<String, String> requestParams = null;

	@Test
	@Rollback
	void registerDetails() {
		try {
			content = "{\"firstName\" : \"" + "" + "\",\"lastName\" : \"" + TESTSTRING + "\",\"email\" : \""
					+ TESTSTRING + "\",\"mobileNo\" : \"" + TESTSTRING + "\",\"countryCode\" : \"" + TESTSTRING + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/register").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&1 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@1 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_REQUIRED, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("registerDetails ", e);
		}
	}
	
	@Test
	@Rollback
	void registerDetails1() {
		try {
			content = "{\"firstName\" : \"" + TESTSTRING + "\",\"lastName\" : \"" + "" + "\",\"email\" : \""
					+ ""+ "\",\"mobileNo\" : \"" + TESTSTRING + "\",\"countryCode\" : \"" + TESTSTRING + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/register").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&2 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@2 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_REQUIRED, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("registerDetails ", e);
		}
	}
	
	@Test
	@Rollback
	void mobileNoCheckDetails() {
		try {
			content = "{\"mobileNo\" : \"" + "" + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/mobilecheck").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&4 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@4 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_REQUIRED, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("mobileNoCheckDetails ", e);
		}
	}
	
	@Test
	@Rollback
	void emailCheckDetails() {
		try {
			content = "{\"email\" : \"" + "" + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/emailcheck").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&7 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@7 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_REQUIRED, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("emailCheckDetails ", e);
		}
	}
	
	@Test
	@Rollback
	void createPasswordDetails() {
		try {
			content = "{\"email\" : \"" + "" + "\",\"password\" : \"" + "" + "\",\"confirmPassword\" : \""
					+ TESTSTRING + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/createpassword").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&5 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@5 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_INVALID, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("createPasswordDetails ", e);
		}
	}
	
	@Test
	@Rollback
	void createPasswordDetails1() {
		try {
			content = "{\"email\" : \"" + "" + "\",\"password\" : \"" + TESTSTRING + "\",\"confirmPassword\" : \""
					+ TESTSTRING + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/createpassword").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&6 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@6 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_INVALID, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("createPasswordDetails ", e);
		}
	}
	
	@Test
	@Rollback
	void forgotPasswordDetails() {
		try {
			content = "{\"email\" : \"" + "" + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/forgotpassword").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&8 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@8 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_REQUIRED, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("forgotPasswordDetails ", e);
		}
	}
	
	
	@Test
	@Rollback
	void socialloginDetails1() {
		try {
			content = "{\"email\" : \"" + "" + "\",\"socialId\" : \"" + "" + "\",\"type\" : \""
					+ TESTSTRING + "\",\"firstName\" : \"" + "" + "\",\"lastName\" : \"" + TESTSTRING
					+ "\",\"mobileNo\" : \"" + "" + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/sociallogin").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&10 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@10 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_REQUIRED, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("socialloginDetails1 ", e);
		}
	}
	
	@Test
	@Rollback
	void generateotpDetails1() {
		try {
			content = "{\"email\" : \"" + "" + "\",\"mobileNo\" : \"" + "" + "\",\"countryCode\" : \""
					+ TESTSTRING + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/generateotp").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&11 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@11 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_INVALID, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("generateotpDetails1 ", e);
		}
	}
	
	@Test
	@Rollback
	void otpvalidationDetails1() {
		try {
			content = "{\"email\" : \"" + "" + "\",\"mobileNo\" : \"" + TESTSTRING + "\",\"countryCode\" : \""
					+ TESTSTRING + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/otpvalidation").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&12 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@12 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_REQUIRED, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("otpvalidationDetails1 ", e);
		}
	}
	
	@Test
	@Rollback
	void loginDetails() {
		try {
			content = "{\"username\" : \"" + "" + "\",\"password\" : \"" + TESTSTRING + "\"}";
			requestBuilder = MockMvcRequestBuilders.post("/login").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&13 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@13 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_REQUIRED, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("loginDetails ", e);
		}
	}
	
	@Test
	@Rollback
	void logoutDetails() {
		try {
			content = "{\"authHeader\" : \"" + "" + "\"}";
			requestBuilder = MockMvcRequestBuilders.put("/revoke").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			logger.info("&&&14 " + content);
			result = (MvcResult) mockMvc.perform(requestBuilder).andReturn();
			logger.info("@@@14 " + result.getResponse().getContentAsString());
			parser = new JSONParser();
			json = (JSONObject) parser.parse(result.getResponse().getContentAsString());
			json = (JSONObject) json.get(STATUS);
			assertEquals(ResponseStatusCode.STATUS_INVALID, (long) json.get(STATUS));

		} catch (Exception e) {
			logger.error("logoutDetails ", e);
		}
	}
	
	
}
