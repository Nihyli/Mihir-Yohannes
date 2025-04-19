package com.vgb; 

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class InvoiceReport {

    public static void main(String[] args) {
        // Load base data
        List<Company> companies = DatabaseLoader.loadCompanies("data/Companies.csv");
        List<Person> persons = DatabaseLoader.loadPersons("data/Persons.csv");
        List<Item> baseItems = DatabaseLoader.loadItems("data/Items.csv");

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

        // Load invoices and process invoice items using DatabaseLoader
        List<Invoice> invoiceList = DatabaseLoader.loadInvoices("data/Invoices.csv", companyMap, personMap);
        Map<UUID, Invoice> invoiceMap = new HashMap<>();
        for (Invoice inv : invoiceList) {
            invoiceMap.put(inv.getUuid(), inv);
        }
        DatabaseLoader.processInvoiceItems("data/InvoiceItems.csv", invoiceMap, itemLookup);


        generateOverallSummaryReport(invoiceList);
        System.out.println();
        generateCustomerSummaryReport(invoiceList, companies);
        System.out.println();
        generateDetailedInvoiceReport(invoiceList, personMap);
    }

    
	/**
	 * This function will generate the first overall summary report. By taking in the list of invoices
	 * then compiles the report and finally outputs it.
	 */
    private static void generateOverallSummaryReport(List<Invoice> invoices) {
    	
        Collections.sort(invoices, new Comparator<Invoice>() {
            @Override
            public int compare(Invoice i1, Invoice i2) {
                double sum1 = 0.0;
                for (Item item : i1.getInvoiceItems().values()) {
                    sum1 += item.calculateCost();
                }
                double sum2 = 0.0;
                for (Item item : i2.getInvoiceItems().values()) {
                    sum2 += item.calculateCost();
                }
                return Double.compare(sum2, sum1);
            }});

        System.out.println("+----------------------------------------------------------------------------------------+");
        System.out.println("| Summary Report - By Total                                                              |");
        System.out.println("+----------------------------------------------------------------------------------------+");
        System.out.printf("%-36s %-25s %10s %12s %12s\n",
            "Invoice #", "Customer", "Num Items", "Tax", "Total");

        int totalItems = 0;
        double totalTax = 0.0;
        double totalAmount = 0.0;
        
        for (Invoice inv : invoices) {
            System.out.println(inv.formatSummaryLine());
            
            totalItems += inv.getInvoiceItems().size();
            totalTax += inv.getTotalTax();
            totalAmount += inv.getTotalAmount();
        }
        System.out.println("+----------------------------------------------------------------------------------------+");
        System.out.printf("%62s %10d $%11.2f $%11.2f\n", "", totalItems, totalTax, totalAmount);
    }

	/**
	 * This function will generate the customer summary report. By taking in the list of invoices
	 * then compiles the report and finally outputs it.
	 */
    private static void generateCustomerSummaryReport(List<Invoice> invoices, List<Company> allCompanies) {
        Map<Company,List<Invoice>> customerInvoices = new HashMap<>();
        for (Company c : allCompanies) {
            customerInvoices.put(c, new ArrayList<Invoice>());
        }

        for (Invoice inv : invoices) {
            Company cust = inv.getCompany();
            if (cust != null) {
                customerInvoices.get(cust).add(inv);
            }
        }
        
        List<Company> companies = new ArrayList<Company>(customerInvoices.keySet());
        Collections.sort(companies, new Comparator<Company>() {
            @Override
            public int compare(Company c1, Company c2) {
                return c1.getCompanyName().compareToIgnoreCase(c2.getCompanyName());
            }
        });

        System.out.println("+----------------------------------------------------------------+");
        System.out.println("| Company Invoice Summary Report                                 |");
        System.out.println("+----------------------------------------------------------------+");
        System.out.printf("%-30s %10s %12s\n",
            "Company", "# Invoices", "Grand Total"
        );

        int overallCount = 0;
        double overallGrandTotal = 0.0;

        for (Company customer : companies) {
            List<Invoice> invs = customerInvoices.get(customer);
            int count = invs.size();
            double compTotal = 0.0;

            for (Invoice inv : invs) {
                for (Item item : inv.getInvoiceItems().values()) {
                    compTotal += item.calculateCost();
                }
            }

            overallCount += count;
            overallGrandTotal += compTotal;

            System.out.printf("%-30s %10d $%11.2f\n", customer.getCompanyName(), count, compTotal);
        }

        System.out.println("+----------------------------------------------------------------+");
        System.out.printf("%-30s %10d $%11.2f\n", "", overallCount, overallGrandTotal);
    }
      
	/**
	 * This function will generate the detailed summary report. By taking in the list of invoices
	 * then compiles the report and finally outputs it.
	 */
	 private static void generateDetailedInvoiceReport(List<Invoice> invoices, Map<UUID, Person> personMap) {
		 Collections.sort(invoices, new Comparator<Invoice>() {
		      @Override
		      public int compare(Invoice i1, Invoice i2) {
		          return i1.getInvoiceDate().compareTo(i2.getInvoiceDate());
		        }
		    });
		 
		    for (Invoice inv : invoices) {
		        inv.printDetailedReport(personMap);
		    }
		}
}
