package com.assignment.service;

import java.util.List;

import com.assignment.entity.User;
import com.assignment.exception.InvalidUserException;
import com.assignment.exception.PasswordEncrytionException;
import com.assignment.exception.UserExistException;

public interface UserService {

	public User getUserByEmail(String email);

	public List<User> getAllUser();

	public void deleteUser(String email) throws InvalidUserException;;

	public User createUser(User user) throws UserExistException, PasswordEncrytionException;

	public User updateUser(User user, String oldEmail) throws InvalidUserException;

	public void changePassword(String email, String oldPassword, String newPassword) throws Exception;

	public String login(String email, String password) throws Exception;
}
