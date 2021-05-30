package com.jhop.callers.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.jhop.callers.models.Phone;
import com.jhop.callers.models.User;
import com.jhop.callers.repositories.PhoneRepository;
import com.jhop.callers.repositories.UserRepository;

@Service
/**
 * This handles users and their phones.  Phones don't really exist independent of users, so for this example we handle them together
 * @author jhopper
 *
 */
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    public List<User> listUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Return all of the phones, for all users.
     * 
     * This isn't really needed for the functionality, but it is helpful for testing, to ensure there are no orphan phones
     * 
     * @return
     */
    public List<Phone> listPhones() {
        return phoneRepository.findAll();
    }
    
    public User addUser(User aUser) {
    	User user = userRepository.save(aUser);
    	//return the one we made
    	return user;
    }
    
    public User updateUser(User aUser) {
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
    	try {
    		userRepository.deleteById(userID);
    	} catch(EmptyResultDataAccessException e){
    		logger.info("deleteUser() - User {} does not exist",userID);
    		
    		//log the issue, and then throw it again so that it can be handled higher up
    		throw e;
    	}
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
    
    /**
     * Remove a phone from a user
     * @param userID
     * @param aPhone
     */
    public void deletePhone(UUID userID, UUID phoneId) {
    	//Get the user
    	Optional<User> userOpt = userRepository.findById(userID);
    	if(userOpt.isPresent()) {
    		User user = userOpt.get();
    		//Get the specific phone
    		List<Phone> myphones = user.getPhones();
    		
    		//remove the phone from the phoneRepository
    		Optional<Phone> endangeredPhone = myphones.stream().filter(p -> p.getPhoneId().equals(phoneId)).findFirst();
    		if(endangeredPhone.isPresent()) {
    			phoneRepository.delete(endangeredPhone.get());	
    		} else {
    			throw new EmptyResultDataAccessException("Phone does not exist", 1);
    		}
    	} else {
    		throw new EmptyResultDataAccessException("User does not exist", 1);
    	}
    }

}
