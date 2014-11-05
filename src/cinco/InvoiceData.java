package cinco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Method that removes every person record from the database
	 */
	public static void removeAllPersons()  {

	/**
	 * Method that removes every person record from the database
	 */
	public static void removeAllPersons() {
		
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://cse.unl.edu/YOUR_LOGIN";
		String user = "YOUR_LOGIN";
		String password = "YOUR_SQL_PASWORD";

		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement statement = conn.createStatement();

			String sql1 = "DELETE FROM Emails";
			String sql2 = "DELETE FROM Invoice";
			String sql3 = "DELETE FROM Person";
			
			statement.executeQuery(sql1);  
			statement.executeQuery(sql2); 
			statement.executeQuery(sql3); 
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * Removes the person record from the database corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 */
	public static void removePerson(String personCode) {
		
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://cse.unl.edu/YOUR_LOGIN";
		String user = "YOUR_LOGIN";
		String password = "YOUR_SQL_PASWORD";

		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement statement = conn.createStatement();

			String sql1 = "DELETE FROM Email WHERE personCode='"+personCode+"'";
			String sql2 = "DELETE FROM Invoice WHERE personCode='"+personCode+"'";
			String sql3 = "DELETE FROM Person WHERE personCode='"+personCode+"'";
			
			statement.executeQuery(sql1);  
			statement.executeQuery(sql2); 
			statement.executeQuery(sql3); 
			conn.close();
			
		}catch(Exception e){
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
	}
	
	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
	}
	
	/**
	 * Method that removes every customer record from the database
	 */
	public static void removeAllCustomers() {
		
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://cse.unl.edu/YOUR_LOGIN";
		String user = "YOUR_LOGIN";
		String password = "YOUR_SQL_PASWORD";

		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement statement = conn.createStatement();


			String sql1 = "DELETE FROM Invoice";
			String sql2 = "DELETE FROM Customer";
			 

			statement.executeQuery(sql1); 
			statement.executeQuery(sql2); 
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void addCustomer(String customerCode, String type, String primaryContactPersonCode, String name, 
			String street, String city, String state, String zip, String country) {
	}

	/**
	 * Removes all product records from the database
	 */
	public static void removeAllProducts() {
		
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://cse.unl.edu/YOUR_LOGIN";
		String user = "YOUR_LOGIN";
		String password = "YOUR_SQL_PASWORD";

		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement statement = conn.createStatement();

			String sql1 = "DELETE FROM Invoice";
			String sql2 = "DELETE FROM Product";
			 
			statement.executeQuery(sql1); 
			statement.executeQuery(sql2); 
 			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Removes a particular product record from the database corresponding to the
	 * provided <code>productCode</code>
	 * @param assetCode
	 */
	public static void removeProduct(String productCode) {
		
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://cse.unl.edu/YOUR_LOGIN";
		String user = "YOUR_LOGIN";
		String password = "YOUR_SQL_PASWORD";

		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement statement = conn.createStatement();

			String sql1 = "DELETE FROM Invoice WHERE productCode='"+productCode+"'";
			String sql2 = "DELETE FROM Product WHERE productCode='"+productCode+"'";
			 
			statement.executeQuery(sql1); 
			statement.executeQuery(sql2); 
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Adds an equipment record to the database with the
	 * provided data.  
	 */
	public static void addEquipment(String productCode, String name, Double pricePerUnit) {}
	
	/**
	 * Adds an license record to the database with the
	 * provided data.  
	 */
	public static void addLicense(String productCode, String name, double serviceFee, double annualFee) {}

	/**
	 * Adds an consultation record to the database with the
	 * provided data.  
	 */
	public static void addConsultation(String productCode, String name, String consultantPersonCode, Double hourlyFee) {}
	
	/**
	 * Removes all invoice records from the database
	 */
	public static void removeAllInvoices() {
		
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://cse.unl.edu/YOUR_LOGIN";
		String user = "YOUR_LOGIN";
		String password = "YOUR_SQL_PASWORD";

		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement statement = conn.createStatement();

			String sql1 = "DELETE FROM Invoice";
			 
			statement.executeQuery(sql1); 

			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes the invoice record from the database corresponding to the
	 * provided <code>invoiceCode</code>
	 * @param invoiceCode
	 */
	public static void removeInvoice(String invoiceCode) {
		
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://cse.unl.edu/YOUR_LOGIN";
		String user = "YOUR_LOGIN";
		String password = "YOUR_SQL_PASWORD";

		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement statement = conn.createStatement();

			String sql1 = "DELETE FROM Invoice WHERE invoiceCode='"+invoiceCode+"'";
			 
			statement.executeQuery(sql1); 
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds an invoice record to the database with the given data.  
	 */
	public static void addInvoice(String invoiceCode, String customerCode, String salesPersonCode) {}
	
	/**
	 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * number of units
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String productCode, int numUnits) {}
	
	/**
	 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * begin/end dates
	 */
	public static void addLicenseToInvoice(String invoiceCode, String productCode, String startDate, String endDate) {}

	/**
	 * Adds a particular equipment (corresponding to <code>productCode</code> to an 
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * number of billable hours.
	 */
	public static void addConsultationToInvoice(String invoiceCode, String productCode, double numHours) {}
}
