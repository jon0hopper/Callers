package com.jhop.callers.Callers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import com.jhop.callers.models.Phone;
import com.jhop.callers.models.User;
import com.jhop.callers.services.UserService;

@SpringBootTest
public class UserServiceUnitTest {

	@Autowired
	private UserService userService;

	@Test
	public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
		List<User> users = userService.listUsers();

		assertEquals(users.size(), 2);
	}

	@Test
	public void add_User() {

		int count = userService.listUsers().size();

		User u = new User();
		u.setUserName("Bill");
		u.setPassword("abc123");
		u.setEmail("bill@home.com");

		User newUser = userService.addUser(u);

		assertEquals(count + 1, userService.listUsers().size());
		assertTrue(newUser.getUserId() != null);
	}

	@Test
	public void delete_User() {

		// get the initial number of users
		int count = userService.listUsers().size();

		int countPhones = userService.listPhones().size();

		// Add a new one (to delete later)
		User u = new User();
		u.setUserName("Baily");
		u.setPassword("abc123");
		u.setEmail("bill@home.com");

		User newUser = userService.addUser(u);

		assertEquals(count + 1, userService.listUsers().size());
		assertTrue(newUser.getUserId() != null);

		// give the user a phone, so we can see it gets deleted when the user goes
		Phone p = new Phone();
		p.setPhoneModel("model name");
		p.setPhoneName("phone name");
		p.setPhoneNumber("0011 444333");

		userService.addPhone(newUser.getUserId(), p);
		// phone was added
		assertEquals(countPhones + 1, userService.listPhones().size());

		// Delete our new user
		userService.deleteUser(newUser.getUserId());
		// its gone
		assertEquals(count, userService.listUsers().size());

		// phone was removed
		assertEquals(countPhones, userService.listPhones().size());

		// delete a non-existant user
		try {
			userService.deleteUser(UUID.fromString("22222222-b173-4e1b-8f26-2e0500655b82"));
			fail("this should throw an exception because th user doesnt exist");
		} catch (EmptyResultDataAccessException e) {
			// this is the expected path
		}
		assertEquals(count, userService.listUsers().size());

	}

	@Test
	public void add_Phone() {

		User u = new User();
		u.setUserName("Ted");
		u.setPassword("abc123");
		u.setEmail("bill@home.com");

		User newUser = userService.addUser(u);

		assertTrue(newUser.getUserId() != null);

		Phone p = new Phone();
		p.setPhoneModel("model name");
		p.setPhoneName("phone name");
		p.setPhoneNumber("0011 444333");

		userService.addPhone(newUser.getUserId(), p);

		User user = userService.getUser(newUser.getUserId());
		assertEquals(1, user.getPhones().size());

		// Add another
		p = new Phone();
		p.setPhoneModel("model2 name");
		p.setPhoneName("phone2 name");
		p.setPhoneNumber("0011 434333");

		userService.addPhone(newUser.getUserId(), p);

		user = userService.getUser(newUser.getUserId());
		assertEquals(2, user.getPhones().size());
	}

	@Test
	public void remove_Phone() {

		int totalPhoneCount = userService.listPhones().size();

		// Add a new user and their phone
		User u = new User();
		u.setUserName("Vlad");
		u.setPassword("abc123");
		u.setEmail("bill@home.com");

		User newUser = userService.addUser(u);

		Phone p1 = new Phone();
		p1.setPhoneModel("model name");
		p1.setPhoneName("vlads phone name");
		p1.setPhoneNumber("0011 444333");

		userService.addPhone(newUser.getUserId(), p1);

		// Check it worked
		User user = userService.getUser(newUser.getUserId());
		assertEquals(1, user.getPhones().size());
		assertEquals(totalPhoneCount + 1, userService.listPhones().size());

		// Add another
		Phone p2 = new Phone();
		p2.setPhoneModel("model2 name");
		p2.setPhoneName("phone2 name");
		p2.setPhoneNumber("0011 434333");

		userService.addPhone(newUser.getUserId(), p2);

		// check it worked
		user = userService.getUser(newUser.getUserId());
		assertEquals(2, user.getPhones().size());
		assertEquals(totalPhoneCount + 2, userService.listPhones().size());

		userService.deletePhone(newUser.getUserId(), p1.getPhoneId());

		user = userService.getUser(newUser.getUserId());
		assertEquals(1, user.getPhones().size());
		assertEquals(totalPhoneCount + 1, userService.listPhones().size());

		// Test removing a non-existant phone
		try {
			userService.deletePhone(newUser.getUserId(), UUID.fromString("11111111-b173-4e1b-8f26-2e0500655b82"));
			fail("Expect Exception from invalid phone");
		} catch (EmptyResultDataAccessException e) {
			// this is the expected path
			user = userService.getUser(newUser.getUserId());
			assertEquals(1, user.getPhones().size());
		}

		// test removing a real phone from a non-existent user
		try {
			userService.deletePhone(UUID.fromString("22222222-b173-4e1b-8f26-2e0500655b82"), p2.getPhoneId());
			fail("Expect Exception from invalid phone");
		} catch (EmptyResultDataAccessException e) {
			// this is the expected path
			user = userService.getUser(newUser.getUserId());
			assertEquals(1, user.getPhones().size());
		}

		// test removing the same phone again
		try {
			userService.deletePhone(newUser.getUserId(), p1.getPhoneId());
			fail("Expect Exception from invalid phone");
		} catch (EmptyResultDataAccessException e) {
			// this is the expected path
			user = userService.getUser(newUser.getUserId());
			assertEquals(1, user.getPhones().size());
		}

		// test removing the second phone
		userService.deletePhone(newUser.getUserId(), p2.getPhoneId());
		// this is the expected path
		user = userService.getUser(newUser.getUserId());
		assertEquals(0, user.getPhones().size());
		assertEquals(totalPhoneCount, userService.listPhones().size());

		// test removal when there are no phones
		// test removing the same phone again
		try {
			userService.deletePhone(newUser.getUserId(), p1.getPhoneId());
			fail("Expect Exception from invalid phone");
		} catch (EmptyResultDataAccessException e) {
			// this is the expected path
			user = userService.getUser(newUser.getUserId());
			assertEquals(0, user.getPhones().size());
		}
	}
}
