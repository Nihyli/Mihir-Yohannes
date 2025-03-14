package com.vgb;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JUnit test suite for VGB invoice system.
 */
public class EntityTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Creates an instance of a piece of equipment and tests if
	 * its cost and tax calculations are correct.
	 */
	@Test
	public void testEquipment() {

		//data values
		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;

		Equipment eq = new Equipment(uuid, name, model, cost);

		//2. Establish the expected cost and tax (rounded to nearest cent)
		double expectedCost = 95125.00;
		double expectedTax = 4994.06;

		//3. TODO: Invoke methods to determine the cost/tax:
		double actualCost = eq.preTaxCost();
		double actualTax = eq.getTaxes();

		//4. Use assertEquals with the TOLERANCE to compare:
		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		//ensure that the string representation contains necessary elements
		assertTrue(eq.toString().contains("Backhoe 3000"));
		assertTrue(eq.toString().contains("BH30X2"));
		assertTrue(eq.toString().contains("95125.00"));

	}

	@Test
	public void testLease() {
		UUID uuid = UUID.randomUUID();
		String name = "Fronthoe";
		String model = "FH30X2";
		double cost = 2000;
		LocalDate startDate = LocalDate.parse("2025-03-01");
		LocalDate endDate = LocalDate.parse("2025-03-06");
		
	    Lease leas = new Lease(uuid, name, model, cost, startDate, endDate);

		//2. Establish the expected cost and tax (rounded to nearest cent)
		double expectedCost = 9.86;
		double expectedTax = 0;

		//3. Invoke methods to determine the cost/tax:
		double actualCost = leas.preTaxCost();
		double actualTax = leas.getTaxes();

		//4. Use assertEquals with the TOLERANCE to compare:
		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		//ensure that the string representation contains necessary elements
		assertTrue(leas.toString().contains("Fronthoe"));
		assertTrue(leas.toString().contains("2025-03-01")); //Start Date
		assertTrue(leas.toString().contains("2025-03-06")); //End Date
		assertTrue(leas.toString().contains("2000.00"));
		
	}

	@Test
	public void testRental() {
		UUID uuid = UUID.randomUUID();
		String name = "Tractor";
		String model = "TR30X2";
		double retail = 30000, hoursRented = 48; 
		
		Rental rent = new Rental(uuid, name, model, retail, hoursRented);

		//2. Establish the expected cost and tax (rounded to nearest cent)
		double expectedCost = 1440;
		double expectedTax = 63.07;
		double expectedTotalCost = 1503.07; 
		
		//3. Invoke methods to determine the cost/tax:
		double actualCost = rent.preTaxCost();
		double actualTax = rent.getTaxes();
		double actualTotalCost = rent.calculateCost();

		//4. Use assertEquals with the TOLERANCE to compare:
		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotalCost, actualTotalCost, TOLERANCE);
		//ensure that the string representation contains necessary elements
		assertTrue(rent.toString().contains("Tractor"));
		assertTrue(rent.toString().contains("1503.07"));
		
	}

	@Test
	public void testMaterial() {
		UUID uuid = UUID.randomUUID();
		String name = "Screws";
		String unit = "per box";
		double costPerUnit = 10;
		int quantity = 30;
		
		Material mat = new Material(uuid, name, unit, costPerUnit, quantity);

		//2. Establish the expected cost and tax (rounded to nearest cent)
		double expectedCost = 300;
		double expectedTax = 21.45;
		double expectedTotalCost = 321.45;
		
		//3. Invoke methods to determine the cost/tax:
		double actualCost = mat.preTaxCost();
		double actualTax = mat.getTaxes();
		double actualTotalCost = mat.calculateCost();

		//4. Use assertEquals with the TOLERANCE to compare:
		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotalCost, actualTotalCost, TOLERANCE);
		//ensure that the string representation contains necessary elements
		assertTrue(mat.toString().contains("Screws"));
		assertTrue(mat.toString().contains("321.45"));
	}


	@Test
	public void testContract() {
	    // 1. Create a Contract object
	    UUID uuid = UUID.randomUUID();
	    String name = "Foundation Pour";
	    UUID companyUUID = UUID.randomUUID();
	    double contractCost = 10500.0;

	    Contract contract = new Contract(uuid, name, companyUUID, contractCost);

	    // 2. Establish the expected cost and tax (rounded to nearest cent)
	    double expectedPreTaxCost = 10500.0; // Pre-tax cost (same as contract cost)
	    double expectedTax = 0; // Contracts have no tax
	    double expectedTotalCost = 10500.0; // Total cost (same as contract cost)

	    // 3. Invoke methods to determine the cost/tax
	    double actualPreTaxCost = contract.preTaxCost();
	    double actualTax = contract.getTaxes();
	    double actualTotalCost = contract.calculateCost();

	    // 4. Use assertEquals with the TOLERANCE to compare
	    assertEquals(expectedPreTaxCost, actualPreTaxCost, TOLERANCE);
	    assertEquals(expectedTax, actualTax, TOLERANCE);
	    assertEquals(expectedTotalCost, actualTotalCost, TOLERANCE);

	    // 5. Ensure that the string representation contains necessary elements
	    assertTrue(contract.toString().contains("Foundation Pour")); // Check for the contract name
	    assertTrue(contract.toString().contains("10500.00")); // Check for the total cost
	}




}