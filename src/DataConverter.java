import java.io.*;
import java.util.Scanner;

import com.thoughtworks.xstream.XStream;  
//This will read .dat files, parse, create objects, and convert to XML/JSON
public class DataConverter {

	//Reads the customers.dat file
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public static Customer[] readCustomers() {  //Will read data from file and output to array
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
			
			customers[iterator] = new Customer(info[0], info[1], info[2], info[3], address);
			iterator++;
		}
		s.close();
		return customers;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	public static void writeXML(Object[] objects) {
		XStream xstream = new XStream();
//		String xml = xstream.toXML(objects[0]);
		String objType = objects[0].getClass().getCanonicalName(); //Returns the name of objects we are using with
		String filename = objType + ".xml"; //Filename for output
		File file = new File(filename);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			PrintWriter writer = new PrintWriter(filename);
			for (Object o : objects) {
				xstream.toXML(o, writer);
				writer.println();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}
	
	public static void main(String[] args) {
		
		//This is just code to test out what has been done so far
		Person[] peeps = readPersons();
		writeXML(peeps);
		Customer[] customers = readCustomers();
		writeXML(customers);
		

	}

}
