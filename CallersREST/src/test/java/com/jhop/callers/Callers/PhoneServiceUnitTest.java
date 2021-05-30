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
import com.jhop.callers.services.PhoneService;
import com.jhop.callers.services.UserService;


@SpringBootTest
public class PhoneServiceUnitTest {

    @Autowired
    private PhoneService phoneService;
       
    @Test
    public void add_Phone() {
    	
    	Phone u = new Phone();
    	u.setPhoneModel("model name");
    	u.setPhoneName("phone name");
    	u.setPhoneNumber("0011 444333");
    	
    	Phone newPhone = phoneService.addPhone(u);
    	
    
    	assertTrue(newPhone.getPhoneId()!=null);
    }
    
}
