package com.vgb;

import java.util.UUID;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public class Lease extends Equipment {
	private LocalDate startDate, endDate;
	
	public Lease(UUID itemUUID, String itemName, String modelNumber, double retailPrice, LocalDate startDate, LocalDate endDate)
	{
		super(itemUUID, itemName, modelNumber, retailPrice);	
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	@Override
	public double preTaxCost() {
	    // Calculate the number of days in the lease (inclusive of both start and end dates)
	    long leaseDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

	    // Calculate the lease duration as a fraction of the amortization period (5 years)
	    double leaseDurationYears = leaseDays / 365.0; // Use 365.0 to force floating-point division

	    // Calculate the pre-tax cost: (lease duration / 5 years) * retail price * 1.5 (50% markup)
	    double preTax = (leaseDurationYears / 5.0) * super.getRetailPrice() * 1.5;

	    // Round to 2 decimal places
	    return Math.round(preTax * 100.0) / 100.0;
	}

	@Override
	public double calculateCost() {
		double cost = preTaxCost() + getTaxes();
		return  Math.round(cost * 100)/100;
	}
	
	@Override
	public double getTaxes() {
		if (preTaxCost() < 5000)
		{
			return 0;
		}
		else if(preTaxCost() < 12500)
		{
			return 500;
		}
		else 
		{
			return 1500;
		}
	}
	
		
	@Override
	public String toString() {
		String formatted = String.format("Name %s %s %s Price %.2f", super.getItemName(), startDate, endDate, super.getRetailPrice());
	    System.out.println(formatted);
		return formatted;
	}
}
