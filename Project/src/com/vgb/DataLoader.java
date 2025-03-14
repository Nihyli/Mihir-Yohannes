/*
 * Authors: Yohannes Hailu, Mihir Lochan Maruvada
 * Date: 2025/02/21
 * Description: Reads CSV files using loadPersons(), loadCompanies() and loadItems() methods.   
 */

package com.vgb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class DataLoader{

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
    
}