package com.jhop.callers.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhop.callers.models.Phone;
import com.jhop.callers.models.User;
import com.jhop.callers.repositories.PhoneRepository;
import com.jhop.callers.repositories.UserRepository;

@Service
public class PhoneService {
	
    @Autowired
    private PhoneRepository phoneRepository;

    
    public Phone addPhone(Phone aPhone) {
    	Phone phone = phoneRepository.saveAndFlush(aPhone);
    	//return the one we made
    	return phone;
    }    

}
