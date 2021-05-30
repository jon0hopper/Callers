package com.jhop.callers.models;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
public class User {

	@Id
	@GeneratedValue
	@Getter
	@Setter
	private UUID userId;
	
	@Getter
	@Setter
	private String userName;
	
	@Getter
	@Setter
	private String password;
	
	@Getter
	@Setter
	private String email;
	
	@Getter
	@Setter
	private String preferredPhoneNumner;
	
	//EAGER helps with lazy initialization.  There can be performance issues with this, but overcoming them adds complexity
	//REMOVE ensures that phones are removed when their users are removed
	@OneToMany(mappedBy="owner",fetch = FetchType.EAGER,cascade=CascadeType.REMOVE)
	@Getter
	private List<Phone> phones;
	
	public void addPhone(Phone phone) {
		phones.add(phone);
	}
	
}
