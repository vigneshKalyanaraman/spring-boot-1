package com.obs.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.obs.customdomain.SocialLoginInput;
import com.obs.customdomain.TokenInput;
import com.obs.customdomain.UserRegistrationInput;
import com.obs.domain.Country;
import com.obs.domain.DeviceToken;
import com.obs.domain.User;
import com.obs.domain.UserProfession;
import com.obs.helper.UserServiceHelper;
import com.obs.messages.ResponseListMessage;
import com.obs.messages.ResponseMessage;
import com.obs.messages.ResponseStatus;

@RestController
public class LoginController {
	
	@Autowired
	UserServiceHelper serviceHelper;
	
	@GetMapping("/test")
	public String test() {
		return "test";
	}
	
	@GetMapping("/country")
	public ResponseListMessage<Country> getCountry(HttpServletResponse response){
		return serviceHelper.getAllCountry(response);
	}
	
	@GetMapping
	public ResponseListMessage<UserProfession> getProfession(HttpServletResponse response){
		return serviceHelper.getProfessionRepo(response);		
	}
	
	@PostMapping("/register")
	public ResponseMessage<User> regiterUser(@RequestBody UserRegistrationInput input,HttpServletResponse res){
		return serviceHelper.registerUser(input, res);
	}
	
	@PostMapping("/emailcheck")
	public ResponseMessage<String> emailCheck(@RequestBody  UserRegistrationInput input,HttpServletResponse res){
		return serviceHelper.emailCheck(input, res);
	}
	
	@PostMapping("/mobilecheck")
	public ResponseMessage<String> mobileCheck(@RequestBody  UserRegistrationInput input,HttpServletResponse res){
		return serviceHelper.mobileCheck(input, res);
	}
	
	@PostMapping("/createpassword")
	public ResponseMessage<String> createPassword(@RequestBody  UserRegistrationInput input,HttpServletResponse res){
		return serviceHelper.createPassword(input, res);
	}
	
	@PostMapping("/login")
	public @ResponseBody JSONObject doLogin(@FormParam("username") String username,
			@FormParam("password") String password, 
	 HttpServletRequest request, final HttpServletResponse res) {
		return serviceHelper.doLogin(username, password,  request, res);
	}
	
	@PostMapping("/otpvalidation")
	public ResponseMessage<String> validateUser(@RequestBody  UserRegistrationInput input,HttpServletResponse res){
		return serviceHelper.otpVerification(input, res);
	}
	
	@PostMapping("/generateotp")
	public ResponseMessage<JSONObject> generateOtp(@RequestBody UserRegistrationInput input,HttpServletResponse res){
		return serviceHelper.generateOtp(input, res);
	}
	
	@PostMapping("/forgotpassword")
	public @ResponseBody ResponseMessage<JSONObject> forgotPassword(@RequestBody  UserRegistrationInput userObj,HttpServletRequest request,final HttpServletResponse response)
	{
		return serviceHelper.forgotPassword(userObj, response);
	}
	
	@DeleteMapping("/user/{emailId}")
	public @ResponseBody ResponseMessage<String> removeUser(@PathVariable(value = "emailId") String emailId,
			HttpServletRequest request, final HttpServletResponse response) {
		return serviceHelper.removeUser(emailId, response);
	}
	
	@PostMapping("/sociallogin")
	public @ResponseBody ResponseMessage<JSONObject> socialLogin(@RequestBody SocialLoginInput input,
			 final HttpServletResponse response) {
		return serviceHelper.socialLogin(input,response);
	}
	
	@PutMapping("/revoke")
	public @ResponseBody ResponseStatus logout(HttpServletRequest request) {
		return serviceHelper.logout(request);	
	}
	
	@PutMapping("/changepassword")
	public @ResponseBody ResponseMessage<User> changePassword(@RequestBody UserRegistrationInput input,HttpServletRequest request,final HttpServletResponse response){
		return serviceHelper.changePassword(input, request, response);
	}
	
	@PostMapping("/forgotpasswords")
	public @ResponseBody ResponseMessage<JSONObject> adminForgotPassword(@RequestBody  UserRegistrationInput input,HttpServletRequest request,final HttpServletResponse response)
	{
		return serviceHelper.adminForgotPassword(input, response);
	}
	
	@PutMapping("/resetpassword")
	public @ResponseBody ResponseMessage<JSONObject> resetPassword(@RequestBody UserRegistrationInput input){
		return serviceHelper.resetPassword(input);
	}
	
	@PutMapping("/devicetoken")
	public @ResponseBody ResponseMessage<DeviceToken> updateDeviceToken(@RequestBody TokenInput input,HttpServletRequest request,final HttpServletResponse response){
		return serviceHelper.updateDeviceToken(input, request, response);
	}
	
	@DeleteMapping("/useraccount")
	public @ResponseBody ResponseStatus deleteUser(
			HttpServletRequest request, final HttpServletResponse response) {
		return serviceHelper.deleteUser(request, response);
	}
	
	

}
