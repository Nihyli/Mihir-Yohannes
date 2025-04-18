package com.vgb; 

import java.time.temporal.ChronoUnit;
import java.util.*;

public class InvoiceReport {

    public static void main(String[] args) {
        // Load base data
        List<Company> companies = DataLoader.loadCompanies("data/Companies.csv");
        List<Person> persons = DataLoader.loadPersons("data/Persons.csv");
        List<Item> baseItems = DataLoader.loadItems("data/Items.csv");

        // Build lookup maps
        Map<UUID, Company> companyMap = new HashMap<>();
        for (Company comp : companies) {
            companyMap.put(comp.getCompanyUUID(), comp);
        }
        
        Map<UUID, Person> personMap = new HashMap<>();
        for (Person p : persons) {
            personMap.put(p.getPersonUUID(), p);
        }
        
        Map<UUID, Item> itemLookup = new HashMap<>();
        for (Item item : baseItems) {
            itemLookup.put(item.getItemUUID(), item);
        }

        // Load invoices and process invoice items using DataLoader
        List<Invoice> invoiceList = DataLoader.loadInvoices("data/Invoices.csv", companyMap, personMap);
        Map<UUID, Invoice> invoiceMap = new HashMap<>();
        for (Invoice inv : invoiceList) {
            invoiceMap.put(inv.getUuid(), inv);
        }
        DataLoader.processInvoiceItems("data/InvoiceItems.csv", invoiceMap, itemLookup);


        generateOverallSummaryReport(invoiceList);
        System.out.println();
        generateCustomerSummaryReport(invoiceList);
        System.out.println();
        generateDetailedInvoiceReport(invoiceList, personMap);
    }

    
	/**
	 * This function will generate the first overall summary report. By taking in the list of invoices
	 * then compiles the report and finally outputs it.
	 */
    private static void generateOverallSummaryReport(List<Invoice> invoices) {
        System.out.println("+----------------------------------------------------------------------------------------+");
        System.out.println("| Summary Report - By Total                                                              |");
        System.out.println("+----------------------------------------------------------------------------------------+");
        System.out.printf("%-40s %-30s %10s %12s %12s\n", "Invoice #", "Customer", "Num Items", "Tax", "Total");
        
        int totalItems = 0;
        double totalTax = 0.0;
        double totalAmount = 0.0;
        
        for (Invoice inv : invoices) {
            int numItems = inv.getInvoiceItems().size();
            double invTax = 0.0, invTotal = 0.0;
            
            for (Item item : inv.getInvoiceItems().values()) {
                invTax += item.getTaxes();
                invTotal += item.calculateCost();
            }
            
            totalItems += numItems;
            totalTax += invTax;
            totalAmount += invTotal;
            System.out.printf("%-40s %-30s %10d $%11.2f $%11.2f\n", inv.getUuid(), 
                    inv.getCompany() != null ? inv.getCompany().getCompanyName() : "N/A",
                    numItems, invTax, invTotal);
        }
        
        System.out.println("+----------------------------------------------------------------------------------------+");
        System.out.printf("%52s %10d $%11.2f $%11.2f\n", "", totalItems, totalTax, totalAmount);
    }

	/**
	 * This function will generate the customer summary report. By taking in the list of invoices
	 * then compiles the report and finally outputs it.
	 */
    private static void generateCustomerSummaryReport(List<Invoice> invoices) {
        // Group invoices by company
        Map<Company, List<Invoice>> customerInvoices = new TreeMap<>(new Comparator<Company>() {
            @Override
            public int compare(Company c1, Company c2) {
                return c1.getCompanyName().compareToIgnoreCase(c2.getCompanyName());
            }
        });
        
        for (Invoice inv : invoices) {
            Company cust = inv.getCompany();
            if (cust == null) continue;
            customerInvoices.putIfAbsent(cust, new ArrayList<>());
            customerInvoices.get(cust).add(inv);
        }
        
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("| Company Invoice Summary Report                                 |");
        System.out.println("+----------------------------------------------------------------+");
        System.out.printf("%-30s %10s %12s\n", "Company", "# Invoices", "Grand Total");
        
        double overallGrandTotal = 0.0;
        int overallCount = 0;
        
        for (Map.Entry<Company, List<Invoice>> entry : customerInvoices.entrySet()) {
            Company customer = entry.getKey();
            List<Invoice> invs = entry.getValue();
            double compTotal = 0.0;
            for (Invoice inv : invs) {
                for (Item item : inv.getInvoiceItems().values()) {
                    compTotal += item.calculateCost();
                }
            }
            overallGrandTotal += compTotal;
            overallCount += invs.size();
            System.out.printf("%-30s %10d $%11.2f\n", customer.getCompanyName(), invs.size(), compTotal);
        }
        
        System.out.println("+----------------------------------------------------------------+");
        System.out.printf("%-30s %10d $%11.2f\n", "", overallCount, overallGrandTotal);
    }

