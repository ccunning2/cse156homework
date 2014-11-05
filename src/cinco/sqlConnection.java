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
	
	
}