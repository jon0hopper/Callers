package com.jhop.callers.models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Phone {

	@Id
	@GeneratedValue
	@Getter
	@Setter
	private UUID phoneId;

	@Getter
	@Setter
	private String phoneName;

	@Getter
	@Setter
	private String phoneModel;

	@Getter
	@Setter
	private String phoneNumber;
	// Assuming one number per phone. Otherwise we need to model sims in phones.

	// The ID of the user who owns this phone.  owner_id is stored in the db table
	@ManyToOne
	@JoinColumn(name = "owner_id")
	@Setter
	private User owner;

}
