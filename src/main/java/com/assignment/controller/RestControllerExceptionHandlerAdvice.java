package com.assignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.assignment.exception.InvalidUserException;
import com.assignment.exception.PasswordEncrytionException;
import com.assignment.exception.PasswordMisMatchException;
import com.assignment.exception.UserExistException;

/**
 * Handle all the exception generated in UserController and generate response
 */
@ControllerAdvice(assignableTypes = UserController.class) 
@RequestMapping(produces = "application/json") 
public class RestControllerExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(RestControllerExceptionHandlerAdvice.class);

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<String> handlerDefaultException(Exception ex, WebRequest request) {
		logger.error("Error - ", ex);
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InvalidUserException.class)
	public final ResponseEntity<String> handlerInvalidUserException(Exception ex, WebRequest request) {
		logger.error("Error - ", ex);
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserExistException.class)
	public final ResponseEntity<String> handleUserExistExceptions(Exception ex, WebRequest request) {
		logger.error("Error - ", ex);
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.IM_USED);
	}

	@ExceptionHandler(PasswordEncrytionException.class)
	public final ResponseEntity<String> handlePasswordEncrytionException(Exception ex, WebRequest request) {
		logger.error("Error - ", ex);
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(PasswordMisMatchException.class)
	public final ResponseEntity<String> handlerPasswordMisMatchException(Exception ex, WebRequest request) {
		logger.error("Error - ", ex);
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}
}
