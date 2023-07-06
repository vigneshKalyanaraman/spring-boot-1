
package com.obs.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.obs.customdomain.SocialLoginInput;
import com.obs.customdomain.TokenInput;
import com.obs.customdomain.UserRegistrationInput;
import com.obs.domain.Country;
import com.obs.domain.DeviceToken;
import com.obs.domain.User;
import com.obs.domain.UserProfession;
import com.obs.domain.UserProfile;
import com.obs.domain.UserSecurity;
import com.obs.email.EmailManager;
import com.obs.enums.LoginType;
import com.obs.messages.MessageConstants;
import com.obs.messages.ResponseListMessage;
import com.obs.messages.ResponseMessage;
import com.obs.messages.ResponseStatus;
import com.obs.messages.ResponseStatusCode;
import com.obs.messages.SMSConstants;
import com.obs.repository.AccountStatusRepo;
import com.obs.repository.AccountTypeRepo;
import com.obs.repository.CountryRepo;
import com.obs.repository.DeviceTokenRepo;
import com.obs.repository.UserProfessionRepo;
import com.obs.repository.UserProfileRepo;
import com.obs.repository.UserRepo;
import com.obs.repository.UserSecurityRepo;
import com.obs.service.CustomUserDetailsService;
import com.obs.service.IUserService;
import com.obs.twilio.SendSMS;
import com.obs.util.CommonProperties;
import com.obs.util.CommonUtils;
import com.obs.validation.BasicValidation;
import com.obs.validation.UserValidation;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceHelper {

	private static final String PASS = "password";

	@Autowired
	IUserService userService;

	@Autowired
	UserValidation userValidation;

	@Autowired
	UserRepo userRepo;

	@Autowired
	AccountTypeRepo accountTypeRepo;

	@Autowired
	CountryRepo countryRepo;

	@Autowired
	PasswordEncoder passwordencoder;

	@Autowired
	BasicValidation basicValidation;

	@Autowired
	AccountStatusRepo statusRepo;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	SendSMS sms;

	@Autowired
	CommonUtils commonUtils;

	@Autowired
	UserProfessionRepo professionRepo;

	@Autowired
	UserProfileRepo profileRepo;

	@Autowired
	UserSecurityRepo userSecurityRepo;

	@Autowired
	private DefaultTokenServices tokenService;

	@Autowired
	DeviceTokenRepo deviceToken;

	private static final String ERROR = "error";
	private static final String ERRORDESCRIPTION = "error_description";
	private static final String ERRORCODE = "error_code";
	private static final String GENERATEDSUCCESSFULLY = "Generated Successfully";
	private static final String STATUS = "status";

	public ResponseMessage<User> registerUser(UserRegistrationInput input, HttpServletResponse res) {
		ResponseStatus status = null;
		String validation = "";
		User user = null;
		try {
			validation = userValidation.validateUser(input);
			if (validation.equals(MessageConstants.SUCCESS)) {
				String userCheck = checkUserExistence(input);
				user = basicValidation.checkStringnullandempty(userCheck) ? null : createOrUpdateUser(input);
				if (user != null) {
					status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
				} else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_ALREADY_EXISTS,
							MessageConstants.USEREXISTS + " with " + userCheck);
					return new ResponseMessage<>(status, null);
				}

			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, validation);
				res.setStatus(ResponseStatusCode.STATUS_REQUIRED);
			}
		} catch (Exception e) {
			log.error(e.toString());
		}

		return new ResponseMessage<>(status, user);
	}

	private User createOrUpdateUser(UserRegistrationInput input) {
		User user = null;
		try {
			user = input.getUserId() != null ? userService.getUserById(input.getUserId()) : new User();
			user.setAccountType(accountTypeRepo.getById(1L));
			user.setFirstName(input.getFirstName());
			user.setLastName(input.getLastName());
			user.setCreatedOn(new Date());
			user.setUpdatedOn(new Date());
			user.setEmailId(input.getEmail());
			user.setActive(true);
			user.setRegisterType(input.getRegisterType());
			user.setMobileNo(input.getMobileNo());
			user.setUserName(input.getFirstName() + " " + input.getLastName());
			user.setCountryCode(input.getCountryCode());
			user.setUserName(input.getFirstName() + " " + input.getLastName());
			user.setAccountStatus(statusRepo.getOne(1L));
			user.setResident(input.getResident());
			user = userService.saveUser(user);
			UserProfile profile = new UserProfile();
			profile.setUserId(user);
			profile.setCreatedOn(new Date());
			profile.setUpdatedOn(new Date());
			profileRepo.save(profile);
			saveUserSecurity(user, input);
		} catch (Exception e) {
			log.error("UserServiceHelper::createOrUpdateUser" + e.toString());
		}
		return user;
	}

	private UserSecurity saveUserSecurity(User user, UserRegistrationInput userObj) {
		UserSecurity userSecurity = null;
		try {
			userSecurity = new UserSecurity();
			userSecurity.setUserId(user);
			userSecurity.setEmail(userObj.getEmail());
			userSecurity.setMobileNo(userObj.getMobileNo());
			userSecurity.setSocialType(userObj.getSocialType());
			if (basicValidation.checkStringnullandempty(userObj.getSocialId())) {

				userSecurity.setPassword(passwordencoder.encode(userObj.getSocialId()));
				userSecurity.setSocial(true);
			}

			userSecurity = userService.saveOrUpdateUserSecurity(userSecurity);

		} catch (Exception e) {
			log.error(e.toString());
			userService.deleteUser(user);
		}
		return userSecurity;
	}

	private UserSecurity saveUserSecurityInfo(User user, SocialLoginInput input) {
		UserSecurity userSecurity = null;
		try {
			userSecurity = new UserSecurity();
			userSecurity.setUserId(user);
			userSecurity.setSocialPassword(passwordencoder.encode(input.getEmail()));
			userSecurity.setSocial(true);
			userSecurity = userSecurityRepo.save(userSecurity);
		} catch (Exception e) {
			log.error("saveUserSecurityInfo ", e);
			userService.deleteUser(user);
		}
		return userSecurity;
	}

	private String checkUserExistence(UserRegistrationInput userObj) {

		String email = emailExists(userObj) ? userObj.getEmail() : null;
		String mobile = mobileExists(userObj) ? userObj.getMobileNo() : null;
		String result = null;
		if (email != null && mobile != null) {
			result = email + " and " + mobile;
		} else if (email != null) {
			result = email;
		} else if (mobile != null) {
			result = mobile;
		}
		return result;
	}

	private boolean emailExists(UserRegistrationInput userObj) {
		boolean userExists = false;
		User user = userObj.getEmail() != null ? userService.getUserByUserNameOrEmailorMobile(userObj.getEmail())
				: null;
		if (user != null) {
			userExists = true;
		}
		return userExists;
	}

	private boolean mobileExists(UserRegistrationInput userObj) {
		boolean userExists = false;
		User user = userObj.getMobileNo() != null ? userService.getUserByUserNameOrEmailorMobile(userObj.getMobileNo())
				: null;
		if (user != null) {
			userExists = true;
		}
		return userExists;
	}

	public ResponseMessage<String> mobileCheck(UserRegistrationInput input, HttpServletResponse res) {
		ResponseStatus status = null;
		boolean userExists = false;
		boolean validation = false;
		try {
			validation = basicValidation.checkStringnullandempty(input.getMobileNo());
			if (validation) {
				userExists = mobileExists(input);
				if (userExists) {
					status = new ResponseStatus(ResponseStatusCode.STATUS_ALREADY_EXISTS, MessageConstants.USEREXISTS);
				} else {
					res.setStatus(ResponseStatusCode.STATUS_OK);
					status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD, MessageConstants.NORECORD);
				}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, MessageConstants.MOBILENO);
			}
		} catch (Exception e) {
			log.error(e.toString());
		}

		return new ResponseMessage<>(status, "");
	}

	private JSONObject accessToken(String username, String password) {
		String baseUrl = CommonProperties.getBaseURL() + CommonProperties.getContextPath() + MessageConstants.OAUTH;
		log.info(username);
		HashMap<String, String> input = new HashMap<>();
		User user = null;
		UserSecurity usc = userService.getUserSecurityByUserName(username);
		JSONObject json1 = new JSONObject();
		try {
			user = usc.getUserId();
			input.put(PASS, password);
			input.put("grant_type", PASS);
			input.put("username", username);

			String body = getDataString(input);
			log.info("BODY************** " + body);
			// create headers
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/x-www-form-urlencoded");
			headers.setBasicAuth(MessageConstants.OAUTHTC, MessageConstants.OAUTHTC);
			// build the request
			HttpEntity<String> entity = new HttpEntity<>(body, headers);
			log.info("ENTITY************** " + entity.toString());
			// send POST request
			ResponseEntity<JSONObject> response = restTemplate.postForEntity(baseUrl, entity, JSONObject.class);
			log.info("RESPONSE************** " + response.toString());
			json1 = response.getBody();

			if (response.getStatusCodeValue() == 200) {
				user.setInValidAttempts(0L);
				userService.saveOrUpdateUser(user);
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e.toString());
		}
		return json1;
	}

	private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}
		return result.toString();
	}

	public ResponseListMessage<Country> getAllCountry(HttpServletResponse response) {
		List<Country> country = null;
		ResponseStatus status = null;
		try {
			country = userService.getAllCountry();
			response.setStatus(ResponseStatusCode.STATUS_OK);
			status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
		} catch (Exception e) {
			log.error(e.toString());
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.toString());
		}
		return new ResponseListMessage<>(status, country);
	}

	public ResponseListMessage<UserProfession> getProfessionRepo(HttpServletResponse response) {
		List<UserProfession> profession = null;
		ResponseStatus status = null;
		try {
			profession = professionRepo.findAll();
			response.setStatus(ResponseStatusCode.STATUS_OK);
			status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
		} catch (Exception e) {
			log.error(e.toString());
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.toString());
		}
		return new ResponseListMessage<>(status, profession);
	}

	public JSONObject doLogin(String username, String password, HttpServletRequest request, HttpServletResponse res) {
		log.info(">>>>>doLogin>>>>>>>>>>>>>>>");
		User user = userService.getUserByUserNameOrEmailorMobile(username);
		JSONObject json1 = null;
		if (username == null || username.isEmpty() && password == null || password.isEmpty()) {
			res.setStatus(ResponseStatusCode.STATUS_INVALID);
			return createGeneralJSON(ResponseStatusCode.STATUS_REQUIRED, MessageConstants.USERANMEORPASS);
		}
		if (username == null || username.isEmpty()) {
			res.setStatus(ResponseStatusCode.STATUS_INVALID);
			return createGeneralJSON(ResponseStatusCode.STATUS_REQUIRED, MessageConstants.USERANMEREQUIRED);
		}
		if (password == null || password.isEmpty()) {
			res.setStatus(ResponseStatusCode.STATUS_INVALID);
			return createGeneralJSON(ResponseStatusCode.STATUS_REQUIRED, MessageConstants.USERPASSWORD);
		}

		if (user != null) {
			log.info("security");
			UserSecurity usc = userService.getUserSecurityByUserName(user.getUserId());
			log.info("security");
			JSONObject json = accountValiadtion(usc, password, res);
			if (json != null) {
				return json;
			}

		} else {
			res.setStatus(ResponseStatusCode.STATUS_INVALID);
			return createGeneralJSON(ResponseStatusCode.STATUS_NORECORD, MessageConstants.USERNOTFOUND);
		}
		log.info(request.getLocalAddr());
		CustomUserDetailsService.setLoginType(1);
		json1 = accessToken(user.getUserId(), password);
		UserProfile profile = userService.getByUserId(user.getUserId());
		json1.put("profile", profile);
		json1.put(STATUS, createGeneralJSONLogin(ResponseStatusCode.STATUS_OK, MessageConstants.LOGIN_SUCCESS));
		return json1;
	}

	private JSONObject createGeneralJSONLogin(int code, String message) {
		JSONObject json = new JSONObject();
		json.put(STATUS, "" + code);
		json.put("message", message);
		return json;
	}

	private JSONObject createGeneralJSON(int code, String message) {
		JSONObject json = new JSONObject();
		Map<String, String> map = new HashMap<>();
		map.put(STATUS, "" + code);
		map.put("message", message);
		json.put(STATUS, map);
		return json;
	}

	@SuppressWarnings("unchecked")
	private JSONObject createErrorJSON(String error, String errDescription, int errorCode) {
		JSONObject json = new JSONObject();
		json.put(ERROR, error);
		json.put(ERRORDESCRIPTION, errDescription);
		json.put(ERRORCODE, errorCode);
		return json;
	}

	private JSONObject accountValiadtion(UserSecurity usc, String password, HttpServletResponse res) {
		JSONObject json = null;
		User user = usc.getUserId();
		if (!user.isActive()) {
			res.setStatus(ResponseStatusCode.STATUS_INVALID);
			return createGeneralJSON(ResponseStatusCode.STATUS_UNAUTHORIZED, MessageConstants.INACTIVE);
		}
		if (!passwordencoder.matches(password, usc.getPassword())) {

			res.setStatus(ResponseStatusCode.STATUS_INVALID);
			return createGeneralJSON(ResponseStatusCode.STATUS_UNAUTHORIZED, MessageConstants.INCORRECTPASSWORD);
		}

		return json;

	}

	public ResponseMessage<String> otpVerification(UserRegistrationInput input, HttpServletResponse response) {
		ResponseStatus status = null;
		UserSecurity sec = null;
		User user = null;
		String validation = null;
		try {
			validation = userValidation.mobileAndEmailValidation(input);
			if (MessageConstants.SUCCESS.equalsIgnoreCase(validation)) {
				validation = userValidation.otpRequiredValidation(input);
				if (MessageConstants.SUCCESS.equalsIgnoreCase(validation)) {

					user = basicValidation.checkStringnullandempty(input.getEmail())
							? userService.getUserByUserNameOrEmailorMobile(input.getEmail())
							: userService.getUserByUserNameOrEmailorMobile(input.getMobileNo());
					if (basicValidation.checkStringnullandempty(input.getEmail())) {
						sec = userService.getUserSecurityByEmail(input.getEmail());
						if (sec != null) {
							if (sec.getEmailOtp().toString().equals(input.getEmailOtp())) {
								user.setEmailVerification(true);
							} else {
								status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID,
										MessageConstants.INVALIDEMAILOTP);
								return new ResponseMessage<>(status, null);
							}
						}
					}

					if (basicValidation.checkStringnullandempty(input.getMobileNo())) {
						sec = userService.getMobileNoySecurity(input.getMobileNo());
						if (sec != null) {
							if (sec.getMobileNo().equals(input.getMobileNo())) {
								user.setMobileVerification(true);
							} else {
								status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID,
										MessageConstants.INVALIDMOBILEOTP);
								return new ResponseMessage<>(status, null);
							}
						}
					}
					user.setAccountStatus(statusRepo.getById(2L));
					userRepo.save(user);
					response.setStatus(ResponseStatusCode.STATUS_OK);
					status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
				} else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, validation);
					return new ResponseMessage<>(status, null);
				}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, validation);
				return new ResponseMessage<>(status, null);
			}

		} catch (Exception e) {
			log.error(e.toString());
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.toString());
		}
		return new ResponseMessage<>(status, validation);
	}

	public ResponseMessage<String> emailCheck(UserRegistrationInput input, HttpServletResponse res) {
		ResponseStatus status = null;
		boolean userExists = false;
		String validation = "";
		try {
			validation = emailCheckRequired(input);
			if (MessageConstants.SUCCESS.equals(validation)) {
				userExists = emailExists(input);
				if (userExists) {
					status = new ResponseStatus(ResponseStatusCode.STATUS_ALREADY_EXISTS, MessageConstants.USEREXISTS);
				} else {
					res.setStatus(ResponseStatusCode.STATUS_OK);
					status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD, MessageConstants.NORECORD);
				}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, MessageConstants.EMAILREQUIRED);
				return new ResponseMessage<>(status, null);
			}
		} catch (Exception e) {
			log.error(e.toString());
		}

		return new ResponseMessage<>(status, "");
	}

	public ResponseMessage<String> createPassword(UserRegistrationInput input, HttpServletResponse res) {
		ResponseStatus status = null;
		UserSecurity security = null;
		String validation = null;
		try {
			validation = userValidation.validatePassword(input);
			if (validation.equals(MessageConstants.SUCCESS)) {

				security = userService.getUserSecurityByEmail(input.getEmail());
				if (security != null) {
					security.setPassword(passwordencoder.encode(input.getPassword()));
					security.setUpdatedOn(new Date());
					userService.saveOrUpdateUserSecurity(security);
					res.setStatus(ResponseStatusCode.STATUS_OK);
					status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
				} else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD, MessageConstants.NORECORD);
				}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID, validation);
			}

		} catch (Exception e) {
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.toString());
		}
		return new ResponseMessage<>(status, validation);
	}

	private long createOtp() {

		return ((long) (1000 + new Random().nextFloat() * 9000));
	}

	public ResponseMessage<JSONObject> generateOtp(UserRegistrationInput input, HttpServletResponse res) {
		ResponseStatus status = null;
		UserSecurity userSecurity = null;
		String validation = null;
		JSONObject json = null;
		String msg = "";
		Map<String, String> map = new HashMap<>();

		try {

			validation = userValidation.mobileAndEmailValidation(input);

			if (validation.equals(MessageConstants.SUCCESS)) {
				if (basicValidation.checkStringnullandempty(input.getEmail())) {
					userSecurity = userService.getUserSecurityByEmail(input.getEmail());
					if (userSecurity != null && userSecurity.getUserId().getAccountType().getId() == 1L) {
						log.info("enter");

						userSecurity.setEmail(input.getEmail());
						userSecurity.setEmailOtp(createOtp());
						userSecurity.setEmailOtpCreationDate(new Date());
						msg = String.format(SMSConstants.EMAIL, userSecurity.getEmailOtp());
						EmailManager.userVerificationMail(input.getEmail(), msg,
								userSecurity.getUserId().getFirstName());
						map.put("emailOtp status", GENERATEDSUCCESSFULLY);
					}
				}

				if (basicValidation.checkStringnullandempty(input.getMobileNo())) {
					if (Boolean.FALSE.equals(countryRepo.existsByCountryDialCode(input.getCountryCode()))) {
						status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID, MessageConstants.COUNTRYCODE);
						return new ResponseMessage<>(status, json);
					}
					userSecurity = userService.getMobileNoySecurity(input.getMobileNo());

					if (userSecurity != null) {
						userSecurity.setMobileNo(input.getMobileNo());
						userSecurity.setOtp(createOtp());
						userSecurity.setOtpCreationDate(new Date());
						msg = String.format(SMSConstants.MESSAGE, userSecurity.getOtp());
						sms.sendSMS(input.getCountryCode() + input.getMobileNo(), msg);
						map.put("mobileOtp status", GENERATEDSUCCESSFULLY);
					}
				}
				userService.saveOrUpdateUserSecurity(userSecurity);

				json = new JSONObject(map);
				res.setStatus(ResponseStatusCode.STATUS_OK);
				status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);

			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID, validation);
			}

		} catch (Exception e) {
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.toString());
		}
		return new ResponseMessage<>(status, json);
	}

	public ResponseMessage<JSONObject> forgotPassword(UserRegistrationInput input, HttpServletResponse response) {
		ResponseStatus status = null;
		UserSecurity security = null;
		JSONObject json = null;
		Map<String, String> map = new HashMap<>();
		String msg = null;
		try {

			if (input != null) {

				if (basicValidation.checkStringnullandempty(input.getEmail())) {
					security = userService.getUserSecurityByEmail(input.getEmail());
					if (security != null) {
						security.setEmailOtp(createOtp());
						security.setUpdatedOn(new Date());
						msg = String.format(SMSConstants.EMAIL, security.getEmailOtp());
						EmailManager.userVerificationMail(input.getEmail(), msg, security.getUserId().getFirstName());
						map.put("emailOtp", GENERATEDSUCCESSFULLY);
					} else {
						status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD, MessageConstants.EMAILNOTFOUND);
						return new ResponseMessage<>(status, null);
					}
				} else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, MessageConstants.EMAILREQUIRED);
					return new ResponseMessage<>(status, null);
				}
				if (input.getMobileNo() != null) {
					if (Boolean.FALSE.equals(countryRepo.existsByCountryDialCode(input.getCountryCode()))) {
						status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID, MessageConstants.COUNTRYCODE);
						return new ResponseMessage<>(status, json);
					}
					security = userService.getMobileNoySecurity(input.getMobileNo());
					security.setOtp(createOtp());
					security.setUpdatedOn(new Date());
					msg = String.format(SMSConstants.RESETMESSAGE, security.getOtp());
					sms.sendSMS(security.getUserId().getCountryCode() + input.getMobileNo(), msg);
					map.put("mobileOtp", GENERATEDSUCCESSFULLY);
				}
				userService.saveOrUpdateUserSecurity(security);

				json = new JSONObject(map);
				response.setStatus(ResponseStatusCode.STATUS_OK);

				status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);

			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID, MessageConstants.INPUTREQUIRED);
			}

		} catch (Exception e) {
			log.error(e.toString());
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.toString());
		}
		return new ResponseMessage<>(status, json);
	}

	public ResponseMessage<String> removeUser(String emailId, HttpServletResponse response) {
		ResponseStatus status = null;
		String message = null;
		User user = userService.getUserByUserNameOrEmailorMobile(emailId);
		if (Objects.nonNull(user)) {
			UserSecurity security = userSecurityRepo.getByEmail(user.getEmailId());
			userSecurityRepo.delete(security);
			UserProfile profile = profileRepo.findByUserId(user.getUserId());
			profileRepo.delete(profile);
			userService.deleteUser(user);
			message = "Successfully deleted";
			response.setStatus(ResponseStatusCode.STATUS_OK);
			status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
		} else {
			status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD, MessageConstants.NORECORD);
			message = "No record found for the userEmail";
		}
		return new ResponseMessage<>(status, message);
	}

	public ResponseMessage<JSONObject> socialLogin(SocialLoginInput input, HttpServletResponse response) {
		ResponseStatus status = null;
		JSONObject json = null;
		String validation = "";
		User user = null;

		List<String> loginType = Stream.of(LoginType.values()).map(Enum::name).collect(Collectors.toList());
		try {
			validation = socialLoginRequired(input);
			if (MessageConstants.SUCCESS.equals(validation)) {

				user = userService.getUserByUserNameOrEmailorMobile(input.getEmail());
				if (user != null && user.getAccountType() != null && user.getAccountType().getId() == 1) {
					if (input.getType().equals(user.getSocialLoginType())) {
						user.setGoogleId(input.getSocialId());
						CustomUserDetailsService.setLoginType(2);
						json = updateSocialLoginInfo(input, user);
						status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
					} else {
						status = new ResponseStatus(ResponseStatusCode.STATUS_ALREADY_EXISTS,
								MessageConstants.EMAILALREADYEXIST);
						return new ResponseMessage<>(status, null);
					}
				}
				if (loginType.contains(input.getType())) {
					CustomUserDetailsService.setLoginType(2);
					json = updateSocialLoginInfo(input, user);
					response.setStatus(ResponseStatusCode.STATUS_OK);

					status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
				}

				else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID, MessageConstants.INVALIDLOGINTYPE);
				}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, validation);
			}

		} catch (Exception e) {
			log.error("socialLogin ", e);
		}
		return new ResponseMessage<>(status, json);
	}

	private JSONObject updateSocialLoginInfo(SocialLoginInput input, User user) {
		try {
			if (user == null) {
				user = new User();
				user = addSocialAccount(user, input);
				saveUserSecurityInfo(user, input);
			} else {
				user = updateSocialAccount(user, input);
				UserSecurity us = userService.getUserSecurityByUserId(user.getUserId());
				if (us != null) {
					us.setSocial(true);
					us.setSocialPassword(us.getSocialPassword() != null ? us.getSocialPassword()
							: passwordencoder.encode(input.getEmail()));
					us.setEmail(input.getEmail());
					us.setMobileNo(input.getMobileNo());
					userSecurityRepo.save(us);
				}
			}
		} catch (Exception e) {
			log.error("updateSocialLoginInfo ", e);
		}
		return user != null ? accessToken(user.getUserId(), input.getEmail()) : null;
	}

	private User updateSocialAccount(User user, SocialLoginInput input) {
		try {
			switch (input.getType()) {
			case "FACEBOOK":
				user.setFacebookId(!basicValidation.isStringnullandempty(user.getFacebookId()) ? user.getFacebookId()
						: input.getSocialId());
				break;
			case "GOOGLE":
				user.setGoogleId(!basicValidation.isStringnullandempty(user.getGoogleId()) ? user.getGoogleId()
						: input.getSocialId());
				break;
			case "APPLE":
				user.setAppleId(!basicValidation.isStringnullandempty(user.getAppleId()) ? user.getAppleId()
						: input.getSocialId());
				break;
			default:
				break;
			}
			user = userRepo.save(user);

		} catch (Exception e) {
			log.error(" updateSocialAccount ", e);
		}
		return user != null ? user : null;
	}

	private User addSocialAccount(User user, SocialLoginInput input) {
		try {
			user.setEmailId(input.getEmail().toLowerCase());
			user.setFirstName(input.getFirstName());
			user.setLastName(input.getLastName());
			user.setUserName(commonUtils.generateUserName(input.getEmail()));
			user.setActive(true);
			user.setEmailVerification(true);
			user.setMobileNo(basicValidation.checkStringnullandempty(input.getMobileNo()) ? input.getMobileNo() : null);
			user.setEmailVerification(
					basicValidation.checkStringnullandempty(input.getMobileNo()) ? Boolean.TRUE : Boolean.FALSE);
			user.setAccountType(accountTypeRepo.getById(1));
			user.setAccountStatus(statusRepo.getById(2L));
			user.setResident(input.getResident() != null ? input.getResident() : "IN");
			user.setSocialAccount(true);
			user.setSocialLoginType(input.getType());
			switch (input.getType()) {
			case "FACEBOOK":
				user.setFacebookId(input.getSocialId());
				break;
			case "GOOGLE":
				user.setGoogleId(input.getSocialId());
				break;
			case "APPLE":
				user.setAppleId(input.getSocialId());
				break;
			default:
				break;
			}
			user = userRepo.save(user);
			UserProfile profile = new UserProfile();
			profile.setUserId(user);
			profile.setCreatedOn(new Date());
			profile.setUpdatedOn(new Date());
			profileRepo.save(profile);
		} catch (Exception e) {
			log.error("addSocialAccount ", e);
		}
		return user != null ? user : null;
	}

	private String socialLoginRequired(SocialLoginInput input) {
		if (basicValidation.isStringnullandempty(input.getEmail())) {
			return MessageConstants.EMAILREQUIRED;
		}
		if (basicValidation.isStringnullandempty(input.getSocialId())) {
			return MessageConstants.SOCIALIDREQUIRED;
		}
		if (basicValidation.isStringnullandempty(input.getFirstName())) {
			return MessageConstants.FIRSTNAME_REQUIRED;
		}
		if (basicValidation.isStringnullandempty(input.getType())) {
			return MessageConstants.TYPE_REQUIRED;
		}
		return MessageConstants.SUCCESS;
	}

	private String emailCheckRequired(UserRegistrationInput input) {

		if (basicValidation.isStringnullandempty(input.getEmail())) {
			return MessageConstants.EMAILREQUIRED;
		}
		return MessageConstants.SUCCESS;
	}

	public ResponseStatus logout(HttpServletRequest request) {
		log.info("in logout");
		ResponseStatus status = null;
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			String tokenValue = authHeader.replace("bearer", "").trim();
			tokenService.revokeToken(tokenValue);
			tokenService.setAccessTokenValiditySeconds(1);
			tokenService.setRefreshTokenValiditySeconds(1);
			status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.USERLOGOUT);
		} else {
			status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID, MessageConstants.USERCANTNOTLOGOUT);
		}
		return new ResponseStatus(status);
	}

	public ResponseMessage<User> changePassword(UserRegistrationInput input, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseStatus status = null;
		User user = commonUtils.getUser(request);
		try {
			if (user != null) {
				UserSecurity userSecurity = userService.getUserSecurityByUserId(user.getUserId());
				String validation = userValidation.changePasswordValidation(input, userSecurity.getPassword());
				if (validation.equalsIgnoreCase(MessageConstants.SUCCESS)) {
					userSecurity.setPassword(passwordencoder.encode(input.getPassword()));
					userSecurityRepo.save(userSecurity);
					status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.PASSWORDCHANGE);
				} else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, validation);
					response.setStatus(ResponseStatusCode.STATUS_REQUIRED);
					return new ResponseMessage<>(status, null);
				}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD, MessageConstants.NORECORD);
				return new ResponseMessage<>(status, null);
			}
		} catch (Exception e) {
			log.error("", e);
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.toString());
			response.setStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR);
		}

		return new ResponseMessage<>(status, user);

	}

	public ResponseMessage<JSONObject> adminForgotPassword(UserRegistrationInput input, HttpServletResponse response) {
		ResponseStatus status = null;
		UserSecurity security = null;
		JSONObject json = null;
		Map<String, String> map = new HashMap<>();
		try {
			if (basicValidation.checkStringnullandempty(input.getEmail())) {
				security = userService.getUserSecurityByEmail(input.getEmail());
				if (security != null) {
					security.setUpdatedOn(new Date());
					security.setVerifyLink(commonUtils.randomReferenceString(6) + "_"
							+ security.getUserId().getFirstName().substring(0, 3));
					security = userService.saveOrUpdateUserSecurity(security);
					EmailManager.forgotPassword(input.getEmail(), "", security.getUserId().getFirstName(), "",
							CommonProperties.getForgetPassword() + "?verifyLink=" + security.getVerifyLink());
					map.put(STATUS, GENERATEDSUCCESSFULLY);
				} else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD, MessageConstants.EMAILNOTFOUND);
					return new ResponseMessage<>(status, null);
				}
				response.setStatus(ResponseStatusCode.STATUS_OK);
				status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
				json = new JSONObject(map);
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID, MessageConstants.EMAILREQUIRED);
				return new ResponseMessage<>(status, null);
			}

		} catch (Exception e) {
			log.error("forgotPassword ", e);
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.getMessage());
		}

		return new ResponseMessage<>(status, json);
	}

	public ResponseMessage<JSONObject> resetPassword(UserRegistrationInput input) {
		JSONObject json = null;
		ResponseStatus status = null;
		UserSecurity security = null;
		String validation = "";
		try {
			validation = resetPasswordRequired(input);
			if (MessageConstants.SUCCESS.equals(validation)) {
				security = userService.getUserSecurityByVerifyLink(input.getVerifyLink());
				if (security != null) {
					security.setPassword(passwordencoder.encode(input.getPassword()));
					userService.saveOrUpdateUserSecurity(security);
					status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
					return new ResponseMessage<>(status, json);
				} else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_NOTEXIST, MessageConstants.VERIFYLINK);
					return new ResponseMessage<>(status, json);
				}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, validation);
			}
		} catch (Exception e) {
			log.error("resetPassword ", e);
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.getMessage());
		}

		return new ResponseMessage<>(status, json);

	}

	private String resetPasswordRequired(UserRegistrationInput input) {
		if (basicValidation.isStringnullandempty(input.getVerifyLink())) {
			return MessageConstants.VERIFYLINKREQUIRED;
		}
		if (basicValidation.isStringnullandempty(input.getPassword())) {
			return MessageConstants.USERPASSWORD;
		}
		if (basicValidation.isStringnullandempty(input.getConfirmPassword())) {
			return MessageConstants.CONFIRMPASSWORD;
		}
		if (!input.getConfirmPassword().equals(input.getPassword())) {
			return MessageConstants.MISSMATCHPASS;
		}
		return MessageConstants.SUCCESS;

	}

	public ResponseMessage<DeviceToken> updateDeviceToken(TokenInput input, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseStatus status = null;
		DeviceToken devicetoken = null;
		User user = commonUtils.getUser(request);
		String validation = "";
		try {
			validation = userValidation.updateDeviceTokenRequired(input);
			if (MessageConstants.SUCCESS.equals(validation)) {
				devicetoken = userService.getDeviceTokenByUserId(user.getUserId());
				if (devicetoken != null) {
					if (input.getToken() != null && !input.getToken().isEmpty()) {
						devicetoken.setToken(input.getToken());
						devicetoken.setType(input.getType());
						devicetoken.setUpdatedOn(new Date());
					}
					if (input.getLocationToken() != null && !input.getLocationToken().isEmpty()) {
						devicetoken.setLocationToken(input.getLocationToken());
						devicetoken.setType(input.getType());
						devicetoken.setUpdatedOn(new Date());
					}
				} else {
					devicetoken = new DeviceToken();

					devicetoken.setUserId(user.getUserId());
					devicetoken.setToken(input.getToken());
					devicetoken.setLocationToken(input.getLocationToken());
					devicetoken.setType(input.getType());
					devicetoken.setCreatedOn(new Date());
				}
				deviceToken.save(devicetoken);
				response.setStatus(ResponseStatusCode.STATUS_OK);
				status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_INVALID, validation);
			}

		} catch (Exception e) {
			log.error("updateDeviceToken :", e);
		}
		return new ResponseMessage<>(status, devicetoken);
	}

	public ResponseStatus deleteUser(HttpServletRequest request, HttpServletResponse response) {
		ResponseStatus status = null;
		User user = commonUtils.getUser(request);

		try {
			if (basicValidation.checkStringnullandempty(user.getUserId())) {
				user = userService.getUserById(user.getUserId());
				if (user != null) {
					user.setActive(false);
					user.setDeleted(true);
					user.setEmailId(user.getEmailId() + "_deleted");
					user.setMobileNo(user.getMobileNo() + "_deleted");
					userService.saveOrUpdateUser(user);
					response.setStatus(ResponseStatusCode.STATUS_OK);
					status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.DELETED);
				} else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD, MessageConstants.NORECORD);
				}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, MessageConstants.USERID);
			}
		} catch (Exception e) {
			log.error("deleteUser", e);
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.getMessage());
		}
		return status;
	}

}
