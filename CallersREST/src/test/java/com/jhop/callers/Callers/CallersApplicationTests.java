package com.jhop.callers.Callers;

import org.junit.jupiter.api.Test;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhop.callers.models.Phone;
import com.jhop.callers.models.User;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.json.JSONArray;
import org.json.JSONObject;

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
		MvcResult call1 = mvc
				.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String result1 = call1.getResponse().getContentAsString();
		System.out.println(result1);
		// now get the id out of result1, and see that it is what we need.
		JSONArray userList = new JSONArray(result1);
		
		assertEquals(2,userList.length());
	}
	
	
	@Test
	void addUser() throws Exception {
		
    	User u = new User();
    	u.setUserName("Bill");
    	u.setPassword("abc123");
    	u.setEmail("bill@home.com");
    	
    	
		String jsonStr = mapper.writeValueAsString(u);

		MvcResult response = mvc.perform(MockMvcRequestBuilders.post("/User/").contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(jsonStr))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		User newUser = mapper.readValue(response.getResponse().getContentAsString(), User.class);
	
		assertEquals(u.getUserName(),newUser.getUserName());
	//	assertEquals(null,newUser.getPassword());
		assertEquals(u.getEmail(),newUser.getEmail());
		
		//we got the auto ID
		assertTrue(newUser.getUserId()!=null);
		
		
	}
	
	
	@Test
	void addPhone() throws Exception {
		//get the users, we will add to the first one.
		MvcResult call1 = mvc
				.perform(MockMvcRequestBuilders.get("/User").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String result1 = call1.getResponse().getContentAsString();
		System.out.println(result1);
		// now get the id out of result1, and see that it is what we need.
		JSONArray userList = new JSONArray(result1);
		
		User newUser = mapper.readValue(userList.get(0).toString(), User.class);
		

		//Get the users phones
		MvcResult response = mvc.perform(MockMvcRequestBuilders.get("/User/" + newUser.getUserId() + "/Phone").contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<Phone> phones = mapper.readValue(response.getResponse().getContentAsString(), List.class);
		//There are no phones yet
		assertEquals(0,phones.size());
		
		
		//Add a new phone
		Phone newPhone = new Phone();
		newPhone.setPhoneModel("2001XXL2");
		newPhone.setPhoneName("My first Phone");
		newPhone.setPhoneNumber("1800 1122111");
		
		String jsonStr = mapper.writeValueAsString(newPhone);

		response = mvc.perform(MockMvcRequestBuilders.post("/User/" + newUser.getUserId() + "/Phone").contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(jsonStr))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		//Get the users phones again
		response = mvc.perform(MockMvcRequestBuilders.get("/User/" + newUser.getUserId() + "/Phone").contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<Phone> phonesAfter = mapper.readValue(response.getResponse().getContentAsString(), List.class);
		//There are no phones yet
		assertEquals(1,phonesAfter.size());
	}

}