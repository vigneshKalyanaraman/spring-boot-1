package com.obs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.obs.customdomain.UserRegistrationInput;
import com.obs.domain.User;
import com.obs.helper.AdminServiceHelper;
import com.obs.messages.ResponseListPagination;
import com.obs.messages.ResponseMessage;
import com.obs.messages.ResponseStatus;

@RestController
public class AdminController {

	@Autowired
	AdminServiceHelper adminServiceHelper;

	@PostMapping("/users")
	public @ResponseBody ResponseMessage<User> editUser(@RequestBody UserRegistrationInput input,
			HttpServletRequest request, final HttpServletResponse response) {
		return adminServiceHelper.editUser(input, request, response);
	}

	@PutMapping("/deleteuser")
	public @ResponseBody ResponseStatus deleteManageUser(@RequestParam("userId") String userId,
			HttpServletRequest request, final HttpServletResponse response) {
		return adminServiceHelper.deleteManageUser(userId, response);
	}

	@GetMapping("/viewuser")
	public @ResponseBody ResponseListPagination<User> viewUser(
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "0") int pageSize,
			HttpServletRequest request, final HttpServletResponse response) {
		return adminServiceHelper.viewUser(search, pageNumber, pageSize, request, response);
	}

	@GetMapping("/viewexportuser")
	public @ResponseBody String exportViewUser(@RequestParam(value = "search", required = false) String search,
			HttpServletRequest request, HttpServletResponse response) {
		return adminServiceHelper.exportViewUser(search,request, response);
	}

}
