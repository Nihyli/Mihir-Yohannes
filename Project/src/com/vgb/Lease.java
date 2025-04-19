package com.vgb;

import java.util.UUID;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Lease extends Equipment {
    private LocalDate startDate, endDate;
    
    public Lease(UUID itemUUID, String itemName, String modelNumber, double retailPrice, LocalDate startDate, LocalDate endDate) {
        super(itemUUID, itemName, modelNumber, retailPrice);  
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    @Override
    public double preTaxCost() {
        long leaseDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double leaseDurationYears = leaseDays / 365.0;
        double preTax = (leaseDurationYears / 5.0) * super.getRetailPrice() * 1.5;
        return Math.round(preTax * 100.0) / 100.0;
    }

    @Override
    public double calculateCost() {
        double cost = preTaxCost() + getTaxes();
        return Math.round(cost * 100.0) / 100.0;
    }
    
    @Override
    public double getTaxes() {
        if (preTaxCost() < 5000) {
            return 0;
        } else if (preTaxCost() < 12500) {
            return 500;
        } else {
            return 1500;
        }
    }
    
    @Override
    public String toString() {
        String formatted = String.format("Name %s %s %s Price %.2f", super.getItemName(), startDate, endDate, super.getRetailPrice());
        System.out.println(formatted);
        return formatted;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    @Override
    public void printDetailedReport() {
        System.out.printf("%s (Lease) %s-%s\n",getItemUUID(), getItemName(), getModelNumber());
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        System.out.printf("\t%d days (%s -> %s)\n",days, startDate, endDate);
        double tax = getTaxes();
        double preTax = preTaxCost();
        System.out.printf("%60s $%11.2f $%11.2f\n","", tax, preTax);
    }
}
