package com.theHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.theHub.exception.AppException;

import jakarta.servlet.http.HttpSession;

@Service
public class UserSessionService {
	
	@Autowired
	private HttpSession session;

    public void setUserId(Long userId) {
        session.setAttribute("userId", userId);
    }

    public Long getUserId() {
    	Long loggedUserId = (Long) session.getAttribute("userId");
    	if (loggedUserId == null)
    		throw new AppException("No se ha iniciado sesión", HttpStatus.UNAUTHORIZED);
        return loggedUserId;
    }

    public void clearUserId() {
        session.removeAttribute("userId");
    }

}