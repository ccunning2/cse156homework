package cinco;
import java.sql.*;

import com.cinco.InvoiceData;
//This is just a class to test the sql connection
public class sqlTester {

	public static void main(String[] args) {

//addPerson works
	//InvoiceData.addPerson("675w", "Steven", "Wright", "1155 Hewlett", "Seattle", "WA", "76432", "USA");

		//addEmail verified works
		//InvoiceData.addEmail("675e", "LOTRsucks@tolkien.net");
		
		
		//removeallpersons works
		//InvoiceData.removeAllPersons();
	
		//removePerson works
		//InvoiceData.removePerson("675e");
	
		//addPerson works
		//InvoiceData.addPerson("555t", "John", "Kennedy", "555 smith st", "Washington", "DC", "87743", "USA");
	
		//addemail works
		//InvoiceData.addEmail("555t", "JFK@hotmail.com");
	
		//removeallcustomers works
		//InvoiceData.removeAllCustomers();
		
		
		//addCustomer works
//		InvoiceData.addCustomer("C025", "G", "555t", "Kennedy Inc.", "444 Rudy Drive", "Tacoma", "WA", "67432", "USA");
	
		//removeproducts works
//		InvoiceData.removeAllProducts();
	
			//works
//		InvoiceData.removeProduct("fp12");
		
//		InvoiceData.addEquipment("543t", "Wrench", 2000.00);
		
//		InvoiceData.addLicense("721", "winRAR Deluxe", 12.99, 425.00);
		
//		InvoiceData.addConsultation("499", "Help!", "322b", 333.00);
		
//        InvoiceData.removeAllInvoices();
		
//		InvoiceData.removeInvoice("INV004");
		
//		InvoiceData.addInvoice("INV007", "C001", "322b");
		
		//InvoiceData.addEquipmentToInvoice("INV007","782g",8);
		
		//InvoiceData.addLicenseToInvoice("INV004", "90fa", "1999-12-13", "2001-03-14");
		
		InvoiceData.addConsultationToInvoice("INV004", "782g", 24);
		
		
		
	}
	

}
