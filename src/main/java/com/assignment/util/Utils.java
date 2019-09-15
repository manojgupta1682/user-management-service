package com.assignment.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.assignment.entity.User;
import com.assignment.vo.UserVO;

public class Utils {

	public static List<UserVO> getUserVOList(Collection<User> userCollection){
		return userCollection.stream()
				.map(u -> new UserVO(u.getEmail(), u.getFirstName(), u.getLastName()))
				.collect(Collectors.toList());
	}

	public static UserVO getUserVO(User usr) {
		return new UserVO(usr.getEmail(),usr.getFirstName(),usr.getLastName());
	}

}
