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
        return Math.round(preTax * 100.0) / 100.0;
    }
    
    @Override
    public double getTaxes() {
        double tax = preTaxCost() * (7.15 / 100);
        return Math.round(tax * 100.0) / 100.0;
    }
    
    @Override
    public double calculateCost() {
        double cost = preTaxCost() + getTaxes();
        return Math.round(cost * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        String formatted = String.format("Name %s Price %.2f", super.getItemName(), calculateCost());
        System.out.println(formatted);
        return formatted;
    }

    public int getQuantity() {
        return quantity;
    }
    
    @Override
    public double getRetailPrice() {
        return preTaxCost();
    }
    
    @Override
    public void printDetailedReport() {
        System.out.printf("%s (Material) %s\n",
            getItemUUID(), getItemName());
        System.out.printf("\t%d @ $%.2f/%s\n",
            quantity, costPerUnit, itemUnit);
        double tax = getTaxes();
        double preTax = preTaxCost();
        System.out.printf("%60s $%11.2f $%11.2f\n","", tax, preTax);
    }
}
