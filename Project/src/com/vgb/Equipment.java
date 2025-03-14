/*
 * Authors: Yohannes Hailu, Mihir Lochan Maruvada
 * Date: 2025/02/21
 * Description: Represents an Equipment, which is a type of Item, with a model number and a retail price. It also inherits UUID, type and name from Item.  
 */

package com.vgb;

import java.util.UUID;

public class Equipment extends Item {
	private String modelNumber;
	private double retailPrice;
	
	public Equipment(UUID itemUUID, String itemName, String modelNumber, double retailPrice) {
		super(itemUUID, itemName);
		this.modelNumber = modelNumber;
		this.retailPrice = retailPrice;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	@Override
	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}
	
	@Override
	public double preTaxCost() {
		return Math.round(retailPrice*100)/100;
	}
	
	@Override
	public double calculateCost() {
		return retailPrice + getTaxes();
	}
	
	@Override
	public double getTaxes() {
	    double tax = preTaxCost() * 0.0525; 
	    return Math.round(tax * 100.0) / 100.0; 
	}

	@Override
	public String toString() {
		String formatted = String.format("Name %s %s Price %.2f", super.getItemName(), modelNumber, retailPrice);
	    System.out.println(formatted);
		return formatted;
	}
}

