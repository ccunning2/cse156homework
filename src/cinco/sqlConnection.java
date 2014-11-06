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
	 * @param conn
	 * @return
	 */
	public static ResultSet getPersons(Connection conn) {
		String sql = "SELECT * FROM Person";
		
		try {
			PreparedStatement personGetter = conn.prepareStatement(sql);
			ResultSet persons = personGetter.executeQuery();
			personGetter.close();
			return persons;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	
	
	
	
	
	
	
	
}