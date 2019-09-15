package com.assignment.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.assignment.entity.User;
import com.assignment.repository.UserRepository;
import com.assignment.util.AESCipher;

/**
 * Test UserService functionality
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@MockBean
	private  UserRepository userRepository;

	@Autowired
	private  UserService userService;

	@Before
	public void setUp() throws Exception {
		User user1 = new User("anilgupta@gmail.com","anil","gupta","anil123");
		User user2 = new User("manojgupta@gmail.com","manoj","gupta","gupta123");
		Collection<User> userCollections = new ArrayList<>();
		userCollections.add(user1);
		userCollections.add(user2);
		Mockito.when(userRepository.findByEmail(user1.getEmail()))
		.thenReturn(user1);
		Mockito.when(userRepository.findAll())
		.thenReturn(userCollections);
	}

	@Test
	public void returnUser_WhenEmailIsValid() {
		String email = "anilgupta@gmail.com";
		User user = userService.getUserByEmail(email);
		assertSame(email,user.getEmail());
	}

	@Test
	public void returnUserCollection_WhenRecordsIsPresent() {
		Collection<User> userCollections = userService.getAllUser();
		assertEquals(userCollections.size(), 2);
	}

	@Test(expected = Exception.class)
	public void deleteUser_WhenEmailIsValid() throws Exception {
		User user = new User("priyanka@gmail.com","Priyanka","jaiswal","jas");
		String email = "priyanka@gmail.com";
		Mockito.doAnswer((u) -> {
			System.out.println("deleteuser test --"+u.getArgument(0));
			assertTrue("priyanka@gmail.com".equals(((User)u.getArgument(0)).getEmail()));
			return null;
		}).when(userRepository).deleteById(email);

		Mockito.when(userRepository.findByEmail(email)).thenAnswer(new Answer() {
			private int count = 0;
			public Object answer(InvocationOnMock invocation) {
				if (count++ == 1)
					return user;

				return null;
			}
		});

		Mockito.when(userRepository.findByEmail(email)).thenReturn(null);
		userService.deleteUser(email);	
		userService.getUserByEmail(email);
	}

	@Test
	public void createNewUser() throws Exception {
		User user = new User("priyanka@gmail.com","Priyanka","jaiswal","jas");
		Mockito.doAnswer((u) -> {
			assertTrue("priyanka@gmail.com".equals(((User)u.getArgument(0)).getEmail()));
			return null;
		}).when(userRepository).save(user);

		Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenAnswer(new Answer(){
			private int count = 1;
			public Object answer(InvocationOnMock invocation) {
				if (count == 1) {
					count++;
					return null;
				}
				return user;
			}
		});
		userService.createUser(user);	
		assertEquals("Priyanka", userService.getUserByEmail("priyanka@gmail.com").getFirstName());
	}

	@Test
	public void updateExistingUser() throws Exception {
		User user = new User("sunil@gmail.com","sunil","patil","patil15");
		String oldEmail = "sunil@gmail.com";
		Mockito.doAnswer((u) -> {
			assertTrue("sunil@gmail.com".equals(((User)u.getArgument(0)).getEmail()));
			return null;
		}).when(userRepository).save(user);

		Mockito.when(userRepository.findByEmail(oldEmail)).thenAnswer(new Answer() {
			private int count = 0;
			public Object answer(InvocationOnMock invocation) {
				if (count++ == 1)
					return user;

				user.setLastName("Patel");
				return user;
			}
		});
		userService.updateUser(user, oldEmail);	
		assertEquals("Patel", userService.getUserByEmail(oldEmail).getLastName());
	}

	@Test
	public void changePassword_WhenBothOldAndNewPasswordExist_And_BothAreValid() throws Exception {
		String oldPassword = "patil15";
		String newPassword = "Patel23";
		String email = "sunil@gmail.com";
		User user = new User(email,"sunil","patil","patil15");

		Mockito.when(userRepository.findByEmail(email)).thenAnswer(new Answer() {
			private int count = 1;
			public Object answer(InvocationOnMock invocation) throws Exception {
				if (count == 1) {
					user.setPassword(AESCipher.encrypt(user.getPassword()));
					count++;
					return user;
				}
				user.setPassword(AESCipher.encrypt(newPassword));
				return user;
			}
		});
		userService.changePassword(email, oldPassword, newPassword);
		User usr = userService.getUserByEmail(email);
		assertEquals(newPassword,AESCipher.decrypt(usr.getPassword()));
	}

	@Test
	public void loginSuccessfull_WhenEmailAndPasswordMatch() throws Exception{

		String email = "sunil@gmail.com";
		String pwd = "patil15";
		User user = new User(email,"sunil","patil",AESCipher.encrypt(pwd));
		Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

		String msg = userService.login(email, pwd);
		assertEquals("Welcome "+user.getFirstName()+" "+user.getLastName()+"!", msg);
	}
}
