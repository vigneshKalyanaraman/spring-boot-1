package com.obs.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obs.dao.IUserDao;
import com.obs.domain.User;



@RestController
public class UserController
{
	
	  @Autowired
	  private IUserDao userService;
	
	  @GetMapping("/authenticate")
	  public ResponseEntity<Principal> user(Principal user) {
	   return ResponseEntity.<Principal>ok(user);
	  }	 
	  
	  @GetMapping("/getuserbyusername")
	  public ResponseEntity<User> getUserByUsername(Principal principal){
	    User user = userService.getUserById(principal.getName());
	    return ResponseEntity.<User>ok(user);    
	  }

}
