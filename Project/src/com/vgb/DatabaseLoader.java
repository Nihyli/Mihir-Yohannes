package com.vgb;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;


public class DatabaseLoader {
    
    private static final Logger LOGGER = LogManager.getLogger(DatabaseLoader.class);
    
    static {
        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.DEBUG);
        LOGGER.info("DatabaseLoader initialized");
    }
    
    private static String itemType; // 'E', 'M', or 'C'

    
    /**
     * Loads all items from the Item table, joining subcontractor info as needed.
     * Creates Equipment, Material, or Contract objects based on the itemType code.
     *
     *
     */
    public static List<Item> loadItems(String filePath) {
        LOGGER.debug("Entering loadItems with filePath={}", filePath);
        List<Item> items = new ArrayList<>();
        String itemQuery = "SELECT itemUuid, itemName, itemPrice, itemType, modelNumber, " +
                           "costPerUnit, itemUnit, companyUuid FROM Item " +
                           "LEFT JOIN Company ON Item.subcontractorId = Company.companyId;";
        LOGGER.debug("Item query: {}", itemQuery);
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(itemQuery);
             ResultSet rs = ps.executeQuery()) {
            
            LOGGER.debug("Executed item query, iterating result set");
            while (rs.next()) {
                String itemType = rs.getString("itemType");
                LOGGER.debug("Result row: itemType={}", itemType);
                UUID uuid = UUID.fromString(rs.getString("itemUuid"));
                String name = rs.getString("itemName");
                LOGGER.debug("Result row: uuid={}, name={}", uuid, name);
                
                if ("E".equals(itemType)) {
                    Equipment equipment = new Equipment(
                        uuid, 
                        name, 
                        rs.getString("modelNumber"), 
                        rs.getDouble("itemPrice")
                    );
                    LOGGER.debug("Created Equipment: {}", equipment);
                    items.add(equipment);
                    
                } else if ("M".equals(itemType)) {
                    Material material = new Material(
                        uuid, 
                        name, 
                        rs.getString("itemUnit"), 
                        rs.getDouble("costPerUnit")
                    );
                    LOGGER.debug("Created Material: {}", material);
                    items.add(material);
                    
                } else if ("C".equals(itemType)) {
                    Contract contract = new Contract(
                        uuid, 
                        name, 
                        UUID.fromString(rs.getString("companyUuid"))
                    );
                    LOGGER.debug("Created Contract: {}", contract);
                    items.add(contract);
                } else {
                    LOGGER.warn("Unknown itemType '{}' for UUID {}", itemType, uuid);
                }
            }
            
        } catch(Exception e) {
            LOGGER.error("Error loading items from database", e);
            e.printStackTrace();
            throw new RuntimeException("Error loading items from database", e);
        }
        
        LOGGER.debug("loadItems returning {} items", items.size());
        return items;
    }
    
    /**
     * Loads person records and their email addresses(can be multiple emails per person) in a one to many relationship. 
     */
    public static List<Person> loadPersons(String filePath) {
        LOGGER.debug("Entering loadPersons with filePath={}", filePath);
        Map<UUID, Person> persons = new HashMap<>();
        String personQuery = "SELECT Person.personUuid, Person.firstName, Person.lastName, " +
                             "Person.phoneNumber, Email.emailAddress " +
                             "FROM Person JOIN Email ON Person.personId = Email.personId";
        LOGGER.debug("Person query: {}", personQuery);

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(personQuery);
             ResultSet rs = ps.executeQuery()) {

            LOGGER.debug("Executed person query, iterating result set");
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("personUuid"));
                String firstName = rs.getString("firstName");
                String lastName  = rs.getString("lastName");
                String phone     = rs.getString("phoneNumber");
                String email     = rs.getString("emailAddress");
                LOGGER.debug("Row: uuid={}, firstName={}, lastName={}, email={}", uuid, firstName, lastName, email);

                if (persons.containsKey(uuid)) {
                    LOGGER.debug("Adding email to existing Person {}", uuid);
                    persons.get(uuid).getEmails().add(email);
                } else {
                    List<String> emails = new ArrayList<>();
                    emails.add(email);
                    Person person = new Person(uuid, firstName, lastName, phone, emails);
                    LOGGER.debug("Created new Person: {}", person);
                    persons.put(uuid, person);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to load persons from DB", e);
            throw new RuntimeException("Failed to load persons from DB", e);
        }

        List<Person> result = new ArrayList<>(persons.values());
        LOGGER.debug("loadPersons returning {} persons", result.size());
        return result;
    }


    /**
     * Loads companies along with their address and contact person.
     */
    public static List<Company> loadCompanies(String filePath) {
        LOGGER.debug("Entering loadCompanies with filePath={}", filePath);
        Map<UUID, Company> companies = new HashMap<>();
        String companyQuery = "SELECT Address.street, Address.city, Zip.zipCode, State.stateName, " +
                              "Company.companyUuid, Person.personUuid, Company.companyName " +
                              "FROM Company " +
                              "JOIN Person ON Company.contactId = Person.personId " +
                              "JOIN Address ON Company.addressId = Address.addressId " +
                              "JOIN Zip ON Address.zipId = Zip.zipId " +
                              "JOIN State ON Address.stateId = State.stateId;";
        LOGGER.debug("Company query: {}", companyQuery);

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(companyQuery);
             ResultSet rs = ps.executeQuery()) {

            LOGGER.debug("Executed company query, iterating result set");
            while (rs.next()) {
                String street    = rs.getString("street");
                String city      = rs.getString("city");
                String zipCode   = rs.getString("zipCode");
                String stateName = rs.getString("stateName");
                UUID companyUuid = UUID.fromString(rs.getString("companyUuid"));
                UUID personUuid  = UUID.fromString(rs.getString("personUuid"));
                String companyName = rs.getString("companyName");
                
                LOGGER.debug("Row: companyUuid={}, name={}, contactUuid={}", companyUuid, companyName, personUuid);
                Address address = new Address(street, city, zipCode, stateName);
                Company company = new Company(companyUuid, personUuid, companyName, address);
                companies.put(companyUuid, company);
                LOGGER.debug("Created Company: {}", company);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to load companies from DB", e);
            throw new RuntimeException("Failed to load companies from DB", e);
        }

        List<Company> result = new ArrayList<>(companies.values());
        LOGGER.debug("loadCompanies returning {} companies", result.size());
        return result;
    }

    /**
     * Loads invoices and associates them with their customer Company and
     * salesperson Person.
     *
     */
    public static List<Invoice> loadInvoices(String filePath, Map<UUID, Company> companies, Map<UUID, Person> salesPeople) {
        LOGGER.debug("Entering loadInvoices with filePath={}", filePath);
        Map<UUID, Invoice> invoiceMap = new HashMap<>();
        String query = "SELECT i.invoiceUuid, i.invoiceDate, " +
                       "c.companyUuid as customerUuid, c.companyName, " +
                       "p.personUuid as salesPersonUuid, p.firstName, p.lastName " +
                       "FROM Invoice i " +
                       "LEFT JOIN Company c ON i.customerId = c.companyId " +
                       "LEFT JOIN Person p ON i.salesPersonId = p.personId";
        LOGGER.debug("Invoice query: {}", query);

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            LOGGER.debug("Executed invoice query, iterating result set");
            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("invoiceDate"));
                UUID customerUuid = rs.getString("customerUuid") != null
                    ? UUID.fromString(rs.getString("customerUuid")) : null;
                UUID salesPersonUuid = UUID.fromString(rs.getString("salesPersonUuid"));
                UUID invoiceUUID = UUID.fromString(rs.getString("invoiceUuid"));
                
                LOGGER.debug("Row: invoiceUuid={}, date={}, customerUuid={}, salesPersonUuid={}",
                             invoiceUUID, date, customerUuid, salesPersonUuid);

                Company company = customerUuid != null ? companies.get(customerUuid) : null;
                Person salesPerson = salesPeople.get(salesPersonUuid);
                Invoice invoice = new Invoice(invoiceUUID, company, salesPerson, date, 0.0, new HashMap<>());
                invoiceMap.put(invoiceUUID, invoice);
                LOGGER.debug("Created Invoice: {}", invoice);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to load Invoices from DB", e);
            throw new RuntimeException("Failed to load Invoices from DB", e);
        }

        List<Invoice> result = new ArrayList<>(invoiceMap.values());
        LOGGER.debug("loadInvoices returning {} invoices", result.size());
        return result;
    }
    
    /**
     * Processes line items for each invoice, joining invoiceItem and Item
     * tables. Depending on the type, creates Rental, Lease, Material, or Contract
     * instances and attaches them to the parent Invoice.
     *
     * */
    public static void processInvoiceItems(String filePath, Map<UUID, Invoice> invoiceMap,
                                           Map<UUID, Item> itemLookup) {
        LOGGER.debug("Entering processInvoiceItems with filePath={}", filePath);
        String companyQuery = "SELECT ii.*, i.invoiceUuid, " +
                              "it.itemUuid, it.itemType, it.itemName, " +
                              "it.modelNumber, it.itemPrice, it.costPerUnit, it.itemUnit " +
                              "FROM invoiceItem ii " +
                              "JOIN Invoice i ON ii.invoiceId = i.invoiceId " +
                              "JOIN Item it ON ii.itemId = it.itemId";
        LOGGER.debug("Invoice-items query: {}", companyQuery);

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(companyQuery);
             ResultSet rs = ps.executeQuery()) {

            LOGGER.debug("Executed invoice-items query, iterating result set");
            while (rs.next()) {
                UUID invoiceUUID = UUID.fromString(rs.getString("invoiceUuid"));
                UUID itemUUID    = UUID.fromString(rs.getString("itemUuid"));
                LOGGER.debug("Row: invoiceUuid={}, itemUuid={}", invoiceUUID, itemUUID);

                Invoice invoice = invoiceMap.get(invoiceUUID);
                Item baseItem   = itemLookup.get(itemUUID);
                if (baseItem == null) {
                    LOGGER.warn("Item with UUID {} not found in lookup", itemUUID);
                    continue;
                }

                Item invoiceItem = null;
                if (baseItem instanceof Equipment) {
                    Equipment equip = (Equipment) baseItem;
                    invoiceItem = equip;
                    if (rs.getObject("hoursRented") != null) {
                        double hoursRented = rs.getDouble("hoursRented");
                        double perHourCharge = rs.getDouble("perHourCharge");
                        invoiceItem = new Rental(equip.getItemUUID(), equip.getItemName(),
                                                 equip.getModelNumber(), perHourCharge, hoursRented);
                        LOGGER.debug("Created Rental: {}", invoiceItem);
                    } else if (rs.getObject("startDate") != null && rs.getObject("endDate") != null) {
                        LocalDate startDate = rs.getDate("startDate").toLocalDate();
                        LocalDate endDate   = rs.getDate("endDate").toLocalDate();
                        invoiceItem = new Lease(equip.getItemUUID(), equip.getItemName(),
                                                equip.getModelNumber(), equip.getRetailPrice(),
                                                startDate, endDate);
                        LOGGER.debug("Created Lease: {}", invoiceItem);
                    }
                } else if (baseItem instanceof Material) {
                    Material baseMat = (Material) baseItem;
                    int quantity = rs.getObject("quantity") != null ? rs.getInt("quantity") : 0;
                    invoiceItem = new Material(baseMat.getItemUUID(),
                                               baseMat.getItemName(),
                                               baseMat.getItemUnit(),
                                               baseMat.getCostPerUnit(),
                                               quantity);
                    LOGGER.debug("Created Material invoice item: {}", invoiceItem);
                } else if (baseItem instanceof Contract) {
                    Contract baseCon = (Contract) baseItem;
                    double contractAmount = rs.getInt("contractCost");
                    invoiceItem = new Contract(baseCon.getItemUUID(),
                                               baseCon.getItemName(),
                                               baseCon.getCompanyUUID(),
                                               contractAmount);
                    LOGGER.debug("Created Contract invoice item: {}", invoiceItem);
                }

                if (invoiceItem != null) {
                    invoice.addToInvoice(invoiceItem.getItemUUID(), invoiceItem);
                    LOGGER.debug("Added item {} to Invoice {}", invoiceItem.getItemUUID(), invoiceUUID);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to process invoice items", e);
            throw new RuntimeException("Failed to load invoiceItems from DB", e);
        }
        LOGGER.debug("Completed processInvoiceItems");
    }
}
