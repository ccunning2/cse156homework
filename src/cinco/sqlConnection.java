package cinco;
import java.sql.*;


//Class and or methods to connect to the sql database


//Include constants for drivername, useid, password, url
public class sqlConnection {
	public static final String url = "jdbc:mysql://cse.unl.edu/ccunning";
	public static final String username = "ccunning";
	public static final String password = "sqlccunning";
	public static final String driver = "com.mysql.jdbc.Driver";
	
	public static Connection getConnection() {
		try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			return conn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	/***
	 * Simple method to get a resultset containing all persons from the database.
	 * @param conn Connection to sql database
	 * @return ResultSet containing all persons from db
	 */
	public static ResultSet getPersons(Connection conn) {
		String sql = "SELECT * FROM Person";
		
		try {
			PreparedStatement personGetter = conn.prepareStatement(sql);
			ResultSet persons = personGetter.executeQuery();
			//personGetter.close();    Test
			return persons;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns resultSet of all customers from DB
	 * @param conn Connection to db
	 * @return resultset
	 */
	public static ResultSet getCustomers(Connection conn) {
		String sql = "SELECT * FROM Customer";
		
		try {
			PreparedStatement customerGetter = conn.prepareStatement(sql);
			ResultSet customers = customerGetter.executeQuery();
//			customerGetter.close();  
			return customers;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns resultset of products
	 */
	
	public static ResultSet getProducts(Connection conn){
		String sql = "SELECT * FROM Products";
		
		try {
			PreparedStatement productsGetter = conn.prepareStatement(sql);
			ResultSet products = productsGetter.executeQuery();
			
//			productsGetter.close();
			return products;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * Returns resultset of invoicedata 
	 */
	
	public static ResultSet getInvoice(Connection conn) {
		String sql = "SELECT * FROM Invoice";
		try {
			PreparedStatement invoiceGetter = conn.prepareStatement(sql);
			ResultSet invoices = invoiceGetter.executeQuery();
			
//			invoiceGetter.close();
			
			return invoices;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * Returns number of invoice records
	 * @param conn
	 * @return
	 */
	public static int getInvoiceCount(Connection conn) {
		int count = 0;
		String sql = "SELECT COUNT(DISTINCT invoiceCode) FROM Invoice";
		try {
			PreparedStatement invoiceGetter = conn.prepareStatement(sql);
			ResultSet invoices = invoiceGetter.executeQuery();
			invoices.next();
			count = invoices.getInt(1);
			invoiceGetter.close();
			invoices.close();
			return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	
	
	/**
	 * Will return an address created from database info.
	 * @param AddressID
	 * @return
	 */
	public static Address getAddress(int AddressID) {
		String street=null,city=null,state=null,zip=null,country = null;
		
		Connection addressConn = getConnection();
		
		try {
			PreparedStatement addressGet = addressConn.prepareStatement("SELECT * FROM Address WHERE AddressID=?");
			addressGet.setInt(1, AddressID);
			ResultSet addressInfo = addressGet.executeQuery();
			while (addressInfo.next()) { //Should only be one address per AddressID
				street = addressInfo.getString("Street");
				city = addressInfo.getString("City");
				state = addressInfo.getString("State");
				zip = addressInfo.getString("zip");
				country = addressInfo.getString("Country");
			}
			
			addressGet.close();
			addressInfo.close();
			addressConn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		Address address = new Address(street,city,state,zip,country);
		return address;
	}

	
	
	
	/**
	 * Will return a String[] of all emails associated with a personcode
	 * @param personCode
	 * @return
	 */
	public static String[] getEmails(String personCode) {
		String sql = "SELECT address FROM Emails WHERE Person=?";
		Connection emailsConn = getConnection();
		String[] emails = null;  //Array to hold emails
		try {
			PreparedStatement emailsGetter = emailsConn.prepareStatement(sql);
			emailsGetter.setString(1, personCode);
			
			
			ResultSet dbEmails = emailsGetter.executeQuery();
			//Need to set size of array
			int size = 0;
			dbEmails.last();
			size = dbEmails.getRow();  //Row number is number of emails
			dbEmails.beforeFirst();
			emails = new String[size];
			
			int iterator = 0;
			while (dbEmails.next()) {
				emails[iterator] = dbEmails.getString("address");
			}
			dbEmails.close();
			emailsGetter.close();
			emailsConn.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return emails;
	}
	
	
	public static Person getPerson(Connection conn, String personCode) {
		String sql = "SELECT * FROM Person WHERE PersonCode =?";
		String[] emails = null;
		Address address = null;
		int addressID = 0;
		String firstName = null;
		String lastName = null;
		Person personx = null;
		try {
			PreparedStatement personGetter = conn.prepareStatement(sql);
			personGetter.setString(1, personCode);
			ResultSet person = personGetter.executeQuery();
			person.next();
			addressID = person.getInt("AddressID");
			address = getAddress(addressID);
			getEmails(personCode);
			firstName = person.getString("FirstName");
			lastName = person.getString("LastName");
			
			personx = new Person(personCode, firstName, lastName, address, emails );
			personGetter.close();
			person.close();
			return personx;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	
	
	
}