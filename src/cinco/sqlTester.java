package cinco;
import java.sql.*;
//This is just a class to test the sql connection
public class sqlTester {

	public static void main(String[] args) {
		Connection cunning = sqlConnection.getConnection();
		String sql = "SELECT Person.* , Address.* FROM Person JOIN Address ON Person.AddressID=Address.AddressID";
		ResultSet rs = null;
		try {
			String personCode = "666";
			PreparedStatement checkPerson = cunning.prepareStatement("SELECT * FROM Person WHERE PersonCode = ?");
			checkPerson.setString(1, personCode);
			ResultSet checkedPerson = checkPerson.executeQuery();
			 if (!checkedPerson.last()) { //If checkedPerson.last is true, there are results and the person exists
				 
			 }
			 checkPerson.close();
			checkedPerson.close();
			cunning.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			cunning.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	

	}

}
