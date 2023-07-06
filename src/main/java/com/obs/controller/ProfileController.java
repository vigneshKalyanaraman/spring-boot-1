package com.obs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.obs.customdomain.MyProfileInput;
import com.obs.domain.GuestUser;
import com.obs.domain.UserProfile;
import com.obs.helper.ProfileServiceHelper;
import com.obs.messages.ResponseMessage;

@RestController
public class ProfileController {

	@Autowired
	ProfileServiceHelper userServiceHelper;

	@GetMapping("/viewProfile")
	public @ResponseBody ResponseMessage<UserProfile> viewUser(HttpServletRequest request, final HttpServletResponse response) {
		return userServiceHelper.viewUser( request, response);
	}

	@PostMapping("/editProfile")
	public @ResponseBody ResponseMessage<UserProfile> editUser(@RequestBody MyProfileInput profileObj,
			HttpServletRequest request, final HttpServletResponse response) {
		return userServiceHelper.editUser(profileObj, request, response);
	}
	
	@PostMapping("/guestuser")
	public @ResponseBody ResponseMessage<GuestUser> createGuestUser(@RequestBody MyProfileInput profileObj,
			HttpServletRequest request, final HttpServletResponse response) {
		return userServiceHelper.createGuestUser(profileObj, response);
	}
	
	@PutMapping("/profile")
	public @ResponseBody ResponseMessage<UserProfile> updateUser(@RequestBody MyProfileInput profileObj,
			HttpServletRequest request, final HttpServletResponse response) {
		return userServiceHelper.updateUserImage(profileObj, request);
	}
}
