/*
 * Authors: Yohannes Hailu, Mihir Lochan Maruvada
 * Date: 2025/02/21
 * Description: 
 * The DataConverter class loads data from CSV files using DataLoader, 
 * converts it into Java objects, and serializes the data into both JSON and XML formats. 
 * The output files are saved in the "data" directory.
 */
package com.vgb;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DataConverter {

	public static void convertPeopleToJson(List<Person> peopleList) {
		/**
		 * This takes in a list of people instances
		 * and converts the list into a JSON file
		 */
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    String jsonOutput = gson.toJson(peopleList);
	    try (FileWriter writer = new FileWriter("data/Persons.json")) {
	        writer.write(jsonOutput);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void convertCompanyToJson(List<Company> companyList) {
		/**
		 * This takes in a list of Company instances
		 * and converts the list into a JSON file
		 */
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    String jsonOutput = gson.toJson(companyList);
	    try (FileWriter writer = new FileWriter("data/Companies.json")) {
	        writer.write(jsonOutput);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void convertItemsToJson(List<Item> itemList) {
		/**
		 * This takes in a list of Items instances
		 * and converts the list into a JSON file
		 */
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    String jsonOutput = gson.toJson(itemList);
	    try (FileWriter writer = new FileWriter("data/Items.json")) {
	        writer.write(jsonOutput);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void convertPeopleToXML(List<Person> peopleList) {
		/**
		 * This takes in a list of people instances
		 * and converts the list into a XML file
		 */
		XStream xstream = new XStream(new DomDriver());
	    String xmlOutput = xstream.toXML(peopleList);
	    try (FileWriter writer = new FileWriter("data/Persons.xml")) {
	        writer.write(xmlOutput);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void convertCompanyToXML(List<Company> companyList) {
		/**
		 * This takes in a list of Company instances
		 * and converts the list into a XML file
		 */
		XStream xstream = new XStream(new DomDriver());
	    String xmlOutput = xstream.toXML(companyList);
	    try (FileWriter writer = new FileWriter("data/Companies.xml")) {
	        writer.write(xmlOutput);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void convertItemsToXML(List<Item> itemList) {
		/**
		 * This takes in a list of I instances
		 * and converts the list into a XML file
		 */
		XStream xstream = new XStream(new DomDriver());
	    String xmlOutput = xstream.toXML(itemList);
	    try (FileWriter writer = new FileWriter("data/items.xml")) {
	        writer.write(xmlOutput);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void main(String args[]) {
		List<Person> peopleList = DataLoader.loadPersons("data/Persons.csv");
		List<Company> companyList = DataLoader.loadCompanies("data/Companies.csv");
		List<Item> itemList = DataLoader.loadItems("data/Items.csv");
		
		convertPeopleToJson(peopleList);
		convertCompanyToJson(companyList);
		convertItemsToJson(itemList);
		convertPeopleToXML(peopleList);
		convertCompanyToXML(companyList);
		convertItemsToXML(itemList);

		
	}
}