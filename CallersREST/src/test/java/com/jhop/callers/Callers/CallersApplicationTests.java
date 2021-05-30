package com.jhop.callers.Callers;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhop.callers.models.Phone;
import com.jhop.callers.models.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.json.JSONArray;

@SpringBootTest
@AutoConfigureMockMvc
class CallersApplicationTests {

	@Autowired
	private MockMvc mvc;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	void contextLoads() {
	}

	@Test
	void listUsers() throws Exception {
		MvcResult call1 = mvc.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String result1 = call1.getResponse().getContentAsString();
		System.out.println(result1);
		// now get the id out of result1, and see that it is what we need.
		JSONArray userList = new JSONArray(result1);

		assertEquals(2, userList.length());
	}

	@Test
	void addUser() throws Exception {

		User u = new User();
		u.setUserName("Bill");
		u.setPassword("abc123");
		u.setEmail("bill@home.com");

		String jsonStr = mapper.writeValueAsString(u);

		MvcResult response = mvc
				.perform(MockMvcRequestBuilders.post("/User/").contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(jsonStr))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		User newUser = mapper.readValue(response.getResponse().getContentAsString(), User.class);

		assertEquals(u.getUserName(), newUser.getUserName());
		// assertEquals(null,newUser.getPassword());
		assertEquals(u.getEmail(), newUser.getEmail());

		// we got the auto ID
		assertTrue(newUser.getUserId() != null);

	}

	
	@Test
	void updateUserPreferredPhoneNumber() throws Exception {

		//get the first user
		MvcResult call1 = mvc.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		List<User> allUsers = mapper.readValue(call1.getResponse().getContentAsString(), new TypeReference<List<User>>() {});
		User user = allUsers.get(0);
		

		//update the user
		call1 = mvc.perform(MockMvcRequestBuilders.put("/User/" + user.getUserId() + "?preferredPhoneNumber=0861112222").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();
		
		User updatedUser = mapper.readValue(call1.getResponse().getContentAsString(),User.class);
		
		assertEquals("0861112222", updatedUser.getPreferredPhoneNumner());

		//get it again, to be sure it is really persisted the change
		call1 = mvc.perform(MockMvcRequestBuilders.get("/User/" + user.getUserId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();
		
		updatedUser = mapper.readValue(call1.getResponse().getContentAsString(),User.class);
		assertEquals("0861112222", updatedUser.getPreferredPhoneNumner());

	}

	

	@Test
	void updateUserOthers() throws Exception {

		//get the first user
		MvcResult call1 = mvc.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		List<User> allUsers = mapper.readValue(call1.getResponse().getContentAsString(), new TypeReference<List<User>>() {});
		User user = allUsers.get(0);
		

		//update the user
		call1 = mvc.perform(MockMvcRequestBuilders.put("/User/" + user.getUserId() + "?name=tom&email=tom@home.net").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();
		
		User updatedUser = mapper.readValue(call1.getResponse().getContentAsString(),User.class);
		
		assertEquals("tom", updatedUser.getUserName());
		assertEquals("tom@home.net", updatedUser.getEmail());

	}

	
	@Test
	void deleteUser() throws Exception {

		// Get the initial number of users
		MvcResult call1 = mvc.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String result1 = call1.getResponse().getContentAsString();
		System.out.println(result1);
		// now get the id out of result1, and see that it is what we need.
		JSONArray userList = new JSONArray(result1);
		int initialSize = userList.length();

		// Add a new user
		User u = new User();
		u.setUserName("Ben");
		u.setPassword("abc123");
		u.setEmail("bill@home.com");

		String jsonStr = mapper.writeValueAsString(u);

		MvcResult response = mvc
				.perform(MockMvcRequestBuilders.post("/User/").contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(jsonStr))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		User newUser = mapper.readValue(response.getResponse().getContentAsString(), User.class);

		assertEquals(u.getUserName(), newUser.getUserName());
		assertEquals(u.getEmail(), newUser.getEmail());

		// we got the auto ID
		assertTrue(newUser.getUserId() != null);

		// Check we added one
		call1 = mvc.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		result1 = call1.getResponse().getContentAsString();
		System.out.println(result1);
		// now get the id out of result1, and see that it is what we need.
		userList = new JSONArray(result1);
		assertEquals(initialSize + 1, userList.length());

		// delete the user
		response = mvc.perform(MockMvcRequestBuilders.delete("/User/" + newUser.getUserId())
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		// Check it is gone
		call1 = mvc.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		result1 = call1.getResponse().getContentAsString();
		System.out.println(result1);
		// check the number of users is still correct
		userList = new JSONArray(result1);
		assertEquals(initialSize, userList.length());

		// delete a non user
		response = mvc.perform(MockMvcRequestBuilders.delete("/User/11111111-b173-4e1b-8f26-2e0500655b82")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

		// Check it is gone
		call1 = mvc.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		result1 = call1.getResponse().getContentAsString();
		System.out.println(result1);

		// check the number of users is still correct
		userList = new JSONArray(result1);
		assertEquals(initialSize, userList.length());

	}

	@Test
	void addPhone() throws Exception {
		// get the users, we will add to the first one.
		MvcResult call1 = mvc.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String result1 = call1.getResponse().getContentAsString();
		System.out.println(result1);
		// now get the id out of result1, and see that it is what we need.
		JSONArray userList = new JSONArray(result1);

		User newUser = mapper.readValue(userList.get(0).toString(), User.class);

		// Get the users phones
		MvcResult response = mvc.perform(MockMvcRequestBuilders.get("/User/" + newUser.getUserId() + "/Phone")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		List<Phone> phones = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Phone>>() {});
		// There are no phones yet
		assertEquals(0, phones.size());

		// Add a new phone
		Phone newPhone = new Phone();
		newPhone.setPhoneModel("2001XXL2");
		newPhone.setPhoneName("My first Phone");
		newPhone.setPhoneNumber("1800 1122111");

		String jsonStr = mapper.writeValueAsString(newPhone);

		response = mvc
				.perform(MockMvcRequestBuilders.post("/User/" + newUser.getUserId() + "/Phone")
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(jsonStr))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		// Get the users phones again
		response = mvc.perform(MockMvcRequestBuilders.get("/User/" + newUser.getUserId() + "/Phone")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		List<Phone> phonesAfter = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Phone>>() {});
		// The new phone is there
		assertEquals(1, phonesAfter.size());

		// Add another new phone
		newPhone = new Phone();
		newPhone.setPhoneModel("2001XXL2");
		newPhone.setPhoneName("My second Phone");
		newPhone.setPhoneNumber("1800 11332111");

		jsonStr = mapper.writeValueAsString(newPhone);

		response = mvc
				.perform(MockMvcRequestBuilders.post("/User/" + newUser.getUserId() + "/Phone")
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(jsonStr))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		// Get the users phones again
		response = mvc.perform(MockMvcRequestBuilders.get("/User/" + newUser.getUserId() + "/Phone")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		phonesAfter = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Phone>>() {});
		// now we have both phones
		assertEquals(2, phonesAfter.size());

	}

	@Test
	void deletePhone() throws Exception {
		// get the users, we will add to the first one.
		MvcResult call1 = mvc.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String result1 = call1.getResponse().getContentAsString();
		System.out.println(result1);
		// now get the id out of result1, and see that it is what we need.
		JSONArray userList = new JSONArray(result1);
		User newUser = mapper.readValue(userList.get(0).toString(), User.class);

		// Get the users phones
		MvcResult response = mvc.perform(MockMvcRequestBuilders.get("/User/" + newUser.getUserId() + "/Phone")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		List<Phone> phones = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Phone>>() {});
		// The initial number of phones.  There might be some from previous tests
		int initialPhones = phones.size();

		// Add a new phone
		Phone newPhone = new Phone();
		newPhone.setPhoneModel("2001XXL2");
		newPhone.setPhoneName("My first Phone");
		newPhone.setPhoneNumber("1800 1122111");

		String jsonStr = mapper.writeValueAsString(newPhone);

		response = mvc
				.perform(MockMvcRequestBuilders.post("/User/" + newUser.getUserId() + "/Phone")
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(jsonStr))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		// Get the users phones again
		response = mvc.perform(MockMvcRequestBuilders.get("/User/" + newUser.getUserId() + "/Phone")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		List<Phone> phonesAfter = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Phone>>() {});
		// The new phone is there
		assertEquals(initialPhones+1, phonesAfter.size());

		Phone realPhone = phonesAfter.get(0);

		// Delete the phone
		response = mvc.perform(
				MockMvcRequestBuilders.delete("/User/" + newUser.getUserId() + "/Phone/" + realPhone.getPhoneId())
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		
		// Get the users phones again
		response = mvc.perform(MockMvcRequestBuilders.get("/User/" + newUser.getUserId() + "/Phone")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		phonesAfter = mapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Phone>>() {});
		// The new phone is gone
		assertEquals(initialPhones, phonesAfter.size());

		// Delete it again
		response = mvc.perform(
				MockMvcRequestBuilders.delete("/User/" + newUser.getUserId() + "/Phone/" + realPhone.getPhoneId())
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
	}

}
