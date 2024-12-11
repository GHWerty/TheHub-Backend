package com.theHub.controller.util;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.theHub.exception.AppException;
import com.theHub.exception.AppMultipleErrosException;
import com.theHub.service.UserSessionService;

@Component
public class ControllerResponseHandler {
	
	@Autowired
	private UserSessionService userSessionService;

	public ResponseEntity<?> handleResponse(ControllerRequestHandler requestHandler) {

		ResponseEntity<?> response;
		HashMap<String, Object> responseContent = new HashMap<String, Object>();

		HttpStatus httpStatus = null;

		try {
			Long loggedUserId = userSessionService.getUserId();
			httpStatus = requestHandler.process(responseContent, loggedUserId);
		} catch (AppException e) {
			responseContent.put("message", e.getMessage());
			httpStatus = e.getStatus();
		} catch (AppMultipleErrosException e) {
			e.getErrors().forEach((key, value) -> responseContent.put(key, value));
			httpStatus = e.getStatus();
		}catch (Exception e) {
			responseContent.put("message", "Internal server error: ".concat(e.getMessage()));
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		response = new ResponseEntity<HashMap<String, Object>>(responseContent, httpStatus);
		return response;
	}

}