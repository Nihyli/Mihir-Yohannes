/*
 * Authors: Yohannes Hailu, Mihir Lochan Maruvada
 * Date: 2025/02/21
 * Description: This class represents a Compamny with a companyUUID, contactUUID, companyName and address.
 */

package com.vgb;

import java.util.UUID;

public class Company {
	
	private UUID companyUUID;
	private UUID contactUUID;
	private String companyName;
	private Address address;
	
	public Company(UUID companyUUID, UUID contactUUID, String companyName, Address address) {
		this.companyUUID = companyUUID;
		this.contactUUID = contactUUID;
		this.companyName = companyName;
		this.address = address;
	}

	public UUID getCompanyUUID() {
		return companyUUID;
	}

	public UUID getContactUUID() {
		return contactUUID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public Address getAddress() {
		return address;
	}

	
	
	
}
