/*
 * Authors: Yohannes Hailu, Mihir Lochan Maruvada
 * Date: 2025/02/21
 * Description: Represents a Material, which is a type of Item, with a measurement unit and a cost per unit. It also inherits UUID, type and name from Item. 
 */
package com.vgb;

import java.util.UUID;

public class Material extends Item {
	private String itemUnit;
	private double costPerUnit;
	private int quantity;
	
	public Material(UUID itemUUID, String itemName, String itemUnit, double costPerUnit, int quantity) {
		super(itemUUID, itemName);
		this.itemUnit = itemUnit;
		this.costPerUnit = costPerUnit;
		this.quantity = quantity;
	}
	
	public Material(UUID itemUUID, String itemName, String itemUnit, double costPerUnit) {
		super(itemUUID, itemName);
		this.itemUnit = itemUnit;
		this.costPerUnit = costPerUnit;
	}
	
	public String getItemUnit() {
		return itemUnit;
	}

	public double getCostPerUnit() {
		return costPerUnit;
	}

	public void setCostPerUnit(double costPerUnit) {
		this.costPerUnit = costPerUnit;
	}
	
	@Override
	public double preTaxCost() {
		double preTax = quantity * costPerUnit;
		return Math.round(preTax * 100.0)/100.0;
	}
	
	@Override
	public double getTaxes() {
		double tax = preTaxCost() * (7.15/100);
		return Math.round(tax * 100.0)/100.0;
	}
	
	@Override
	public double calculateCost() {
		double cost = preTaxCost() + getTaxes();
		return Math.round(cost * 100.0)/100.0;
	}

	@Override
	public String toString() {
		String formatted = String.format("Name %s Price %.2f", super.getItemName(), calculateCost());
	    System.out.println(formatted);
		return formatted;
	}

	@Override
	public double getRetailPrice() {
		// Auto-generated method stub
		return 0;
	}
	
}
