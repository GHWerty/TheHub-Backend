package com.theHub.controller.util;

import java.util.HashMap;

import org.springframework.http.HttpStatus;

@FunctionalInterface
public interface ControllerRequestHandler {

	HttpStatus process(HashMap<String, Object> responseContent, Long loggedUserId);
	
}