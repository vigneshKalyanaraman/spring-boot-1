package com.obs.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.obs.customdomain.MyProfileInput;
import com.obs.domain.GuestUser;
import com.obs.domain.User;
import com.obs.domain.UserProfile;
import com.obs.messages.MessageConstants;
import com.obs.messages.PropertyConstants;
import com.obs.messages.ResponseMessage;
import com.obs.messages.ResponseStatus;
import com.obs.messages.ResponseStatusCode;
import com.obs.repository.GuestUserRepo;
import com.obs.repository.UserProfessionRepo;
import com.obs.repository.UserProfileRepo;
import com.obs.service.IUserService;
import com.obs.util.CommonUtils;
import com.obs.validation.BasicValidation;
import com.obs.validation.UserValidation;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfileServiceHelper {

	private static final String DATEFORMAT = "dd/MM/yyyy";

	@Autowired
	Environment env;

	@Autowired
	IUserService userService;
	
	@Autowired
	CommonUtils commonUtils;
	
	@Autowired
	GuestUserRepo guestRepo;
	
	@Autowired
	UserProfileRepo profileRepo;
	
	@Autowired
	BasicValidation basicValidation;
	
	@Autowired
	UserProfessionRepo professionRepo;
	
	@Autowired
	UserValidation userValidation;
	
	
	public ResponseMessage<UserProfile> viewUser(HttpServletRequest request,HttpServletResponse res) {
		ResponseStatus status = null;
		UserProfile profile = null;
		try {
			profile = commonUtils.getUserProfile(request);

			if (profile != null) {
				if (Objects.nonNull(profile.getUserId().getDob())) {
					SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);
					String parseddate = formatter.format(profile.getUserId().getDob());
					profile.setDob(parseddate);
				}
				res.setStatus(ResponseStatusCode.STATUS_OK);
				status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD,
						env.getProperty(PropertyConstants.NORECORD));
			}
		} catch (Exception e) {
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, 
					env.getProperty(PropertyConstants.INTERNALERROR));
			log.error("ProfileServiceHelper::viewUser::"+e.toString());
		}
		return new ResponseMessage<>(status, profile);
	}
	


	public ResponseMessage<UserProfile> editUser(MyProfileInput profileObj, HttpServletRequest request,
			HttpServletResponse res) {
		ResponseStatus status = null;
		User user = null;
		User users = null;
		String validation = "";
		UserProfile profile = null;
		try {
			validation = userValidation.validateEditUser(profileObj);
			if (validation.equals(MessageConstants.SUCCESS)) {
			user = commonUtils.getUser(request);
		
			if (user != null) {
				   
//				users = userService.getUserMobileNo(profileObj.getMobileNo());
//				if(users != null) {
//					res.setStatus(ResponseStatusCode.STATUS_OK);
//					status = new ResponseStatus(ResponseStatusCode.STATUS_ALREADY_EXISTS, MessageConstants.MOBILEALREADYEXIST);
//					return new ResponseMessage<>(status,null);
//				}

				user.setCountryCode((profileObj.getCountryCode() != null && !profileObj.getCountryCode().isEmpty())
						? profileObj.getCountryCode()
						: user.getCountryCode());
				
				user.setFirstName((profileObj.getFirstName() != null && !profileObj.getFirstName().isEmpty())
						? profileObj.getFirstName() : user.getFirstName());		
				user.setLastName((profileObj.getLastName() != null && !profileObj.getLastName().isEmpty())
						? profileObj.getLastName()
						: user.getLastName());
				if(Objects.nonNull(profileObj.getDob())) {
					SimpleDateFormat formatter1 = new SimpleDateFormat(DATEFORMAT);
					  Date date = formatter1.parse(profileObj.getDob());  
					  user.setDob(date);
				}		
				user.setProfession(profileObj.getProfessionId()>0?professionRepo.getById(profileObj.getProfessionId()): user.getProfession());
				user.setGender(profileObj.getGender());
				if(basicValidation.checkStringnullandempty(profileObj.getEmail()))
				{
					if(basicValidation.emailValidation(profileObj.getEmail()))
					{
			  	      user.setEmailId(profileObj.getEmail());
					}
					else
					{
						status = new ResponseStatus(ResponseStatusCode.STATUS_NOTMATCHED,MessageConstants.EMAIL);
						return new ResponseMessage<>(status,null);
					}
				}
				user.setMobileNo(profileObj.getMobileNo() != null && !profileObj.getMobileNo().isEmpty()? profileObj.getMobileNo()
					: user.getMobileNo());
			
				user.setProfileImage(profileObj.getProfileImage());
				user.setResident(profileObj.getResident());
				
				user = userService.saveOrUpdateUser(user);
				
				profile = commonUtils.getUserProfile(request);
				if(Objects.nonNull(profileObj.getDob())) {
					SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);
					  String parseddate = formatter.format(user.getDob());  
					  profile.setDob(parseddate);
				}
				
				profile.setProfileImage(profileObj.getProfileImage());
				profile.setUserId(user);
				
				profile = profileRepo.save(profile);
				res.setStatus(ResponseStatusCode.STATUS_OK);
				status = new ResponseStatus(ResponseStatusCode.STATUS_OK, PropertyConstants.UPDATEDSUCCESSFULLY);
			}
			else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, validation);
				return new ResponseMessage<>(status, null);
			}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD,
						env.getProperty(PropertyConstants.NORECORD));
			}
		} catch (Exception e) {
			log.info("ProfileServiceHelper::editUser"+e.toString());
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR,
					env.getProperty(PropertyConstants.INTERNALERROR));
		}
		return new ResponseMessage<>(status, profile);
	}


	public ResponseMessage<GuestUser> createGuestUser(MyProfileInput profileObj,
			HttpServletResponse response) {
		ResponseStatus status = null;
		GuestUser guest = null;
		try {
			guest = new GuestUser();
			guest.setCountry(profileObj.getCountry());
			guest.setDeviceId(profileObj.getDeviceId());
			
			guest = guestRepo.save(guest);
			response.setStatus(ResponseStatusCode.STATUS_OK);
			status = new ResponseStatus(ResponseStatusCode.STATUS_OK,
					env.getProperty(MessageConstants.SUCCESS));			
		}catch(Exception e) {
			log.info("ProfileServiceHelper::createGuestUser"+e.toString());
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR,
					env.getProperty(PropertyConstants.INTERNALERROR));
		}
		return new ResponseMessage<>(status, guest);
	}
	
	public ResponseMessage<UserProfile> updateUserImage(MyProfileInput profileObj, HttpServletRequest request) {
		ResponseStatus status = null;
		User user = null;
		UserProfile profile = null;
		try {
			user = commonUtils.getUser(request);
			if (user != null) {
				profile = commonUtils.getUserProfile(request);
				profile.setProfileImage(profileObj.getProfileImage());
				profile = profileRepo.save(profile);
				status = new ResponseStatus(ResponseStatusCode.STATUS_OK, PropertyConstants.UPDATEDSUCCESSFULLY);
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD,
						env.getProperty(PropertyConstants.NORECORD));
			}
		} catch (Exception e) {
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR,
					env.getProperty(PropertyConstants.INTERNALERROR));
		}
		return new ResponseMessage<>(status, profile);
	}
}
