package com.vgb;

import java.util.UUID;

public class Rental extends Equipment {
    private double perHourCharge = super.getRetailPrice() * 0.001;
    private double hoursRented;
    
    public Rental(UUID itemUUID, String itemName, String modelNumber, double retailPrice, double hoursRented) {
        super(itemUUID, itemName, modelNumber, retailPrice);  
        this.hoursRented = hoursRented;
    }
    
    @Override
    public double calculateCost() {
        double cost = (perHourCharge * hoursRented) + getTaxes();
        return Math.round(cost * 100.0) / 100.0;
    }
    
    @Override
    public double preTaxCost() {
        return perHourCharge * hoursRented;
    }
    
    @Override
    public double getTaxes() {
        double tax = preTaxCost() * (4.38 / 100);
        return Math.round(tax * 100.0) / 100.0;
    }
    
    @Override
    public String toString() {
        String formatted = String.format("Name %s %.2f hours @ $%.2f/hour Total %.2f", 
                super.getItemName(), hoursRented, perHourCharge, calculateCost());
        System.out.println(formatted);
        return formatted;
    }
    
    public double getHoursRented() {
        return hoursRented;
    }
    
    public double getPerHourCharge() {
        return perHourCharge;
    }
}
