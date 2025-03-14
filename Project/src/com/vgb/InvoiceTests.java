package com.vgb;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * JUnit test suite for VGB invoice system.
 */
public class InvoiceTests {

	public static final double TOLERANCE = 0.001;


	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in
	 * the VGB system.
	 */
	@Test
	public void testInvoice01() {

		//1. Create test instances 3 different types of invoice items
		//   You may reuse the instances from your Entity test suites
		//2. Create an instance of your invoice and add these 3 items to it
		//3. Calculate and compare the values to the expected values.
		//data values
		
		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;

		Equipment eq = new Equipment(uuid, name, model, cost);
		
		uuid = UUID.randomUUID();
		name = "Fronthoe";
		model = "FH30X2";
		cost = 2000;
		LocalDate startDate = LocalDate.parse("2025-03-01");
		LocalDate endDate = LocalDate.parse("2025-03-06");
		
	    Lease leas = new Lease(uuid, name, model, cost, startDate, endDate);

	    uuid = UUID.randomUUID();
		name = "Tractor";
		model = "TR30X2";
		double retail = 30000, hoursRented = 48; 
		
		Rental rent = new Rental(uuid, name, model, retail, hoursRented);
	    
		Invoice invoice = new Invoice(UUID.randomUUID(), LocalDate.now());
		 
        invoice.addToInvoice(UUID.randomUUID(), eq);
        invoice.addToInvoice(UUID.randomUUID(), leas);
        invoice.addToInvoice(UUID.randomUUID(), rent);
	    
		double expectedSubtotal = eq.getRetailPrice() + leas.getRetailPrice() + rent.getRetailPrice();
		double expectedTaxTotal = expectedSubtotal * 0.10;
		double expectedGrandTotal = expectedSubtotal + expectedTaxTotal;

		//Call your invoice's methods to get these values
		double actualSubtotal = invoice.calculateSubtotal();
		double actualTaxTotal = actualSubtotal * 0.10;
		double actualGrandTotal = actualSubtotal + actualTaxTotal;

		//Use assertEquals with the TOLERANCE to compare:
		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);
		// ensure that the string representation contains necessary elements
		 String invoiceString = invoice.toString();
	     assertTrue(invoiceString.contains("Backhoe 3000"));
	     assertTrue(invoiceString.contains("Fronthoe"));
	     assertTrue(invoiceString.contains("Tractor"));
	}

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in
	 * the VGB system.
	 */
	@Test
	public void testInvoice02() {
		//1. Create test instances the other 2 types of invoice items
		//   You may reuse the instances from your Entity test suites
		//2. Create an instance of your invoice and add these 2 items to it
		//3. Calculate and compare the values to the expected values.
		
		UUID uuid = UUID.randomUUID();
		String name = "Screws";
		String unit = "per box";
		double costPerUnit = 10;
		int quantity = 30;
		
		Material mat = new Material(uuid, name, unit, costPerUnit, quantity);
		
		uuid = UUID.randomUUID();
	    name = "Foundation Pour";
	    UUID companyUUID = UUID.randomUUID();
	    double contractCost = 10500.0;

	    Contract contract = new Contract(uuid, name, companyUUID, contractCost);
	    
	    Invoice invoice = new Invoice(UUID.randomUUID(), LocalDate.now());
		 
        invoice.addToInvoice(UUID.randomUUID(), mat);
        invoice.addToInvoice(UUID.randomUUID(), contract);
	    
		double expectedSubtotal = mat.getRetailPrice() + contract.getRetailPrice();
		double expectedTaxTotal = expectedSubtotal * 0.10;
		double expectedGrandTotal = expectedSubtotal + expectedTaxTotal;

		//Call your invoice's methods to get these values
		double actualSubtotal = invoice.calculateSubtotal();
		double actualTaxTotal = actualSubtotal * 0.10;
		double actualGrandTotal = actualSubtotal + actualTaxTotal;

		//Use assertEquals with the TOLERANCE to compare:
		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);
		// ensure that the string representation contains necessary elements
		 String invoiceString = invoice.toString();
	     assertTrue(invoiceString.contains("Screws"));
	     assertTrue(invoiceString.contains("Foundation Pour"));
	}



}