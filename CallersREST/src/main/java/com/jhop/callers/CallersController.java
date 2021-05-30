package com.jhop.callers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jhop.callers.services.UserService;
import com.jhop.callers.models.*;

@RestController
public class CallersController {

	private static final Logger logger = LoggerFactory.getLogger(CallersController.class);
	
	private UserService userService;
	
	//Because this is a component, Spring will create a UserService and pass it in
	CallersController (UserService userService){
		this.userService = userService;
	}
	
	@GetMapping("/User")
	public List<User> getUsers(){
		//TODO apparently we should not return Entity objects directly.  We will leave this for now.
		logger.info("getUsers() - start()");
		
		//Set all the passwords to null, we dont want to return them
		List<User> result = userService.listUsers();
		result.stream().forEach(u -> u.setPassword(null));
		
		return userService.listUsers();
	}

	@PostMapping("/User")
	public User addUser(@RequestBody User newUser){

		logger.info("addUser() - start() - {}", newUser);
		
		//return the newly created user.  mostly so you can see the ID
		User user = userService.addUser(newUser);
		//don't return passwords, that rude
		user.setPassword(null);
		return user;
	}

	@GetMapping("/User/{userId}")
	public User getUser(@PathVariable UUID userId){
		//TODO apparently we should not return Entity objects directly.  We will leave this for now.
		logger.info("getUser() - start()");
		
		User result = userService.getUser(userId);
		//set the password to null, we dont want to return passwords
		result.setPassword(null);
		
		return result;
	}
	
	@DeleteMapping("/User/{userId}")
	public void deleteUser(@PathVariable UUID userId) {
		logger.info("deleteUser() - start() - {}", userId);
		
		try {
		userService.deleteUser(userId);
		} catch(EmptyResultDataAccessException e) {
			logger.info("deleteUser() - User {} doesnt exist", userId);
			throw new ResponseStatusException(
			          HttpStatus.NOT_FOUND, "User Does Not Exist");
		}
	}

	
	
	@PutMapping("/User/{userId}")
	public User updateUser(@PathVariable UUID userId, @RequestParam(required = false) String preferredPhoneNumber, @RequestParam(required = false) String name,@RequestParam(required = false) String email) {
		logger.info("deleteUser() - start() - {}", userId);
	
		/*
		 * Im not super sure on this one.  There are hints that this should always PUT a whole JSON user.  However this seems excessive if you 
		 * only want to change one value.  So lets let them specify values as parameters.
		 */
		
		User user = userService.getUser(userId);
		
		if(preferredPhoneNumber != null) {
			user.setPreferredPhoneNumner(preferredPhoneNumber);
		}
		if(name != null) {
			user.setUserName(name);
		}
		if(email != null) {
			user.setEmail(email);
		}		
		
		user = userService.updateUser(user);
		
		//return the updated one
		return user;
		
	}
	
	
	@GetMapping("/User/{userId}/Phone")
	public List<Phone> getUsersPhones(@PathVariable UUID userId){
		logger.info("getUsersPhones() - start() - {}", userId);
		
		User user = userService.getUser(userId);
		if(user!=null) {
			return user.getPhones();
		}
		
		//return an empty set is better than null
		return new ArrayList<>();
	}

	@PostMapping("/User/{userId}/Phone")
	public void addUsersPhone(@PathVariable UUID userId,@RequestBody Phone newPhone){	
		logger.info("addUsersPhone() - start() - {},{}", userId,newPhone);
		
		userService.addPhone(userId, newPhone);
	}
	
	
	@DeleteMapping("/User/{userId}/Phone/{phoneId}")
	public void deletePhone(@PathVariable UUID userId,@PathVariable UUID phoneId) {
		logger.info("deletePhone() - start() - {},{}", userId,phoneId);
		
		try {
			userService.deletePhone(userId,phoneId);
		} catch(EmptyResultDataAccessException e) {
			logger.info("deletePhone() - User or Phone doesnt exist", userId);
			throw new ResponseStatusException(
			          HttpStatus.NOT_FOUND, "User or Phone does Not Exist");
		}
	}
	
}
