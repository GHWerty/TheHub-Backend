package com.theHub.exception;

import java.util.HashMap;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AppMultipleErrosException extends RuntimeException {

	private HttpStatus status;
	private HashMap<String, String> errors;

	public AppMultipleErrosException(HashMap<String, String> errors, HttpStatus status) {
		this.errors = errors;
		this.status = status;
	}
}