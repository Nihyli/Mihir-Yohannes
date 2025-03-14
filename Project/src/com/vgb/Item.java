/*
 * Authors: Yohannes Hailu, Mihir Lochan Maruvada
 * Date: 2025/02/21
 * Description: Represents a generic Item which is the base for other items like Equipment, Material and Contract. It provides standard fields like UUID, type and name. 
 */
package com.vgb;

import java.util.UUID;

public abstract class Item {
	private UUID itemUUID;
	private String itemName;
	
	public Item(UUID itemUUID, String itemName) {
		this.itemUUID = itemUUID;
		this.itemName = itemName;
	}

	public UUID getItemUUID() {
		return itemUUID;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public abstract double calculateCost();	
	public abstract double preTaxCost();
	public abstract double getTaxes();
	public abstract double getRetailPrice();
}
