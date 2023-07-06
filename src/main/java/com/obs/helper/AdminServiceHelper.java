package com.obs.helper;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.obs.customdomain.UserRegistrationInput;
import com.obs.domain.User;
import com.obs.messages.ExportConstants;
import com.obs.messages.MessageConstants;
import com.obs.messages.ResponseListPagination;
import com.obs.messages.ResponseMessage;
import com.obs.messages.ResponseStatus;
import com.obs.messages.ResponseStatusCode;
import com.obs.repository.UserRepo;
import com.obs.service.IUserService;
import com.obs.util.CommonUtils;
import com.obs.util.FileDetailsBean;
import com.obs.validation.BasicValidation;
import com.obs.validation.UserValidation;

@Service

public class AdminServiceHelper {

	private static final Logger logger = LogManager.getLogger(AdminServiceHelper.class);

	@Autowired
	Environment env;

	@Autowired
	IUserService userService;

	@Autowired
	CommonUtils commonUtils;

	@Autowired
	public UserValidation userValidation;

	@Autowired
	BasicValidation basicValidation;

	@Autowired
	UserRepo userRepo;

	public ResponseMessage<User> editUser(UserRegistrationInput input, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseStatus status = null;
		User user = null;
		String validation = "";
		try {
			validation = userValidation.editUserRequired(input);
			if (MessageConstants.SUCCESS.equals(validation)) {
				user = userService.getUserById(input.getUserId());
				if (user != null) {
					user.setFirstName(input.getFirstName());
					user.setLastName(input.getLastName());
					user.setEmailId(input.getEmail());
					user.setUserName(input.getUserName());
					user.setDob(input.getDob());
					user.setMobileNo(input.getMobileNo());
					user.setResident(input.getResident());
					user.setGender(input.getGender());
					user.setProfileImage(input.getProfileImage());
					userService.saveOrUpdateUser(user);
					response.setStatus(ResponseStatusCode.STATUS_OK);
					status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
				} else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD, MessageConstants.NORECORD);
				}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, validation);
			}

		} catch (Exception e) {
			logger.error("editUser", e);
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.getMessage());
		}

		return new ResponseMessage<>(status, user);
	}

	public ResponseStatus deleteManageUser(String userId, HttpServletResponse response) {
		ResponseStatus status = null;
		User user = null;

		try {
			if (basicValidation.checkStringnullandempty(userId)) {
				user = userService.getUserById(userId);
				if (user != null) {
					user.setActive(false);
					user.setDeleted(true);
					userService.saveOrUpdateUser(user);
					response.setStatus(ResponseStatusCode.STATUS_OK);
					status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
				} else {
					status = new ResponseStatus(ResponseStatusCode.STATUS_NORECORD, MessageConstants.NORECORD);
				}
			} else {
				status = new ResponseStatus(ResponseStatusCode.STATUS_REQUIRED, MessageConstants.USERID);
			}
		} catch (Exception e) {

			logger.error("deleteUser", e);
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.getMessage());
		}
		return status;
	}

	public ResponseListPagination<User> viewUser(String search, int pageNumber, int pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseStatus status = null;
		List<User> user = null;

		try {

			user = userService.getUserListWithSearch(search, pageNumber, pageSize);

			response.setStatus(ResponseStatusCode.STATUS_OK);
			status = new ResponseStatus(ResponseStatusCode.STATUS_OK, MessageConstants.SUCCESS);
		} catch (Exception e) {
			logger.error("viewUser", e);
			status = new ResponseStatus(ResponseStatusCode.STATUS_INTERNAL_ERROR, e.getMessage());

		}

		return new ResponseListPagination<>(status, user);
	}

	public String exportViewUser(String search, HttpServletRequest request, HttpServletResponse response) {
		List<User> user = null;

		try {
			user = (List<User>) userRepo.findAll();
			FileDetailsBean fileDetailsBean = getViewUserFileDetails(user.get(0).getFirstName(), search, request);
			String fileData = new String(fileDetailsBean.getFileArray(), StandardCharsets.UTF_8);
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileDetailsBean.getFileName());

			OutputStream outputStream = response.getOutputStream();
			outputStream.write(fileData.getBytes());
			outputStream.flush();
			outputStream.close();
			return fileData;

		} catch (Exception e) {
			logger.error("exportViewUser ", e);
		}

		return null;
	}

	private FileDetailsBean getViewUserFileDetails(String firstName, String search, HttpServletRequest request) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		StringBuilder dataBuilder = new StringBuilder();

		String fileName = firstName + "_viewuser_" + dateFormat.format(new Date()) + ExportConstants.CSV_EXTENSION;
		dataBuilder.append(ExportConstants.VIEW_MANAGE_USER_HEADER);
		dataBuilder.append(ExportConstants.LINE_SEPARATOR);

		List<User> user = userService.getUserListWithSearch(search, 0, 0);
		if (user != null) {
			user.forEach(viewUserDetails -> {

				dataBuilder.append((viewUserDetails.getUserId() != null ? viewUserDetails.getUserId() : "---")
						+ ExportConstants.DEFAULT_SEPARATOR);
				dataBuilder.append((viewUserDetails.getFirstName() != null ? viewUserDetails.getFirstName() : "---")
						+ ExportConstants.DEFAULT_SEPARATOR);
				dataBuilder.append((viewUserDetails.getLastName() != null ? viewUserDetails.getLastName() : "---")
						+ ExportConstants.DEFAULT_SEPARATOR);
				dataBuilder.append((viewUserDetails.getUserName() != null ? viewUserDetails.getUserName() : "---")
						+ ExportConstants.DEFAULT_SEPARATOR);
				dataBuilder.append((viewUserDetails.getGender() != null ? viewUserDetails.getGender() : "---")
						+ ExportConstants.DEFAULT_SEPARATOR);
				dataBuilder.append((viewUserDetails.getMobileNo() != null ? viewUserDetails.getMobileNo() : "---")
						+ ExportConstants.DEFAULT_SEPARATOR);
				dataBuilder.append((viewUserDetails.getResident() != null ? viewUserDetails.getResident() : "---")
						+ ExportConstants.DEFAULT_SEPARATOR);
				dataBuilder.append((viewUserDetails.getEmailId() != null ? viewUserDetails.getEmailId() : "---")
						+ ExportConstants.DEFAULT_SEPARATOR);
				dataBuilder.append(viewUserDetails.getDob() != null ? viewUserDetails.getDob()
						: "------" + ExportConstants.DEFAULT_SEPARATOR);
				dataBuilder.append(ExportConstants.LINE_SEPARATOR);

			});
		}
		FileDetailsBean fileDetailsBean = new FileDetailsBean();
		fileDetailsBean.setFileName(fileName);
		fileDetailsBean.setFileExtension(ExportConstants.CSV_EXTENSION);
		fileDetailsBean.setFileArray(dataBuilder.toString().getBytes());
		return fileDetailsBean;

	}

}
