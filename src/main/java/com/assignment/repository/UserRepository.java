package com.assignment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.assignment.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String>{
	public User findByEmail(String email);
}
