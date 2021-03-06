package com.basiscomponents.bc;

import static com.basiscomponents.constants.TestDataBaseConstants.CON_TO_FILTER_SCOPE_DB;
import static com.basiscomponents.constants.TestDataBaseConstants.USERNAME_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.basiscomponents.db.DataRow;
import com.basiscomponents.db.ResultSet;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SqlTableBCH2ScopeFieldSelectionTest {

	private SqlTableBC tableBC;
	private HashMap<String, ArrayList<String>> scopes;
	private ResultSet rs;
	private static Connection conToFilterScope;

	/**
	 * Loading the h2-Driver and creating the test databases.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@BeforeAll
	public static void initialize()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver").newInstance();
		H2DataBaseProvider.createTestDataBaseForFilteringScoping();
		conToFilterScope = DriverManager.getConnection(CON_TO_FILTER_SCOPE_DB, USERNAME_PASSWORD, USERNAME_PASSWORD);
	}

	/**
	 * The getter and setter of the scopes and FieldSelection are tested here.
	 * 
	 */
	@Test
	public void sqlTableBCScopeFieldSelectionGetNSet() {

		tableBC = new SqlTableBC("");
		scopes = new HashMap<>();
		ArrayList<String> scopeD = new ArrayList<>();
		scopeD.add("FIELD_1");
		scopes.put("D", scopeD);

		// Testing setters
		tableBC.setScopeDef(scopes);
		tableBC.setScope("D");


		// Testing getters
		assertEquals("D", tableBC.getScope());
		assertEquals(scopes, tableBC.getScopeDef());
		
		try {
			DataRow dr = new DataRow();
			dr.setFieldValue("NAME", "");
			dr.setFieldValue("CUSTOMERID", "");

			// Testing setter
			tableBC.setFieldSelection(dr);
			
			// Testing getter
			assertEquals(dr.getFieldNames(), tableBC.getFieldSelection().getFieldNames());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Two scopes are created and inserted into the SqlTableBC. Afterwards the test
	 * checks if the scopes are saved correctly.
	 * 
	 */
	@Test
	public void sqlTableBCScopeIsPresent() {

		tableBC = new SqlTableBC("");
		scopes = new HashMap<>();
		ArrayList<String> scopeD = new ArrayList<>();
		ArrayList<String> scopeF = new ArrayList<>();
		scopeD.add("FIELD_1");
		scopeD.add("FIELD_2");
		scopeD.add("FIELD_3");
		scopeD.add("FIELD_4");
		scopeF.add("FIELD_1");
		scopeF.add("FIELD_2");
		scopeF.add("FIELD_3");
		scopeF.add("FIELD_4");
		scopeF.add("FIELD_5");
		scopeF.add("FIELD_6");

		scopes.put("D", scopeD);
		scopes.put("F", scopeF);
		tableBC.setScopeDef(scopes);

		tableBC.setScope("D");

		assertTrue(tableBC.isFieldInScope("FIELD_1"));
		assertTrue(tableBC.isFieldInScope("FIELD_2"));
		assertTrue(tableBC.isFieldInScope("FIELD_3"));
		assertTrue(tableBC.isFieldInScope("FIELD_4"));
		assertFalse(tableBC.isFieldInScope("FIELD_5"));
		assertFalse(tableBC.isFieldInScope("FIELD_6"));
		assertFalse(tableBC.isFieldInScope("FIELD_7"));
		assertFalse(tableBC.isFieldInScope(null));

		tableBC.setScope("F");

		assertTrue(tableBC.isFieldInScope("FIELD_1"));
		assertTrue(tableBC.isFieldInScope("FIELD_2"));
		assertTrue(tableBC.isFieldInScope("FIELD_3"));
		assertTrue(tableBC.isFieldInScope("FIELD_4"));
		assertTrue(tableBC.isFieldInScope("FIELD_5"));
		assertTrue(tableBC.isFieldInScope("FIELD_6"));
		assertFalse(tableBC.isFieldInScope("FIELD_7"));
		assertFalse(tableBC.isFieldInScope(null));
	}

	/**
	 * Creates a SqlTableBC with a connection to a h2-DataBase. The active table is
	 * switched to CUSTOMERS and its values are queried with a scope.
	 * 
	 * @throws Exception
	 */
	@Test
	public void sqlTableBCScopeSimpleTest() throws Exception {

		// Set table
		tableBC = new SqlTableBC(conToFilterScope);
		tableBC.setTable("CUSTOMERS");

		// Setting up the scope
		scopes = new HashMap<>();
		ArrayList<String> myScope = new ArrayList<>();
		myScope.add("NAME");
		myScope.add("CUSTOMERID");
		scopes.put("A", myScope);
		tableBC.setScopeDef(scopes);
		tableBC.setScope("A");

		rs = tableBC.retrieve();

		// Checking the ColumnNames and results
		assertTrue(rs.getColumnCount() == 2);
		assertTrue(rs.getColumnNames().contains("NAME"));
		assertTrue(rs.getColumnNames().contains("CUSTOMERID"));

		assertEquals("Freeman", rs.get(0).getFieldValue("NAME"));
		assertEquals(1, rs.get(0).getFieldValue("CUSTOMERID"));
	}

	/**
	 * Creates a SqlTableBC with a connection to a h2-DataBase. The active table is
	 * switched to CUSTOMERS and its values are queried with multiple scopes.
	 * 
	 * @throws Exception
	 */
	@Test
	public void sqlTableBCMultipleScopeTest() throws Exception {

		// Set table
		tableBC = new SqlTableBC(conToFilterScope);
		tableBC.setTable("CUSTOMERS");

		// Setting up the scope
		scopes = new HashMap<>();
		ArrayList<String> A = new ArrayList<>();
		ArrayList<String> B = new ArrayList<>();
		A.add("NAME");
		A.add("CUSTOMERID");
		B.add("AGE");
		scopes.put("A", A);
		scopes.put("B", B);
		tableBC.setScopeDef(scopes);
		tableBC.setScope("AB");

		rs = tableBC.retrieve();

		// Checking the ColumnNames and results
		assertTrue(rs.getColumnCount() == 3);
		assertTrue(rs.getColumnNames().contains("NAME"));
		assertTrue(rs.getColumnNames().contains("CUSTOMERID"));
		assertTrue(rs.getColumnNames().contains("AGE"));

		assertEquals("Freeman", rs.get(0).getFieldValue("NAME"));
		assertEquals(1, rs.get(0).getFieldValue("CUSTOMERID"));
		assertEquals(62, rs.get(0).getFieldValue("AGE"));
	}

	/**
	 * Creates a SqlTableBC with a connection to a h2-DataBase. The active table is
	 * switched to CUSTOMERS and its values are queried with a FieldSelection.
	 * 
	 */
	@Test
	public void sqlTableBCFieldSelectionSimpleTest() throws Exception {

		// Set table
		tableBC = new SqlTableBC(conToFilterScope);
		tableBC.setTable("CUSTOMERS");

		// Setting up the FieldSelection
		try {
			DataRow dr = new DataRow();
			dr.setFieldValue("NAME", "");
			dr.setFieldValue("CUSTOMERID", "");

			tableBC.setFieldSelection(dr);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		rs = tableBC.retrieve();

		// Checking the ColumnNames and results
		assertTrue(rs.getColumnCount() == 2);
		assertTrue(rs.getColumnNames().contains("NAME"));
		assertTrue(rs.getColumnNames().contains("CUSTOMERID"));

		assertEquals("Freeman", rs.get(0).getFieldValue("NAME"));
		assertEquals(1, rs.get(0).getFieldValue("CUSTOMERID"));
	}

	/**
	 * Creates a SqlTableBC with a connection to a h2-DataBase. The active table is
	 * switched to CUSTOMERS and its values are queried with a FieldSelection.
	 * 
	 */
//	@Test
	public void sqlTableBCFieldSelectionCollectionTest() throws Exception {

		// Set table
		tableBC = new SqlTableBC(conToFilterScope);
		tableBC.setTable("CUSTOMERS");

		// Setting up the FieldSelection
		Collection<String> collec = new ArrayList<String>();
		collec.add("NAME");
		collec.add("COUNTRY");
		collec.add("AGE");
		tableBC.setFieldSelection(collec);

		rs = tableBC.retrieve();

		// Checking the ColumnNames and results
		assertTrue(rs.getColumnCount() == 3);
		assertTrue(rs.getColumnNames().contains("NAME"));
		assertTrue(rs.getColumnNames().contains("COUNTRY"));
		assertTrue(rs.getColumnNames().contains("AGE"));

		assertEquals("Freeman", rs.get(0).getFieldValue("NAME"));
		assertEquals("USA", rs.get(0).getFieldValue("COUNTRY"));
		assertEquals(62, rs.get(0).getFieldValue("AGE"));
	}

	/**
	 * Cleans up the databases.
	 * 
	 * @throws Exception
	 */
	@AfterAll
	public static void cleanUp() throws Exception {
		conToFilterScope.close();
		H2DataBaseProvider.dropAllTestTables();
	}
}
