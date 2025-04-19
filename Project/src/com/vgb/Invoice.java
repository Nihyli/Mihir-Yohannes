package com.vgb;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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
    
    // Formating for invoice reports
    
    /** 
     * Render the one‐line summary that used to live in InvoiceReport. 
     * (Invoice #, Customer, NumItems, Tax, Total) 
     */
    public String formatSummaryLine() {
        int    numItems = invoiceItems.size();
        double tax      = getTotalTax();
        double total    = getTotalAmount();
        return String.format(
            "%-36s %-25s %10d $%11.2f $%11.2f",
            uuid,
            company != null ? company.getCompanyName() : "N/A",
            numItems,
            tax,
            total
        );
    }
    
    /**
     * Print the detailed section for this invoice (header, items, subtotals).
     * Leaves out the “loop over invoices” logic.
     */
    public void printDetailedReport(Map<UUID, Person> personMap) {
        System.out.println("Invoice#  " + this.getUuid());
        System.out.println("Date      " + this.getInvoiceDate());
        System.out.println("Customer:");
        Company company = this.getCompany();
        
        if (company != null) {
            System.out.println(
                company.getCompanyName() + " (" + company.getCompanyUUID() + ")"
            );
            Person contact = personMap.get(company.getContactUUID());
            if (contact != null) {
                System.out.println(
                    contact.getLastName() + ", " + contact.getFirstName()
                    + " (" + contact.getPersonUUID() + ")"
                );
                System.out.println("\t" + contact.getEmails());
            }
            Address addr = company.getAddress();
            if (addr != null) {
                System.out.println("\t" + addr.getStreet());
                System.out.println("\t" + addr.getCity() + " "+ addr.getState() + " "+ addr.getZip());
            }
        }

        System.out.println("Sales Person:");
        Person sales = this.getSalesPerson();
        if (sales != null) {
            System.out.println(sales.getLastName() + ", " + sales.getFirstName() + " (" + sales.getPersonUUID() + ")");
            System.out.println("\t" + sales.getEmails());
        }

        int numItems = this.getInvoiceItems().size();
        System.out.printf("Items (%d)%48s %12s\n", numItems, "Tax", "Total");
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=- -=-=-=-=-=-");

        double subtotalTax = 0.0;
        double subtotalPreTax = 0.0;

        for (Item item : this.getInvoiceItems().values()) {
            item.printDetailedReport();

            subtotalTax += item.getTaxes();
            subtotalPreTax += item.preTaxCost();
        }

        double grandTotal = subtotalTax + subtotalPreTax;
        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=- -=-=-=-=-=-");
        System.out.printf("%50s Subtotals $%11.2f $%11.2f\n", "", subtotalTax, subtotalPreTax);
        System.out.printf("%50s Grand Total             $%11.2f\n","", grandTotal);
        System.out.println();
    }


     public double getTotalTax() {
        double t = 0;
        for (Item i : invoiceItems.values()) t += i.getTaxes();
        return t;
    }
    
     public double getTotalAmount() {
        double t = 0;
        for (Item i : invoiceItems.values()) t += i.calculateCost();
        return t;
    }
    
    @Override
    public String toString() {
        return "Invoice{" + "uuid=" + uuid + ", company=" + company + ", salesPerson=" + salesPerson + ", invoiceDate=" + invoiceDate + ", invoiceAmount=" + invoiceAmount +", invoiceItems=" + invoiceItems +'}';
    }
}