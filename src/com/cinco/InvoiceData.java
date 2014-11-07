package com.cinco;

import java.sql.*;

import org.joda.time.*;
import org.joda.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import cinco.sqlConnection;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	public static void disableKeys(Connection conn){
		try {
			Statement disable = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
			disable.execute("SET FOREIGN_KEY_CHECKS = 0");
			disable.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void enableKeys(Connection conn){
		try {
			Statement disable = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
			disable.execute("SET FOREIGN_KEY_CHECKS = 1");
			disable.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Method that removes every person record from the database
	 *
	 */
	public static void removeAllPersons()  {
		
		Connection conn = sqlConnection.getConnection();

		try {
			disableKeys(conn);
			Statement disable = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
			disable.execute("SET FOREIGN_KEY_CHECKS = 0");
			PreparedStatement sql1 = conn.prepareStatement("DELETE FROM Emails");
			PreparedStatement sql2 = conn.prepareStatement("DELETE FROM Invoice");
			PreparedStatement sql3 = conn.prepareStatement("DELETE FROM Products");
			PreparedStatement sql4 = conn.prepareStatement("DELETE FROM Person");

			
			sql1.executeUpdate();  
			sql2.executeUpdate(); 
			sql3.executeUpdate(); 
			sql4.executeUpdate(); 
			
			sql1.close();
			sql2.close();
			sql3.close();
			sql4.close();
			disable = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
			disable.execute("SET FOREIGN_KEY_CHECKS = 1");
			disable.close();
			enableKeys(conn);
			conn.close();
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Removes the person record from the database corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 */
	public static void removePerson(String personCode) {
		
		Connection conn = sqlConnection.getConnection();

		try {
			disableKeys(conn);
			PreparedStatement sql1 = conn.prepareStatement("DELETE FROM Emails WHERE Person= ?");
			sql1.setString(1, personCode);
			PreparedStatement sql2 = conn.prepareStatement("DELETE FROM Invoice WHERE salesPerson= ?");
			sql2.setString(1, personCode);
			PreparedStatement sql3 = conn.prepareStatement("DELETE FROM Person WHERE PersonCode= ?");
			sql3.setString(1, personCode);
			
			sql1.executeUpdate();  
			sql2.executeUpdate(); 
			sql3.executeUpdate(); 
			
			sql1.close();
			sql2.close();
			sql3.close();
			enableKeys(conn);
			conn.close();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to add a person record to the database with the provided data. 
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, 
			String street, String city, String state, String zip, String country) {
		Connection cunning = sqlConnection.getConnection();
		
		//Need to check if address exists, and if so, get addressID. Else- Create address and get addressID
		//First check if person exists
		try {
			disableKeys(cunning);
			PreparedStatement checkPerson = cunning.prepareStatement("SELECT * FROM Person WHERE PersonCode = ?");
			checkPerson.setString(1, personCode);
			ResultSet checkedPerson = checkPerson.executeQuery();
			
			//For future reference, checking to see if record already exists is not important, but checking for address is
			if(!checkedPerson.last()) { //If false, record does not exist
				//Need to add address first. Check if exists.
				PreparedStatement checkAddress = cunning.prepareStatement("SELECT * FROM Address WHERE Street = ? AND City = ? AND State = ? AND Zip = ?");
				checkAddress.setString(1, street);
				checkAddress.setString(2, city);
				checkAddress.setString(3, state);
				checkAddress.setString(4, zip);
				ResultSet checkedAddress = checkAddress.executeQuery();
				int AddressID; //Will store AddressID for adding the person
				
				if(!checkedAddress.last()) { //Again, if this is false, the address does not exist. Now add it
					AddressID = addAddress(street, city, state, zip, country);
				} else {  //If address DOES exist, get the address id
						AddressID = checkedAddress.getInt("AddressID");
				}
				
				checkedAddress.close();
				checkAddress.close();
				
				//Finally time to add the person

				PreparedStatement addperson = cunning.prepareStatement("INSERT INTO Person(PersonCode, AddressID, FirstName, LastName) VALUES (?,?,?,?)");
				addperson.setString(1, personCode);
				addperson.setInt(2, AddressID);
				addperson.setString(3, firstName);
				addperson.setString(4, lastName);
				
				addperson.executeUpdate();
				
			}
		checkPerson.close();
		checkedPerson.close();	
		enableKeys(cunning);
		cunning.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
	}
	
	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		Connection emailConn = sqlConnection.getConnection();
		try {
			PreparedStatement emailAdder = emailConn.prepareStatement("INSERT INTO Emails(address, Person) VALUES (?,?)");
			emailAdder.setString(1, email);
			emailAdder.setString(2, personCode);
			emailAdder.executeUpdate();
			
			emailAdder.close();
			emailConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method to add an address to the database
	 */
	public static int addAddress(String street, String city, String state, String zip, String country) {
		Connection addressConnection = sqlConnection.getConnection();
		int AddressID = 0;
		try {
			PreparedStatement addressAdder = addressConnection.prepareStatement("INSERT INTO Address(Street, City, State, Zip, Country) VALUES (?,?,?,?,?)");
			addressAdder.setString(1, street);
			addressAdder.setString(2, city);
			addressAdder.setString(3, state);
			addressAdder.setString(4, zip);
			addressAdder.setString(5, country);
			
			addressAdder.executeUpdate();
			
			addressAdder = addressConnection.prepareStatement("SELECT LAST_INSERT_ID()");
			
			
			//Get new addressID and return
			ResultSet newAddressID = addressAdder.executeQuery();
			newAddressID.next();
			AddressID = newAddressID.getInt("LAST_INSERT_ID()");
			newAddressID.close();
			addressAdder.close();
			addressConnection.close();
			return AddressID;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//TODO Add precautionary closes
		}
		return AddressID;
	}
	/**
	 * Method that removes every customer record from the database
	 */
	public static void removeAllCustomers() {
		
		Connection conn = sqlConnection.getConnection();

		try {

			disableKeys(conn);
			PreparedStatement sql1 = conn.prepareStatement("DELETE FROM Invoice");
			PreparedStatement sql2 = conn.prepareStatement("DELETE FROM Customer");
			sql1.executeUpdate(); 
			sql2.executeUpdate(); 
			sql1.close();
			sql2.close();
			enableKeys(conn);
			conn.close();
			
		
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	public static void addCustomer(String customerCode, String type, String primaryContactPersonCode, String name, 
			String street, String city, String state, String zip, String country) {
		//TODO Verify that this method works
		Connection customerAdder = sqlConnection.getConnection();
		
		
		//Need to check if address exists or not.. 
		//TODO Make this a checkAddress method?
		try {
			disableKeys(customerAdder);
			PreparedStatement checkAddress = customerAdder.prepareStatement("SELECT * FROM Address WHERE Street = ? AND City = ? AND State = ? AND Zip = ?");
			checkAddress.setString(1, street);
			checkAddress.setString(2, city);
			checkAddress.setString(3, state);
			checkAddress.setString(4, zip);
			ResultSet checkedAddress = checkAddress.executeQuery();
			int AddressID; //Will store AddressID for adding the customer
			
			if(!checkedAddress.last()) { //Again, if this is false, the address does not exist. Now add it
				AddressID = addAddress(street, city, state, zip, country);
			} else {  //If address DOES exist, get the address id
					AddressID = checkedAddress.getInt("AddressID");
			}
			
			checkAddress.close();
			
			//Now need to add the customer
			PreparedStatement customerAdd = customerAdder.prepareStatement("INSERT INTO Customer(custType,custName,custCode,AddressID,priContact) VALUES(?,?,?,?,?)" );
			customerAdd.setString(1,type);
			customerAdd.setString(2,name);
			customerAdd.setString(3, customerCode);
			customerAdd.setInt(4, AddressID);
			customerAdd.setString(5, primaryContactPersonCode);
			
			customerAdd.executeUpdate();
			
			customerAdd.close();
			enableKeys(customerAdder);
			customerAdder.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Removes all product records from the database
	 */


	public static void removeAllProducts() {
		
		Connection conn = sqlConnection.getConnection();

		try {

			PreparedStatement sql1 = conn.prepareStatement("DELETE FROM Invoice");
			PreparedStatement sql2 = conn.prepareStatement("DELETE FROM Products");
			 
			sql1.executeUpdate(); 
			sql2.executeUpdate(); 
			sql1.close();
			sql2.close();
 			conn.close();
 			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}


	/**
	 * Removes a particular product record from the database corresponding to the
	 * provided <code>productCode</code>
	 * @param assetCode
	 */
	public static void removeProduct(String productCode) {
		
		Connection conn = sqlConnection.getConnection();

		try {

			PreparedStatement sql1 = conn.prepareStatement("DELETE FROM Invoice WHERE productCode=?");
			sql1.setString(1,productCode);
			PreparedStatement sql2 = conn.prepareStatement("DELETE FROM Products WHERE productCode=?");
			sql2.setString(1,productCode);
			 
			sql1.executeUpdate(); 
			sql2.executeUpdate(); 
			sql1.close();
			sql2.close();
			conn.close();
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Adds an equipment record to the database with the
	 * provided data.  
	 */
	public static void addEquipment(String productCode, String name, Double pricePerUnit) {
		
		Connection equipConn = sqlConnection.getConnection();
		try {
			disableKeys(equipConn);
			PreparedStatement equipAdder = equipConn.prepareStatement("INSERT INTO Products(productCode, prodName, pricePerUnit) VALUES (?,?,?)");
			equipAdder.setString(1, productCode);
			equipAdder.setString(2, name);
			equipAdder.setDouble(3, pricePerUnit);
			
			equipAdder.executeUpdate();
			
			equipAdder.close();
			
			enableKeys(equipConn);
			equipConn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Adds an license record to the database with the
	 * provided data.  
	 */
	public static void addLicense(String productCode, String name, double serviceFee, double annualFee) {
		Connection licenseConn = sqlConnection.getConnection();
		try {
			disableKeys(licenseConn);
			PreparedStatement licenseAdder = licenseConn.prepareStatement("INSERT INTO Products(productCode, prodName, serviceFee, annualFee) VALUES (?,?,?,?)");
			licenseAdder.setString(1, productCode);
			licenseAdder.setString(2, name);
			licenseAdder.setDouble(3, serviceFee);
			licenseAdder.setDouble(4, annualFee);

			
			licenseAdder.executeUpdate();
			
			licenseAdder.close();
			
			enableKeys(licenseConn);
			licenseConn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Adds an consultation record to the database with the
	 * provided data.  
	 */
	public static void addConsultation(String productCode, String name, String consultantPersonCode, Double hourlyFee) {
		Connection consultationConn = sqlConnection.getConnection();
		
		try {
			disableKeys(consultationConn);
			PreparedStatement consultationAdder = consultationConn.prepareStatement("INSERT INTO Products(productCode,prodName,Consultant,hourlyFee) VALUES(?,?,?,?)");
			
			consultationAdder.setString(1, productCode);
			consultationAdder.setString(2, name);
			consultationAdder.setString(3, consultantPersonCode);
			consultationAdder.setDouble(4, hourlyFee);
			
			consultationAdder.executeUpdate();
			
			consultationAdder.close();
			enableKeys(consultationConn);
			consultationConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Removes all invoice records from the database
	 */
	public static void removeAllInvoices() {
		
		Connection conn = sqlConnection.getConnection();

		try {

			
			PreparedStatement sql1 = conn.prepareStatement("DELETE FROM Invoice");
			
			 
			sql1.executeUpdate(); 

			
			sql1.close();
			conn.close();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes the invoice record from the database corresponding to the
	 * provided <code>invoiceCode</code>
	 * @param invoiceCode
	 */
	public static void removeInvoice(String invoiceCode) {
		
		Connection conn = sqlConnection.getConnection();

		try {

			PreparedStatement sql1 = conn.prepareStatement("DELETE FROM Invoice WHERE invoiceCode=?");
			sql1.setString(1, invoiceCode);


			 
			sql1.executeUpdate(); 
			sql1.close();
			conn.close();
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds an invoice record to the database with the given data.  
	 */
	public static void addInvoice(String invoiceCode, String customerCode, String salesPersonCode) {
		Connection invoiceConn = sqlConnection.getConnection();
		
		try {
			disableKeys(invoiceConn);
			PreparedStatement invoiceAdder = invoiceConn.prepareStatement("INSERT INTO Invoice(invoiceCode, salesPerson,custCode) VALUES(?,?,?)");
			invoiceAdder.setString(1, invoiceCode);
			invoiceAdder.setString(2, salesPersonCode);
			invoiceAdder.setString(3, customerCode);
			
			invoiceAdder.executeUpdate();
			
			invoiceAdder.close();
			enableKeys(invoiceConn);
			invoiceConn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * number of units
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String productCode, int numUnits) {
		//Each of the next three methods does exactly the same thing- updates an invoice with this information.
		//Will need to do some data manipulation for each though. Our quantity field in the database is a double, for instance
		
		Connection invoiceConn = sqlConnection.getConnection();
		
		
		try {
			disableKeys(invoiceConn);
			//First need to check if invoice exists. If not, the following will work. If it does, need to update that row
			PreparedStatement invoiceGetter = invoiceConn.prepareStatement("SELECT custCode,salesPerson FROM Invoice WHERE invoiceCode=?");
			PreparedStatement invoiceChecker = invoiceConn.prepareStatement("SELECT productCode FROM Invoice WHERE invoiceCode=?");
			invoiceChecker.setString(1, productCode);
			
			if(!invoiceChecker.execute()) {//False if empty 
				PreparedStatement invoiceUpdater = invoiceConn.prepareStatement("UPDATE Invoice SET productCode=?,quantity=? WHERE invoiceCode=?");
				invoiceUpdater.setString(1, productCode);
				invoiceUpdater.setDouble(2, numUnits);
				invoiceUpdater.setString(3, invoiceCode);
				invoiceUpdater.executeUpdate();
				invoiceUpdater.close();
				enableKeys(invoiceConn);
				invoiceConn.close();
			} else {
		
			invoiceGetter.setString(1, invoiceCode);
			ResultSet checkers = invoiceGetter.executeQuery();
			checkers.next();
			String custCode = checkers.getString("custCode");
			String salesPerson = checkers.getString("salesPerson");
			PreparedStatement invoiceUpdater = invoiceConn.prepareStatement("INSERT INTO Invoice (invoiceCode,salesPerson,custCode,productCode,quantity) VALUES (?,?,?,?,?)");
			invoiceUpdater.setString(1, invoiceCode);
			invoiceUpdater.setString(2, salesPerson);
			invoiceUpdater.setString(3, custCode);
			invoiceUpdater.setString(4,productCode);
			invoiceUpdater.setDouble(5, numUnits);
			invoiceUpdater.executeUpdate();
			invoiceGetter.close();
			invoiceUpdater.close();
			checkers.close();
			enableKeys(invoiceConn);
			invoiceConn.close();}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Adds a particular license (corresponding to <code>productCode</code> to an 
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * begin/end dates
	 */
	public static void addLicenseToInvoice(String invoiceCode, String productCode, String startDate, String endDate) {
		//Need to convert start date and end date to a quantity for our invoice object
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		Connection invoiceConn = sqlConnection.getConnection();
		
		DateTime begin = new DateTime(DateTime.parse(startDate, fmt));
		DateTime finish = new DateTime(DateTime.parse(endDate, fmt));
		double period = Days.daysBetween(begin, finish).getDays(); //Number of days for quantity
		
		
		try {
			disableKeys(invoiceConn);
			PreparedStatement invoiceChecker = invoiceConn.prepareStatement("SELECT productCode FROM Invoice WHERE invoiceCode=?");
			PreparedStatement invoiceUpdater;
			invoiceChecker.setString(1, invoiceCode);
			if (!invoiceChecker.execute()) {//Update else insert
				invoiceUpdater = invoiceConn.prepareStatement("UPDATE Invoice SET productCode=?, quantity=? WHERE invoiceCode=?");
				invoiceUpdater.setString(1, productCode);
				invoiceUpdater.setDouble(2, period);
				invoiceUpdater.setString(3, invoiceCode);
				invoiceUpdater.executeUpdate();
				enableKeys(invoiceConn);
				invoiceConn.close();
			} else { //Insert into 
				PreparedStatement invoiceQuery = invoiceConn.prepareStatement("SELECT custCode,salesPerson FROM Invoice WHERE invoiceCode=?");
				invoiceQuery.setString(1, invoiceCode);
				ResultSet invoiceResults = invoiceQuery.executeQuery();
				invoiceResults.next();
				String custCode = invoiceResults.getString("custCode");
				String salesPerson = invoiceResults.getString("salesPerson");
				invoiceUpdater = invoiceConn.prepareStatement("INSERT INTO Invoice (invoiceCode,salesPerson,custCode,productCode,quantity) VALUES (?,?,?,?,?)");
				invoiceUpdater.setString(1, invoiceCode);
				invoiceUpdater.setString(2, salesPerson);
				invoiceUpdater.setString(3, custCode);
				invoiceUpdater.setString(4, productCode);
				invoiceUpdater.setDouble(5, period);
				invoiceUpdater.executeUpdate();
			} 
			invoiceUpdater.close();
			invoiceConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

	
	public static void addConsultationToInvoice(String invoiceCode, String productCode, double numHours)  {
		
		Connection invoiceConn = sqlConnection.getConnection();
		
		
		try {
			
			disableKeys(invoiceConn);
			//First need to check if invoice exists. If not, the following will work. If it does, need to update that row
			PreparedStatement invoiceGetter = invoiceConn.prepareStatement("SELECT custCode,salesPerson FROM Invoice WHERE invoiceCode=?");
			PreparedStatement invoiceChecker = invoiceConn.prepareStatement("SELECT productCode FROM Invoice WHERE invoiceCode=?");
			invoiceChecker.setString(1, productCode);
			
			if(!invoiceChecker.execute()) {//False if empty 
				PreparedStatement invoiceUpdater = invoiceConn.prepareStatement("UPDATE Invoice SET productCode=?,quantity=? WHERE invoiceCode=?");
				invoiceUpdater.setString(1, productCode);
				invoiceUpdater.setDouble(2, numHours);
				invoiceUpdater.setString(3, invoiceCode);
				invoiceUpdater.executeUpdate();
			} else {
		
			invoiceGetter.setString(1, invoiceCode);
			ResultSet checkers = invoiceGetter.executeQuery();
			checkers.next();
			String custCode = checkers.getString("custCode");
			String salesPerson = checkers.getString("salesPerson");
			PreparedStatement invoiceUpdater = invoiceConn.prepareStatement("INSERT INTO Invoice (invoiceCode,salesPerson,custCode,productCode,quantity) VALUES (?,?,?,?,?)");
			invoiceUpdater.setString(1, invoiceCode);
			invoiceUpdater.setString(2, salesPerson);
			invoiceUpdater.setString(3, custCode);
			invoiceUpdater.setString(4,productCode);
			invoiceUpdater.setDouble(5, numHours);
			invoiceUpdater.executeUpdate();
			invoiceGetter.close();
			invoiceUpdater.close();
			checkers.close();
			enableKeys(invoiceConn);
			invoiceConn.close(); }
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
