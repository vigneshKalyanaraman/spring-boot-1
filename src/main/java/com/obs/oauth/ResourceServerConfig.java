package com.obs.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends
ResourceServerConfigurerAdapter {
	
	private static final String RESOURCE_ID = "restservice";
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources
		.resourceId(RESOURCE_ID);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/user/**","/country","/register","/oauth/token","/login","/otpvalidation","/emailcheck","/generateotp","/forgotpassword","/createpassword","/mobilecheck","/sociallogin**","/forgotpasswords**","/resetpassword**").permitAll()
		.anyRequest()
		.fullyAuthenticated();
	}
	
	 @Bean
	   public RestTemplate getRestTemplate() {
	      return new RestTemplate();
	   }
}
