import java.io.*;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.*;  
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
//This will read .dat files, parse, create objects, and convert to XML/JSON
public class DataConverter {

	//Reads the Persons.dat file
	public static Person[] readPersons() {
		
		try {
		Scanner	s = new Scanner(new FileReader("data/Persons.dat"));
		
		int count = s.nextInt(); //Number of entries. 
		Person[] persons = new Person[count]; //Array to hold persons
		s.nextLine(); //Advance scanner to next line
		int iterator = 0; //Used to iterate in array creation
		while (s.hasNext()) {
			String inLine = s.nextLine(); //Make a string out of the next line
			String [] info;
			String emails; //Necessary in case of no emails
			info = inLine.split(";"); //Makes an array of of the string, delimited by ';'
			if (info.length < 4) { 
				emails = "No Email";
			} else {
				emails = info[3];
			}
			
			Address address = new Address(info[2]);
			
			persons[iterator] = new Person(info[0], info[1], address, emails);
			iterator++;
		}
		s.close();
		return persons;

		
		} catch (FileNotFoundException e) {
			System.out.println("File not found in readPersons()");
		}
		return null;
		
		
	}
	//Changed following method to take Persons database as input argument, to be used in instantiating primary contact person
	public static Customer[] readCustomers(Person[] persons) {  //Will read data from file and output to array. 
		try {
		Scanner	s = new Scanner(new FileReader("data/Customers.dat"));
		
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
			Scanner	s = new Scanner(new FileReader("data/Products.dat"));
			
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
					products[iterator] = new License(info[0], info[2],Double.parseDouble(info[3]), Double.parseDouble(info[4]));
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
			Scanner	s = new Scanner(new FileReader("data/Invoices.dat"));
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
								System.out.println("License here!");
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
		writeXML(invoicesTest);
	}

}
