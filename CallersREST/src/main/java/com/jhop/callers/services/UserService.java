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
public class UserService {
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    public List<User> list() {
        return userRepository.findAll();
    }
    
    public User addUser(User aUser) {
    	User user = userRepository.save(aUser);
    	//return the one we made
    	return user;
    }

    
    public User getUser(UUID userID) {
    	Optional<User> user = userRepository.findById(userID);    	
    	if(user.isPresent()) {
    		return user.get();
    	}
    	return null;
    }
    
    
    public void deleteUser(UUID userID) {
    	userRepository.deleteById(userID);    	
    }
    
    
    /**
     * 
     * @param userID
     * @param aPhone
     */
    public void addPhone(UUID userID, Phone aPhone) {
    	
    	//This LOOKS like adding a phone to a user.
    	//but really we want to add an owner to a phone.
    	//That is the way the database is set up, phones have owners.  
    	//owners dont actually have phones in the database.
    	
    	//getById or FindById. 
    	//getById returns everything null?
    	Optional<User> userOpt = userRepository.findById(userID);
    	
    	if(userOpt.isPresent()) {
    		User user = userOpt.get();
    		aPhone.setOwner(user);
    		phoneRepository.save(aPhone);
    	}
    }
    

}