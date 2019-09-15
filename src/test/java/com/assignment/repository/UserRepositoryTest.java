package com.assignment.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.assignment.entity.User;

/**
 * Test UserRepository functionality
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() throws Exception {
		userRepository.save(new User("mohansingh@gmail.com","Mohan","Singh","ram123"));
		userRepository.save(new User("manojgupta@gmail.com","manoj","gupta","gupta123"));
	}

	@After
	public void tearDown() throws Exception {
		userRepository.deleteById("manojgupta@gmail.com");
		userRepository.deleteById("mohansingh@gmail.com");
	}

	@Test
	public void createNewUser() throws Exception {
		User user = new User("ramsingh@gmail.com","Ram","Singh","ram123");
		userRepository.save(user);
		User u = userRepository.findByEmail("ramsingh@gmail.com");
		userRepository.deleteById("ramsingh@gmail.com");
		assertEquals(u,user);
	}

	@Test
	public void saveExistingUser(){
		User user = userRepository.findByEmail("mohansingh@gmail.com");
		user.setLastName("Gupta");
		userRepository.save(user);
		user = userRepository.findByEmail("mohansingh@gmail.com");
		assertEquals("Gupta", user.getLastName());
	}

	@Test
	public void getUser() {
		User user = userRepository.findByEmail("manojgupta@gmail.com");
		assertNotNull(user);
	}

	@Test
	public void getUserList() {
		List<User> users = (List<User>) userRepository.findAll();
		assertEquals(users.size(), 2);

	}

	@Test
	public void deleteUser() throws Exception {
		userRepository.save(new User("anilgupta@gmail.com","anil","gupta","gupta123"));
		userRepository.deleteById("anilgupta@gmail.com");
		User usr = userRepository.findByEmail("anilgupta@gmail.com");
		assertNull(usr);
	}
}
