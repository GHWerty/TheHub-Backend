package com.theHub.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

	private HttpStatus status;
	private String message;

	public AppException(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}
	
}