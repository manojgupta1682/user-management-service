package com.assignment.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.entity.User;
import com.assignment.service.UserService;
import com.assignment.util.ParameterValidationUtil;
import com.assignment.util.Utils;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@ResponseBody
	@RequestMapping("")
	public <T> ResponseEntity<T> getAllUsers(){
		List<User> userList = userService.getAllUser();
		if(userList == null || userList.size() == 0) {
			return (ResponseEntity<T>) new ResponseEntity<>("No User Available",HttpStatus.OK);
		}
		return (ResponseEntity<T>) new ResponseEntity<>(Utils.getUserVOList(userList),HttpStatus.OK);
	}  

	@ResponseBody
	@RequestMapping("/getByEmail/{email}")
	public <T> ResponseEntity<T> getUser(@PathVariable("email") String email){
		if(!ParameterValidationUtil.validateEmail(email)) {
			return (ResponseEntity<T>) new ResponseEntity<>("Invalid Email - "+email,HttpStatus.BAD_REQUEST);
		}
		User usr = userService.getUserByEmail(email);
		if(usr == null) {
			return (ResponseEntity<T>) new ResponseEntity<>("User not Available",HttpStatus.OK);
		}
		return (ResponseEntity<T>) new ResponseEntity<>(Utils.getUserVO(usr),HttpStatus.OK);
	} 

	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public <T> ResponseEntity<T> createUser(@RequestBody User user) throws Exception{
		List<String> validationErrors = ParameterValidationUtil.validateUser(user);
		if(validationErrors != null) {
			return (ResponseEntity<T>) new ResponseEntity<>(validationErrors,HttpStatus.BAD_REQUEST);
		}
		userService.createUser(user);
		return (ResponseEntity<T>) new ResponseEntity<>("User Created Successfully",HttpStatus.CREATED);
	}

	@ResponseBody
	@RequestMapping(value = "/update/{email}", method = RequestMethod.PUT)
	public <T> ResponseEntity<T> updateUser(@RequestBody User user, @PathVariable("email") String email) throws Exception{

		if(!ParameterValidationUtil.validateEmail(email)) {
			return (ResponseEntity<T>) new ResponseEntity<>("Invalid Email - "+email,HttpStatus.BAD_REQUEST);
		}
		List<String> validationErrors = ParameterValidationUtil.validateUserForUpdate(user);
		if(validationErrors != null) {
			return (ResponseEntity<T>) new ResponseEntity<>(validationErrors,HttpStatus.BAD_REQUEST);
		}
		userService.updateUser(user, email);
		return (ResponseEntity<T>) new ResponseEntity<>("User updated successfully",HttpStatus.OK);

	}

	@ResponseBody
	@RequestMapping(value = "/delete/{emailId}", method = RequestMethod.DELETE)
	public <T> ResponseEntity<T> deleteUser(@PathVariable("emailId") String email) throws Exception{
		if(!ParameterValidationUtil.validateEmail(email)) {
			return (ResponseEntity<T>) new ResponseEntity<>("Invalid Email - "+email,HttpStatus.BAD_REQUEST);
		}
		userService.deleteUser(email);
		return (ResponseEntity<T>) new ResponseEntity<>("User deleted successfully",HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/changePassword/{email}", method = RequestMethod.POST)
	public <T> ResponseEntity<T> changePassword(@PathVariable("email") String email,
			@RequestParam("oldPassword")  String oldPassword,
			@RequestParam("newPassword") String newPassword) throws Exception{

		List<String> validationErrors = new ArrayList<>();
		if(email == null) {
			validationErrors.add("email cannot be null");
		}
		if(oldPassword == null){
			validationErrors.add("old password cannot be null");
		}
		if(newPassword == null) {
			validationErrors.add("new password cannot be null");
		}
		if(validationErrors.size() > 0) {
			return (ResponseEntity<T>) new ResponseEntity<>(validationErrors,HttpStatus.BAD_REQUEST);
		}
		if(!ParameterValidationUtil.validateEmail(email)) {
			return (ResponseEntity<T>) new ResponseEntity<>("Invalid Email - "+email,HttpStatus.BAD_REQUEST);
		}
		userService.changePassword(email, oldPassword, newPassword);
		return (ResponseEntity<T>) new ResponseEntity<>("Password changed",HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public <T> ResponseEntity<T> login(@RequestParam("email") String email,
			@RequestParam("password")  String password) throws Exception{

		List<String> validationErrors = new ArrayList<>();
		if(email == null) {
			validationErrors.add("email cannot be null");
		}
		if(password == null){
			validationErrors.add("password cannot be null");
		}
		if(validationErrors.size() > 0) {
			return (ResponseEntity<T>) new ResponseEntity<>(validationErrors,HttpStatus.BAD_REQUEST);
		}
		if(!ParameterValidationUtil.validateEmail(email)) {
			return (ResponseEntity<T>) new ResponseEntity<>("Invalid Email - "+email,HttpStatus.BAD_REQUEST);
		}
		String loginMsg = userService.login(email, password);
		return (ResponseEntity<T>) new ResponseEntity<>(loginMsg,HttpStatus.OK);
	}

}