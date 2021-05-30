package com.jhop.callers.Callers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jhop.callers.models.Phone;
import com.jhop.callers.models.User;
import com.jhop.callers.repositories.PhoneRepository;
import com.jhop.callers.services.PhoneService;
import com.jhop.callers.services.UserService;


@SpringBootTest
public class UserServiceUnitTest {

    @Autowired
    private UserService userService;
    

    
    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        List<User> users = userService.list();

        assertEquals(users.size(), 2);
    }
    
    
    @Test
    public void add_User() {
    	
    	int count = userService.list().size();
    	
    	User u = new User();
    	u.setUserName("Bill");
    	u.setPassword("abc123");
    	u.setEmail("bill@home.com");
    	
    	User newUser = userService.addUser(u);
    	
    	assertEquals(count+1, userService.list().size());
    	assertTrue(newUser.getUserId()!=null);
    }

    
    @Test
    public void add_Phone() {
    	    	
    	User u = new User();
    	u.setUserName("Ted");
    	u.setPassword("abc123");
    	u.setEmail("bill@home.com");
    	
    	User newUser = userService.addUser(u);
    	
    	assertTrue(newUser.getUserId()!=null);


    	Phone p = new Phone();
    	p.setPhoneModel("model name");
    	p.setPhoneName("phone name");
    	p.setPhoneNumber("0011 444333");
    	  	
    	userService.addPhone(newUser.getUserId(), p);

    	User user = userService.getUser(newUser.getUserId());
    	assertEquals(1,user.getPhones().size());

    	//Add another
    	p = new Phone();
    	p.setPhoneModel("model2 name");
    	p.setPhoneName("phone2 name");
    	p.setPhoneNumber("0011 434333");
    	  	
    	userService.addPhone(newUser.getUserId(), p);

    	user = userService.getUser(newUser.getUserId());
    	assertEquals(2,user.getPhones().size());	
    }
}