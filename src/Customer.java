
public class Customer {
	private String type; //'G' or 'C' depending on government or corporate customer
	private String customerCode;
	private String name;
	private Address address;
	private String primaryContact;
	
	
	
	public Customer(String custCode, String type, String personCode, String name, Address address){
		this.customerCode = custCode;
		this.type = type;
		this.primaryContact = personCode;
		this.name = name;
		this.address = address;
		
	}
}
