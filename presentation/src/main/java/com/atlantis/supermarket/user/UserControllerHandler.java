package com.atlantis.supermarket.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.atlantis.supermarket.core.user.exceptions.UserExistsException;

@ControllerAdvice
public class UserControllerHandler {

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<String> handleMyException(UserExistsException mex) {
	return ResponseEntity.status(409).body("Usuario existente");
    }
}
