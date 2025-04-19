/*
 * Authors: Yohannes Hailu, Mihir Lochan Maruvada
 * Date: 2025/02/21
 * Description: Represents an Contract, which is a type of Item, with a companyID. It also inherits UUID, type and name from Item.  
 */

package com.vgb;

import java.util.UUID;

public class Contract extends Item {
	private UUID companyUUID;
	private double contractCost;

	public Contract(UUID itemUUID, String itemName, UUID companyUUID, double contractCost) {
		super(itemUUID, itemName);
		this.companyUUID = companyUUID;
		this.contractCost = contractCost;
	}
	
	public Contract(UUID itemUUID, String itemName, UUID companyUUID) {
		super(itemUUID, itemName);
		this.companyUUID = companyUUID;
	}

	public UUID getCompanyUUID() {
		return companyUUID;
	}
	
	public double calculateCost() {
		return Math.round(contractCost * 100)/100;
	}

	@Override
	public double preTaxCost() {
		return Math.round(contractCost * 100.0)/100.0;
	}

	@Override
	public double getTaxes() {
		
		return 0;
	}
	
	 @Override
	    public String toString() {
	        String formatted = String.format("Name: %s, Total Cost: %.2f", super.getItemName(), calculateCost());
	        System.out.println(formatted);
	        return formatted;
	    }

	@Override
	public double getRetailPrice() {
		return calculateCost();
	}
	
    @Override
    public void printDetailedReport() {
        System.out.printf("%s (Contract) %s\n",getItemUUID(), getItemName());
        double tax = getTaxes();
        double preTax = preTaxCost();
        System.out.printf("%60s $%11.2f $%11.2f\n","", tax, preTax);
    }
}
