package com.theHub.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.theHub.exception.AppException;
import com.theHub.exception.AppMultipleErrosException;
import com.theHub.model.dto.userDto.CredentialsDto;
import com.theHub.model.dto.userDto.RegisterCredentialsDto;
import com.theHub.model.persist.dao.UserDao;
import com.theHub.service.UserSessionService;

@RestController
public class AuthorizationRestController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserSessionService userSessionService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody CredentialsDto credentials) {

		ResponseEntity<?> response;
		HashMap<String, Object> responseContent = new HashMap<String, Object>();
		HttpStatus httpStatus = null;

		try {
			Long loggedUserId = userDao.getIdByCredentials(credentials.email(), credentials.password());
			userSessionService.setUserId(loggedUserId);
			responseContent.put("result", loggedUserId);
			httpStatus = HttpStatus.OK;
		} catch (AppException e) {
			responseContent.put("message", e.getMessage());
			httpStatus = e.getStatus();
		} catch (Exception e) {
			responseContent.put("message", "Internal server error: ".concat(e.getMessage()));
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<HashMap<String, Object>>(responseContent, httpStatus);
		return response;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterCredentialsDto credentials) {

		ResponseEntity<?> response;
		HashMap<String, Object> responseContent = new HashMap<String, Object>();
		HttpStatus httpStatus = null;

		try {
			Long loggedUserId = userDao.createUser(credentials.nick(), credentials.email() ,credentials.password());
			userSessionService.setUserId(loggedUserId);
			responseContent.put("result", loggedUserId);
			httpStatus = HttpStatus.OK;
		} catch (AppMultipleErrosException e) {
			e.getErrors().forEach((key, value) -> responseContent.put(key, value));
			httpStatus = e.getStatus();
		} catch (Exception e) {
			responseContent.put("message", "Internal server error: ".concat(e.getMessage()));
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<HashMap<String, Object>>(responseContent, httpStatus);
		return response;
	}
	
	
	@PostMapping("/refreshSession")
	public ResponseEntity<?> refreshSession(@RequestBody Long loggedUserId) {

		ResponseEntity<?> response;
		HashMap<String, Object> responseContent = new HashMap<String, Object>();
		HttpStatus httpStatus = null;

		try {
			userSessionService.setUserId(loggedUserId);
			responseContent.put("result", loggedUserId);
			httpStatus = HttpStatus.OK;
		} catch (AppException e) {
			responseContent.put("message", e.getMessage());
			httpStatus = e.getStatus();
		} catch (Exception e) {
			responseContent.put("message", "Internal server error: ".concat(e.getMessage()));
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<HashMap<String, Object>>(responseContent, httpStatus);
		return response;
	}
	
}