package cinco;
import java.io.*;
import java.sql.*;
import java.util.Map;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.*;  
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
//This will read .dat files, parse, create objects, and convert to XML/JSON OLD

//Will now read from database and create the objects.
public class DataConverter {

	//Reads the Person database
	public static Person[] readPersons() {
		String personCode = null;
		String FirstName = null;
		String LastName = null;
		String[] emails = null;

		//Scanner	s = new Scanner(new FileReader("data/ourpersons.dat"));
		//int count = s.nextInt(); //Number of entries. 
		Person[] persons = null;
		try {
			Connection personGetter = sqlConnection.getConnection();
			
			ResultSet dbPersons = sqlConnection.getPersons(personGetter);
			
			//Would like to re-use as much code as possible, so in keeping with the orignal method we will determine
			//the number of persons we have (or try to)
			
			dbPersons.last();
			int count = dbPersons.getRow(); //This should effectively be the number of persons in the DB
			dbPersons.beforeFirst(); //Return to start of resultset
			
			persons = new Person[count];

			int iterator = 0; //Used to iterate in array creation
			while (dbPersons.next()) {
				
				int addressID = dbPersons.getInt("AddressID");
				Address address = sqlConnection.getAddress(addressID);
				personCode = dbPersons.getString("PersonCode");
				emails = sqlConnection.getEmails(personCode);
				FirstName = dbPersons.getString("FirstName");
				LastName = dbPersons.getString("LastName");
				
				persons[iterator] = new Person(personCode,FirstName,LastName, address, emails);
				iterator++;
			}
			dbPersons.close();
			personGetter.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return persons;

		
		
	}
	//Changed following method to take Persons database as input argument, to be used in instantiating primary contact person
	public static Customer[] readCustomers(Person[] persons) {  //Will read data from file and output to array. 
		try {
		Scanner	s = new Scanner(new FileReader("data/ourCustomers.dat"));
		
		int count = s.nextInt(); //Number of entries. 
		Customer[] customers = new Customer[count]; //Array to hold persons
		s.nextLine(); //Advance scanner to next line
		int iterator = 0; //Used to iterate in array creation
		while (s.hasNext()) {
			String inLine = s.nextLine(); //Make a string out of the next line
			String [] info;
			info = inLine.split(";"); //Makes an array of of the string, delimited by ';'
		
			
			Address address = new Address(info[4]);
			customers[iterator] = new Customer(info[0], info[1], info[3], address);
			
			for (Person p : persons) {
				if (p.getPersonCode().equalsIgnoreCase(info[2])) {
					customers[iterator].setPrimaryContact(p);
				}
			}
			
			
			
			iterator++;
		}
		s.close();
		return customers;

		} catch (FileNotFoundException e) {
			System.out.println("File not found in readCustomers()");
		}
		return null;
		
		
	}
	
	public static Product[] readProducts(Person[] persons){
		try {
			Scanner	s = new Scanner(new FileReader("data/ourProducts.dat"));
			
			int count = s.nextInt(); //Number of entries. 
			Product[] products = new Product[count]; //Array to hold products
			s.nextLine(); //Advance scanner to next line
			int iterator = 0; //Used to iterate in array creation
			while (s.hasNext()) {
				String inLine = s.nextLine(); //Make a string out of the next line
				String [] info;
				info = inLine.split(";"); //Makes an array of of the string, delimited by ';'
				if (info[1].equalsIgnoreCase("E")){
					products[iterator] = new Equipment(info[0], info[2], Double.parseDouble(info[3]));
				} else if (info[1].equalsIgnoreCase("L")) {
					products[iterator] = new License(info[0], info[2],Double.parseDouble(info[4]), Double.parseDouble(info[3]));
				} else if (info[1].equalsIgnoreCase("C")) {
					products[iterator] = new Consultation(info[0], info[2], Double.parseDouble(info[4]) );
					for (Person p : persons) {
						if (p.getPersonCode().equalsIgnoreCase(info[3])) {
							//Test creating new reference variable
							Consultation consultation = (Consultation) products[iterator];
							consultation.setConsultant(p);
						}
					}
					
				} else {
					products[iterator] = new Product();
				}
			
				iterator++;
			} //End while
			s.close();
			return products;

			} catch (FileNotFoundException e) {
				System.out.println("File not found exception in readProducts");
			}
			return null;
	}
	public static void writeXML(Object[] objects) {
		XStream xstream = new XStream();
		String objType;

		if (!objects.getClass().getCanonicalName().equalsIgnoreCase("Product[]")) {
		objType = objects[0].getClass().getCanonicalName() + "s";
		} else {
		 objType = "Products";  //This is just to deal with automatically naming the output
		}
		//Returns the name of objects we are using with
		String filename = objType + ".xml"; //Filename for output
		File file = new File(filename);
		try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("Error in writeXML IOException");
		}
		try {
			PrintWriter writer = new PrintWriter(filename);
			writer.println("<?xml version=\"1.0\"?>"+"\n");
			for (Object o : objects) {
				xstream.toXML(o, writer);
				writer.println();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error in writeXML File Not Found Exception");
		}
		
		
		

	}
	
	public static void writeJSON(Object[] objects) {
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("License", License.class);
		xstream.alias("Person", Person.class);
		xstream.alias("Address", Address.class);
		xstream.alias("Customer", Customer.class);
		xstream.alias("Consultation", Consultation.class);
		xstream.alias("Product", Product.class);
		xstream.alias("Equipment", Equipment.class);
		String objType;

		if (!objects.getClass().getCanonicalName().equalsIgnoreCase("Product[]")) {
		objType = objects[0].getClass().getCanonicalName() + "s";
		} else {
		 objType = "Products";  //This is just to deal with automatically naming the output
		}
		//Returns the name of objects we are using with
		String filename = objType + ".json"; //Filename for output
		File file = new File(filename);
		try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("Error in writeJSON IOException");
		}
		try {
			PrintWriter writer = new PrintWriter(filename);
//			writer.println("<?xml version=\"1.0\"?>"+"\n");
			for (Object o : objects) {
				xstream.toXML(o, writer);
				writer.println();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error in writeJSON File Not Found Exception");
		}
		
		
		

	}
	
	public static Invoice[] readInvoice(Person[] persons, Customer[] customers, Product[] products) {
		try {
			Scanner	s = new Scanner(new FileReader("data/ourInvoices.dat"));
			int count = s.nextInt(); //Number of entries. 
			Invoice[] invoices = new Invoice[count]; //Array to hold invoices
			int iterator = 0;
			s.nextLine(); //Advance scanner to nextline
			while (s.hasNext()) {
				String inLine = s.nextLine(); //Make a string out of the next line
				String[] info;
				String[] invProducts; //Use this to handle product situation
				info = inLine.split(";"); //Create array of strings from each line, ; delimited
				//First, instantiate new invoice object
				invoices[iterator] = new Invoice(info[0]);
				for (Customer c : customers) {
					if (c.getCustomerCode().equalsIgnoreCase(info[1])) {
						invoices[iterator].setCustomer(c);
					}
				} //End customers for each
				for (Person p: persons) {
					if (p.getPersonCode().equals(info[2])) {
						invoices[iterator].setSalesPerson(p);
					}
				} // End persons for each
				
				
				//This is a String array of product information
				invProducts = info[3].split(",");
				for (String x: invProducts) {
					int index = x.indexOf(":"); //Get index of occurence of ":"
					String code = x.substring(0, index); //This is the product code
					for (Product p : products) { //Iterate over each product in product array
						if (p.getCode().equals(code)) {
							if (p.getClass().getSimpleName().equals("Equipment")){
								invoices[iterator].addProduct(p, Integer.parseInt(x.substring(index+1))); 
							} else if (p.getClass().getSimpleName().equals("Consultation")) {
								invoices[iterator].addProduct(p, Integer.parseInt(x.substring(index+1)));
							} else if (p.getClass().getSimpleName().equals("License")) {
								//First we need to get the number of days
								int index2; //PLacekeeper for substring formation
								DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
								
								index2 = x.indexOf(":", index+1); //Move index to next ":"
								
								String start = x.substring(index+1,index2); 
								String end = x.substring(index2+1);
							
								
								DateTime begin = new DateTime(DateTime.parse(start, fmt));
								DateTime finish = new DateTime(DateTime.parse(end, fmt));
								int period = Days.daysBetween(begin, finish).getDays();
								invoices[iterator].addProduct(p, period);
							}
						}
					}//End for each product loop
				} //End for stringx:invProducts 
				
				iterator++;
			} //End while
			s.close();
			return invoices;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	//This method will print out our invoices' exec summary
	public static void printInvoiceExecSummary(Invoice[] invoices) {
		double summarySubTotal = 0;
		double summaryFees = 0;
		double summaryTotal= 0;
		double summaryTaxes= 0;
		final String HEADER_FORMAT_STRING = "%-12s%-35s%-30s%10s%13s%14s%13s";
		final String DATA_FORMAT_STRING = "%-12s%-35s%-30s$%10.2f      $%8.2f    $%8.2f  $%10.2f%n";
		System.out.println("Executive Summary Report");
		System.out.println("========================");
		System.out.printf(HEADER_FORMAT_STRING, "Invoice","Customer","Salesperon","Subtotal","Fees","Taxes","Total\n");
		for(Invoice i: invoices) {
			System.out.printf(DATA_FORMAT_STRING, i.getCode(),i.getCustomer().getName(),
					i.getSalesPerson().getLastName() + "," + i.getSalesPerson().getFirstName(),
					i.getSubTotal(), i.getInvoiceFees(), i.getTaxes(), i.getTotal());
			summarySubTotal += i.getSubTotal();
			summaryFees += i.getInvoiceFees();
			summaryTaxes += i.getTaxes();
			summaryTotal += i.getTotal();
		}
		System.out.println("=================================================================================================================================");
		System.out.printf("%-77s$%10.2f      $%8.2f    $%8.2f  $%10.2f%n%n","TOTALS", summarySubTotal,summaryFees,summaryTaxes,summaryTotal);
	}
	
	public static void printInvoiceDetail(Invoice invoice) {
		final String INVOICE_FORMAT_STRING = "%-10s%-60s$%-10.2f$%-10.2f%n";
		System.out.println("\nInvoice " + invoice.getCode());
		System.out.println("Salesperson: " + invoice.getSalesPerson().getLastName() + "," + invoice.getSalesPerson().getFirstName());
		System.out.println("Customer Info:");
		System.out.println("   " + invoice.getCustomer().getName());
		System.out.println("   " + invoice.getCustomer().getPrimaryContact().getLastName() + "," + invoice.getCustomer().getPrimaryContact().getFirstName());
		System.out.println("   " + invoice.getCustomer().getAddress().getStreet());
		System.out.println("   " + invoice.getCustomer().getAddress().getCity() + "," + invoice.getCustomer().getAddress().getState() + " " + invoice.getCustomer().getAddress().getZip()
			+ " " + invoice.getCustomer().getAddress().getCountry());
		System.out.println("---------------------------------------------------------");
		System.out.printf("%-10s%-60s%-11s%-10s%n", "Code", "Item", "Fees", "Total");
		for (Map.Entry<Product, Integer> entry: invoice.Products.entrySet()) {
			if (entry.getKey() instanceof Equipment){
				Equipment E = (Equipment) entry.getKey();
				System.out.printf(INVOICE_FORMAT_STRING, E.getCode(), E.getName()
						+ " (" + entry.getValue() + " units @ $" + E.getPricePerUnit() + "/unit)", invoice.getFees(E), invoice.getTotal(E, entry.getValue()) );
			}
			if (entry.getKey() instanceof Consultation){
				Consultation C = (Consultation) entry.getKey();
				System.out.printf(INVOICE_FORMAT_STRING, C.getCode(), C.getName()
						+ " (" + entry.getValue() + " hours @ $" + C.getHourlyFee() + "/hour)", invoice.getFees(C), invoice.getTotal(C, entry.getValue()) );
			}
			if (entry.getKey() instanceof License) {
				License L = (License) entry.getKey();
				System.out.printf(INVOICE_FORMAT_STRING, L.getCode(), L.getName()
						+ " (" + entry.getValue() + " days @ $" + L.getAnnualLicenseFee() + "/yr)", invoice.getFees(L), invoice.getTotal(L, entry.getValue()) );
			}
		}
		System.out.printf("%90s%n", "====================");
		System.out.printf("%-70s$%-10.2f$%-10.2f%n", "SUB-TOTALS", invoice.getInvoiceFees(), invoice.getSubTotal());
		System.out.printf("%-81s$%-10.2f%n", "COMPLIANCE FEE", invoice.getComplianceFee());
		System.out.printf("%-81s$%-10.2f%n", "TAXES", invoice.getTaxes());
		System.out.printf("%-81s$%-10.2f%n%n%n", "TOTAL",invoice.getTotal());
		
	}
	
	public static void main(String[] args) {
		
		//This is just code to test out what has been done so far
		Person[] peeps = readPersons();
		writeXML(peeps);
		writeJSON(peeps);
		Customer[] customers = readCustomers(peeps);
		writeXML(customers);
		writeJSON(customers);
		Product[] products = readProducts(peeps);
		writeJSON(products);
		writeXML(products);
		Invoice[] invoicesTest = readInvoice(peeps, customers, products);
		printInvoiceExecSummary(invoicesTest);
		for (Invoice i: invoicesTest) {
			printInvoiceDetail(i);
		}
	}

}
