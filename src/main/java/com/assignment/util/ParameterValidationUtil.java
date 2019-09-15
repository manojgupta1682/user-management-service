package com.assignment.util;

import java.util.ArrayList;
import java.util.List;

import com.assignment.entity.User;

public class ParameterValidationUtil {

	public static boolean validateEmail(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

	public static List<String> validateUser(User user) {
		List<String> errorList = new ArrayList<String>();
		if(user == null) {
			errorList.add("Please provide email, first name, last name and password");
			return errorList;
		}
		if(user.getEmail() == null){
			errorList.add("email cannot be blank");
		}
		else if(!validateEmail(user.getEmail())) {
			errorList.add("invalid email - "+user.getEmail());
		}
		if(user.getFirstName() == null){
			errorList.add("first name cannot be blank");
		}
		if(user.getLastName() == null){
			errorList.add("last name cannot be blank");
		}
		if(user.getPassword() == null){
			errorList.add("password cannot be blank");
		}
		return errorList.size() > 0 ? errorList : null;
	}

	public static List<String> validateUserForUpdate(User user) {
		List<String> errorList = new ArrayList<String>();
		if(user == null) {
			errorList.add("Please provide atleast email or first name or last name for update ");
			return errorList;
		}
		if(user.getEmail() != null && !validateEmail(user.getEmail())){
			errorList.add("invalid email - "+user.getEmail());
		}
		if(user.getPassword() != null) {
			errorList.add("password cannot be updated, to update password call changePassword API");
		}
		return errorList.size() > 0 ? errorList : null;
	}

}
