package cinco;

import java.sql.*;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Method that removes every person record from the database
	 */
	public static void removeAllPersons() {
		
		Connection conn = sqlConnection.getConnection();

		try {
			PreparedStatement sql1 = conn.prepareStatement("DELETE FROM Emails");
			PreparedStatement sql2 = conn.prepareStatement("DELETE FROM Invoice");
			PreparedStatement sql3 = conn.prepareStatement("DELETE FROM Person");

			
			sql1.executeUpdate();  
			sql2.executeUpdate(); 
			sql3.executeUpdate(); 
			conn.close();
			sql1.close();
			sql2.close();
			sql3.close();
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
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

			Statement statement = conn.createStatement();

			String sql1 = "DELETE FROM Email WHERE personCode='"+personCode+"'";
			String sql2 = "DELETE FROM Invoice WHERE personCode='"+personCode+"'";
			String sql3 = "DELETE FROM Person WHERE personCode='"+personCode+"'";
			
			statement.executeUpdate(sql1);  
			statement.executeUpdate(sql2); 
			statement.executeUpdate(sql3); 
			conn.close();
			statement.close();
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
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

			Statement statement = conn.createStatement();


			String sql2 = "DELETE FROM Invoice";
			String sql3 = "DELETE FROM Customer";
			 

			statement.executeUpdate(sql2); 
			statement.executeUpdate(sql3); 
			conn.close();
			statement.close();
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
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

			Statement statement = conn.createStatement();

			String sql2 = "DELETE FROM Invoice";
			String sql3 = "DELETE FROM Product";
			 
			statement.executeUpdate(sql2); 
			statement.executeUpdate(sql3); 
 			conn.close();
 			statement.close();
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
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
			Statement statement = conn.createStatement();

			String sql1 = "DELETE FROM Invoice WHERE productCode='"+productCode+"'";
			String sql2 = "DELETE FROM Product WHERE productCode='"+productCode+"'";
			 
			statement.executeUpdate(sql1); 
			statement.executeUpdate(sql2); 
			conn.close();
			statement.close();
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
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
			PreparedStatement equipAdder = equipConn.prepareStatement("INSERT INTO Products(productCode, prodName, pricePerUnit) VALUES (?,?,?)");
			equipAdder.setString(1, productCode);
			equipAdder.setString(2, name);
			equipAdder.setDouble(3, pricePerUnit);
			
			equipAdder.executeUpdate();
			
			equipAdder.close();
			
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
			PreparedStatement licenseAdder = licenseConn.prepareStatement("INSERT INTO Products(productCode, prodName, serviceFee, annualFee) VALUES (?,?,?,?)");
			licenseAdder.setString(1, productCode);
			licenseAdder.setString(2, name);
			licenseAdder.setDouble(3, serviceFee);
			licenseAdder.setDouble(4, annualFee);

			
			licenseAdder.executeUpdate();
			
			licenseAdder.close();
			
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
	public static void addConsultation(String productCode, String name, String consultantPersonCode, Double hourlyFee) {}
	
	/**
	 * Removes all invoice records from the database
	 */
	public static void removeAllInvoices() {
		
		Connection conn = sqlConnection.getConnection();

		try {

			Statement statement = conn.createStatement();

			String sql1 = "DELETE FROM Invoice";
			 
			statement.executeUpdate(sql1); 

			conn.close();
			statement.close();
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
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

			Statement statement = conn.createStatement();

			String sql1 = "DELETE FROM Invoice WHERE invoiceCode='"+invoiceCode+"'";
			 
			statement.executeUpdate(sql1); 
			
			conn.close();
			statement.close();
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
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
			PreparedStatement invoiceAdder = invoiceConn.prepareStatement("INSERT INTO Invoice(invoiceCode, salesPerson,custCode) VALUES(?,?,?)");
			invoiceAdder.setString(1, invoiceCode);
			invoiceAdder.setString(2, salesPersonCode);
			invoiceAdder.setString(3, customerCode);
			
			invoiceAdder.executeUpdate();
			
			invoiceAdder.close();
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
		double quantity = (double) numUnits;
		Connection invoiceConn = sqlConnection.getConnection();
		
		
		try {
			PreparedStatement invoiceUpdater = invoiceConn.prepareStatement("UPDATE Invoice SET productCode=?, quantity=? WHERE invoiceCode=?");
			invoiceUpdater.setString(1, productCode);
			invoiceUpdater.setDouble(2, quantity);
			invoiceUpdater.setString(3, invoiceCode);
			
			invoiceUpdater.close();
			invoiceConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
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
			PreparedStatement invoiceUpdater = invoiceConn.prepareStatement("UPDATE Invoice SET productCode=?, quantity=? WHERE invoiceCode=?");
			invoiceUpdater.setString(1, productCode);
			invoiceUpdater.setDouble(2, period);
			invoiceUpdater.setString(3, invoiceCode);
			
			invoiceUpdater.close();
			invoiceConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

	/**
	 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * number of billable hours.
	 */
	public static void addConsultationToInvoice(String invoiceCode, String productCode, double numHours)  {
		
		Connection conn = sqlConnection.getConnection();


		try {

			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Invoice(invoiceCode,productCode,quantity) VALUES(?,?,?)");
			pstmt.setString(1,invoiceCode);
			pstmt.setString(2,productCode);
			pstmt.setDouble(3,numHours);
			
			pstmt.executeUpdate();
			
			conn.close();
			pstmt.close();
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
