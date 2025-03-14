/*
 * Authors: Yohannes Hailu, Mihir Lochan Maruvada
 * Date: 2025/02/21
 * Description: This class represents a Person with a unique UUID, first name, last name, phone number and a list of their email addresses.
 */
package com.vgb;

import java.util.List;
import java.util.UUID;

public class Person {
	 private UUID personUUID;
	 private String firstName;
	 private String lastName;
	 private String phone;
	 private List<String> emails;
	 
	public Person(UUID personUUID, String firstName, String lastName, String phone, List<String> emails) {
		this.personUUID = personUUID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.emails = emails;
	}

	public UUID getPersonUUID() {
		return personUUID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	 
}
