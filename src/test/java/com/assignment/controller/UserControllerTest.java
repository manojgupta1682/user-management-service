package com.assignment.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.assignment.entity.User;
import com.assignment.service.UserService;

/**
 * Test UserController functionality
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private User user1 = new User("anilgupta@gmail.com","anil","gupta","anil123");
	private User user2 = new User("manojgupta@gmail.com","manoj","gupta","gupta123");

	@Test
	public void getUserByEmail() throws Exception {
		Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user1);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/user/getByEmail/anilgupta@gmail.com").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "{\"email\":\"anilgupta@gmail.com\",\"firstName\":\"anil\",\"lastName\":\"gupta\"}";
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getAllUser() throws Exception {
		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		Mockito.when(userService.getAllUser()).thenReturn(userList);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/user").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "[{\"email\":\"anilgupta@gmail.com\",\"firstName\":\"anil\",\"lastName\":\"gupta\"},{\"email\":\"manojgupta@gmail.com\",\"firstName\":\"manoj\",\"lastName\":\"gupta\"}]";

		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void deleteUser_whenEmailIsValid_And_UserExists() throws Exception {

		Mockito.doAnswer((u) -> {
			assertTrue("anilgupta@gmail.com".equals(u.getArgument(0))); 
			return null;
		}).when(userService).deleteUser(Mockito.anyString());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
				"/user/delete/anilgupta@gmail.com").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse().getContentAsString());
		String expected = "User deleted successfully";
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertEquals(expected, result.getResponse().getContentAsString());
	}

	@Test
	public void createNewUser_WhenAllParameterIsValid() throws Exception {
		Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(user1);

		String jsonString = "{\r\n" + 
				"    \"email\": \"anilgupta@gmail.com\",\r\n" + 
				"    \"firstName\": \"anil\",\r\n" + 
				"    \"lastName\" : \"gupta\",\r\n" + 
				"    \"password\": \"anil123\"\r\n" + 
				"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
				"/user/create").accept(MediaType.APPLICATION_JSON)
				.content(jsonString).contentType(MediaType.APPLICATION_JSON);


		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "User Created Successfully";
		System.out.println(result.getResponse().getContentAsString());
		assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
		assertEquals(expected, result.getResponse().getContentAsString());
	}

	@Test
	public void updateAllAttributeOfExistingUserExceptPassword() throws Exception {
		User user = new User("anilgupta@gmail.com","anil","pandit","anil123");
		Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(user);

		String jsonString = "{\r\n" + 
				"    \"email\": \"anilgupta@gmail.com\",\r\n" + 
				"    \"firstName\": \"anil\",\r\n" + 
				"    \"lastName\" : \"pandit\"\r\n" + 
				"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
				"/user/update/anilgupta@gmail.com").accept(MediaType.APPLICATION_JSON)
				.content(jsonString).contentType(MediaType.APPLICATION_JSON);


		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "User updated successfully";
		System.out.println(result.getResponse().getContentAsString());
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertEquals(expected, result.getResponse().getContentAsString());
	}

	@Test
	public void changePassword_WhenOldPasswordIsNotSame() throws Exception {

		Mockito.doAnswer((u) -> {
			assertTrue("anilgupta1682@gmail.com".equals(u.getArgument(0))); 
			assertTrue(!u.getArgument(1).equals(u.getArgument(2))); 
			return null;
		}).when(userService).changePassword(Mockito.anyString(),Mockito.anyString(),Mockito.anyString());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
				"/user/changePassword/anilgupta1682@gmail.com").accept(MediaType.APPLICATION_JSON)
				.param("oldPassword","abc")
				.param("newPassword", "abcd");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "Password changed";
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertEquals(expected, result.getResponse().getContentAsString());
	}

	@Test
	public void loginSuccessfull_whenEmailAndPasswordAreCorrect() throws Exception {

		Mockito.doAnswer((u) -> {
			assertTrue("anilgupta1682@gmail.com".equals(u.getArgument(0))); 
			assertTrue("abc".equals(u.getArgument(1))); 
			return "Welcome Anil Gupta!";
		}).when(userService).login(Mockito.anyString(),Mockito.anyString());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
				"/user/login").accept(MediaType.APPLICATION_JSON)
				.param("email","anilgupta1682@gmail.com")
				.param("password", "abc");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println(result.getResponse().getContentAsString());
		String expected = "Welcome Anil Gupta!";
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		assertEquals(expected, result.getResponse().getContentAsString());
	}
}
