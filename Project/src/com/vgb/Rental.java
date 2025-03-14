package com.vgb;

import java.util.UUID;

public class Rental extends Equipment{
	double perHourCharge = super.getRetailPrice() * 0.001;
	double hoursRented;
	
	public Rental(UUID itemUUID, String itemName, String modelNumber, double retailPrice, double hoursRented)
	{
		super(itemUUID, itemName, modelNumber, retailPrice);	
		this.hoursRented = hoursRented;
	}
	
	
	@Override
	public double calculateCost() {
		double cost = (perHourCharge * hoursRented)+getTaxes();
		return Math.round(cost*100.0)/100.0;
	}
	
	@Override
	public double preTaxCost() {
		double preTax = perHourCharge * hoursRented;
		return preTax;
	}
	
	@Override
	public double getTaxes() {
	    double tax = preTaxCost() * (4.38 / 100);
	    return Math.round(tax * 100.0) / 100.0; // Use 100.0 for floating-point division
	}
	
	@Override
	public String toString() {
		String formatted = String.format("Name %s %s %s Price %.2f", super.getItemName(), perHourCharge, hoursRented, calculateCost());
	    System.out.println(formatted);
		return formatted;
	}

}
