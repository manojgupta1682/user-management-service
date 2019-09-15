package com.assignment.vo;

public class UserVO { //Created to return User value so that password should not be expose 

	private final String email;
	private final String firstName;
	private final String lastName;

	public UserVO(String email, String firstName, String lastName) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}



}
