package com.obs.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.obs.dao.IUserDao;
import com.obs.domain.UserSecurity;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);


	
	@Autowired
	private IUserDao iuserDAO;

	@Autowired
	public CustomUserDetailsService(IUserDao iuserDAO) {
		this.iuserDAO = iuserDAO;
	}
	
    static int loginType=0;

	public static void setLoginType(int value) {
		loginType=value;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public UserDetails loadUserByUsername(String username)   {	
		UserSecurity usrsecure = null;
		com.obs.domain.User usr = iuserDAO.getUserById(username);
	

		if(usr != null) {
			usrsecure = iuserDAO.getUserSecurityByUserName(usr.getUserId());
			
		}
		if (usrsecure == null ) {
			throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
		}
		
		String usrname = (usrsecure.getUserId() != null)?usrsecure.getUserId().getUserId():usrsecure.getMobileNo();
	
		String upassword = (loginType==2) ? usrsecure.getSocialPassword() : usrsecure.getPassword();
	
		
		String role = usr.getAccountType().getUserType();
		
		List authList = getAuthorities(role);  
	
		return new User(usrname, upassword, authList);  
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List getAuthorities(String role) {  
		List authList = new ArrayList();  
		try
		{
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));       
		if (role != null && role.trim().length() > 0) {  
			if (role.equals("admin")) {
				authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));  
			}  
			if (role.equals("user")) {  
				authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));  
			} 
		}    
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		return authList;  
	}

	public com.obs.domain.User viewProfile(String number){
		com.obs.domain.User user=null;
		try {
			user = iuserDAO.getUserById(number);
			
		}catch(Exception e){
			logger.error("view profile", e);
		}
		return user;
	}
}
