package com.vgb;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Invoice {

    private UUID uuid;
    private Company company;
    private Person salesPerson;
    private LocalDate invoiceDate;
    private double invoiceAmount;
    private Map<UUID, Item> invoiceItems;

    public Invoice(UUID uuid, Company company, Person salesPerson, LocalDate invoiceDate, double invoiceAmount, Map<UUID, Item> invoiceItems) {
        this.uuid = uuid;
        this.company = company;
        this.salesPerson = salesPerson;
        this.invoiceDate = invoiceDate;
        this.invoiceAmount = invoiceAmount;
        this.invoiceItems = new HashMap<>(invoiceItems); 
    }

    public Invoice(UUID uuid, LocalDate invoiceDate) {
        this(uuid, null, null, invoiceDate, 0.0, new HashMap<>());
    }

    public UUID getUuid() {
        return uuid;
    }

    public Company getCompany() {
        return company;
    }

    public Person getSalesPerson() {
        return salesPerson;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void addToInvoice(UUID uuid, Item item) {
        invoiceItems.put(uuid, item);
    }

    public Map<UUID, Item> getInvoiceItems() {
        return new HashMap<>(invoiceItems);
    }

    public double calculateSubtotal() {
        double total = 0.0;
        for (Item item : invoiceItems.values()) {
            total += item.getRetailPrice(); 
        }
        return total;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "uuid=" + uuid +
                ", company=" + company +
                ", salesPerson=" + salesPerson +
                ", invoiceDate=" + invoiceDate +
                ", invoiceAmount=" + invoiceAmount +
                ", invoiceItems=" + invoiceItems +
                '}';
    }
}