package cinco;
import java.sql.*;
//This is just a class to test the sql connection
public class sqlTester {

	public static void main(String[] args) {
//		Connection cunning = sqlConnection.getConnection();
//		String sql = "SELECT Person.* , Address.* FROM Person JOIN Address ON Person.AddressID=Address.AddressID";
//		ResultSet rs = null;
//		try {
//			PreparedStatement ps = cunning.prepareStatement(sql);
//			rs = ps.executeQuery();
//			while (rs.next()) {
//				System.out.println(rs.getString("FirstName"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			rs.close();
//			cunning.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
		
	InvoiceData.addPerson("675w", "Steven", "Wright", "1155 Hewlett", "Seattle", "WA", "76432", "USA");

	}

}