	/**
	 * This function will generate the detailed summary report. By taking in the list of invoices
	 * then compiles the report and finally outputs it.
	 */
 private static void generateDetailedInvoiceReport(List<Invoice> invoices, Map<UUID, Person> personMap) {
     for (Invoice invoice : invoices) {
         System.out.println("Invoice#  " + invoice.getUuid());
         System.out.println("Date      " + invoice.getInvoiceDate());
         System.out.println("Customer:");
         
         if (invoice.getCompany() != null) {
             Company company = invoice.getCompany();
             System.out.println(company.getCompanyName() + " (" + company.getCompanyUUID() + ")");
             Person contact = personMap.get(company.getContactUUID());
             
             if (contact != null) {
                 System.out.println(contact.getLastName() + ", " + contact.getFirstName() + " (" + contact.getPersonUUID() + ")");
                 System.out.println("\t" + contact.getEmails());
             }
             
             if (company.getAddress() != null) {
                 System.out.println("\t" + company.getAddress().getStreet());
                 System.out.println("\t" + company.getAddress().getCity() + " " + company.getAddress().getState() + " " + company.getAddress().getZip());
             }
         }
         System.out.println("Sales Person:");
         if (invoice.getSalesPerson() != null) {
             Person salesPerson = invoice.getSalesPerson();
             System.out.println(salesPerson.getLastName() + ", " + salesPerson.getFirstName() + " (" + salesPerson.getPersonUUID() + ")");
             System.out.println("\t" + salesPerson.getEmails());
         }
         
         int numItems = invoice.getInvoiceItems().size();
         System.out.printf("Items (%d)%48s %12s\n", numItems, "Tax", "Total");
         System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=- -=-=-=-=-=-");
         
         
         double subtotalTax = 0.0;
         double subtotalPreTax = 0.0;
         
         for (Item item : invoice.getInvoiceItems().values()) {
             if (item instanceof Equipment && !(item instanceof Lease) && !(item instanceof Rental)) {
                 Equipment equip = (Equipment) item;
                 System.out.printf("%s (Purchase) %s-%s\n", equip.getItemUUID(), equip.getItemName(), equip.getModelNumber());
                 
             } else if (item instanceof Lease) {
                 Lease lease = (Lease) item;
                 System.out.printf("%s (Lease) %s-%s\n", lease.getItemUUID(), lease.getItemName(), lease.getModelNumber());
                 long days = ChronoUnit.DAYS.between(lease.getStartDate(), lease.getEndDate()) + 1;
                 System.out.printf("\t%d days (%s -> %s)\n", days, lease.getStartDate(), lease.getEndDate());
                 
             } else if (item instanceof Rental) {
                 Rental rental = (Rental) item;
                 System.out.printf("%s (Rental) %s-%s\n", rental.getItemUUID(), rental.getItemName(), rental.getModelNumber());
                 System.out.printf("\t%.2f hours @ $%.2f/hour\n", rental.getHoursRented(), rental.getPerHourCharge());
                 
             } else if (item instanceof Material) {
                 Material material = (Material) item;
                 System.out.printf("%s (Material) %s\n", material.getItemUUID(), material.getItemName());
                 System.out.printf("\t%d @ $%.2f/%s\n", material.getQuantity(), material.getCostPerUnit(), material.getItemUnit());
                 
             } else if (item instanceof Contract) {
                 Contract con = (Contract) item;
                 System.out.printf("%s (Contract) %s\n", con.getItemUUID(), con.getItemName());
             }
             
             double tax = item.getTaxes();
             double preTax = item.preTaxCost();
             double displayTotal; 
             double accumulationPreTax;
             
             if (item instanceof Material || item instanceof Contract) {
                 displayTotal = preTax;
                 accumulationPreTax = preTax;
                 
             } else if (item instanceof Equipment && !(item instanceof Lease) && !(item instanceof Rental)) {
                 displayTotal = item.calculateCost() - tax;
                 accumulationPreTax = item.calculateCost() - tax;
                 
             } else {
                 displayTotal = preTax;
                 accumulationPreTax = preTax;
             }
             
             subtotalTax += tax;
             subtotalPreTax += accumulationPreTax;
             System.out.printf("%60s $%11.2f $%11.2f\n", "", tax, displayTotal);
         }
         double grandTotal = subtotalTax + subtotalPreTax;
         System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=- -=-=-=-=-=-");
         System.out.printf("%60s Subtotals $%11.2f $%11.2f\n", "", subtotalTax, subtotalPreTax);
         System.out.printf("%60s Grand Total             $%11.2f\n", "", grandTotal);
         System.out.println();
     }
 }
}
