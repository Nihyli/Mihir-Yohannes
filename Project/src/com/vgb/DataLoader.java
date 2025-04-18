/*
 * Authors: Yohannes Hailu, Mihir Lochan Maruvada
 * Date: 2025/02/21
 * Description: Reads CSV files using loadPersons(), loadCompanies() and loadItems() methods.
 */

package com.vgb;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;


public class DataLoader {

    public static List<Person> loadPersons(String filePath) {
    	/**
    	 * loads a persons CSV files and turns it into a list of class instances 
    	 */
    	List<Person> persons = new ArrayList<>();
    	
        String line = null;
        
        try (Scanner scanner = new Scanner(new File(filePath))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    String[] tokens = line.split(",");
                    UUID uuid = UUID.fromString(tokens[0]);
                    String firstName = tokens[1];
                    String lastName = tokens[2];
                    String phone = tokens[3];
                    //just in case there are multiple email's, will add to the list
                    List<String> emails = new ArrayList<>();
                    for (int i = 4; i < tokens.length; i++) {
                        if (!tokens[i].trim().isEmpty()) {
                            emails.add(tokens[i]);
                        }
                    }
                    
                    Person person = new Person(uuid, firstName, lastName, phone, emails);
                    persons.add(person);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Encountered error on line: " + line, e);
        }
        return persons;
    }
    
    
    public static List<Company> loadCompanies(String filePath) {
    	/**
    	 * loads a persons CSV files and turns it into a list of class instances 
    	 */
        List<Company> companies = new ArrayList<>();
        String line = null;
        try (Scanner scanner = new Scanner(new File(filePath))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    String[] tokens = line.split(",");
                    UUID companyUuid = UUID.fromString(tokens[0]);
                    UUID contactUuid = UUID.fromString(tokens[1]);
                    String name = tokens[2];
                    Address address = new Address(tokens[3], tokens[4], tokens[5], tokens[6]);
                    Company company = new Company(companyUuid, contactUuid, name, address);
                    companies.add(company);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Encountered error on line: " + line, e);
        }
        return companies;
    }
    
    
    public static List<Item> loadItems(String filePath) {
    	/**
    	 * loads a persons CSV files and turns it into a list of class instances 
    	 */
        List<Item> items = new ArrayList<>();
        
        String line = null;
        
        try (Scanner scanner = new Scanner(new File(filePath))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    String[] tokens = line.split(",");
                    UUID uuid = UUID.fromString(tokens[0]);
                    String type = tokens[1];
                    String name = tokens[2];

                    if ("E".equals(type)) {
                        String modelNumber = tokens[3];
                        double retailPrice = Double.parseDouble(tokens[4]);
                        Equipment equipment = new Equipment(uuid, name, modelNumber, retailPrice);
                        items.add(equipment);
                        
                    } else if ("M".equals(type)) {
                        String unit = tokens[3];
                        double costPerUnit = Double.parseDouble(tokens[4]);
                        Material material = new Material(uuid, name, unit, costPerUnit);
                        items.add(material);
                        
                    } else if ("C".equals(type)) {
                        UUID companyUuid = UUID.fromString(tokens[3]);
                        Contract contract = new Contract(uuid, name, companyUuid);
                        items.add(contract);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Encountered error on line: " + line, e);
        }
        return items;
    }
    
    /**
     * This loads all of the invoices for the inovices.csv file and compiles it to a list of invoices
     */
    
    public static List<Invoice> loadInvoices(String filePath, Map<UUID, Company> companyMap,
                                                Map<UUID, Person> personMap) {
        List<Invoice> invoices = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens.length < 4) continue;
                
                UUID invoiceUUID = UUID.fromString(tokens[0].trim());
                UUID customerUUID = UUID.fromString(tokens[1].trim());
                UUID salesPersonUUID = UUID.fromString(tokens[2].trim());
                LocalDate invoiceDate = LocalDate.parse(tokens[3].trim());
                Company customer = companyMap.get(customerUUID);
                Person salesPerson = personMap.get(salesPersonUUID);
                
                Invoice invoice = new Invoice(invoiceUUID, customer, salesPerson, invoiceDate, 0.0, new HashMap<>());
                invoices.add(invoice);
                
            }
        } catch (IOException e) {
            System.err.println("Error reading Invoices.csv: " + e.getMessage());
        }
        return invoices;
    }

    /**
     * This function processes all of the different types of items
     * To see how far it should process the file
     */
    public static void processInvoiceItems(String filePath, Map<UUID, Invoice> invoiceMap,
                                            Map<UUID, Item> itemLookup) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header line
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] tokens = line.split(",");
                
                if (tokens.length < 3) continue;
                UUID invoiceUUID = UUID.fromString(tokens[0].trim());
                UUID itemUUID = UUID.fromString(tokens[1].trim());
                Invoice invoice = invoiceMap.get(invoiceUUID);
                
                if (invoice == null) {
                    System.err.println("Invoice with UUID " + invoiceUUID + " not found.");
                    continue;
                }
                Item baseItem = itemLookup.get(itemUUID);
                if (baseItem == null) {
                    System.err.println("Item with UUID " + itemUUID + " not found.");
                    continue;
                }
                
                Item invoiceItem = null;
                
                if (baseItem instanceof Equipment) {
                    Equipment equip = (Equipment) baseItem;
                    String indicator = tokens[2].trim();
                    
                    if (indicator.equals("P")) {
                        invoiceItem = equip;
                        
                    } else if (indicator.equals("L") && tokens.length >= 5) { // Lease 
                        LocalDate startDate = LocalDate.parse(tokens[3].trim());
                        LocalDate endDate = LocalDate.parse(tokens[4].trim());
                        invoiceItem = new Lease(equip.getItemUUID(), equip.getItemName(), equip.getModelNumber(),
                                equip.getRetailPrice(), startDate, endDate);
                        
                    } else if (indicator.equals("R") && tokens.length >= 4) { // Rental 
                        double hoursRented = Double.parseDouble(tokens[3].trim());
                        invoiceItem = new Rental(equip.getItemUUID(), equip.getItemName(), equip.getModelNumber(),
                                equip.getRetailPrice(), hoursRented);
                        
                    } else {
                        System.err.println("Unrecognized equipment indicator for item " + itemUUID);
                    }
                    
                } else if (baseItem instanceof Material) { // Material handling
                    Material baseMat = (Material) baseItem;
                    int quantity = Integer.parseInt(tokens[2].trim());
                    invoiceItem = new Material(baseMat.getItemUUID(), baseMat.getItemName(),
                            baseMat.getItemUnit(), baseMat.getCostPerUnit(), quantity);
                    
                } else if (baseItem instanceof Contract) { // Contract handling
                    Contract baseCon = (Contract) baseItem;
                    double contractAmount = Double.parseDouble(tokens[2].trim());
                    invoiceItem = new Contract(baseCon.getItemUUID(), baseCon.getItemName(),
                            baseCon.getCompanyUUID(), contractAmount);
                }
                
                if (invoiceItem != null) {
                    invoice.addToInvoice(invoiceItem.getItemUUID(), invoiceItem);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading InvoiceItems.csv: " + e.getMessage());
        }
    }
    
}
