package cinco;

public class Customer {
	private String type; //'G' or 'C' depending on government or corporate customer
	private String customerCode;
	private String name;
	private Address address;
	private Person primaryContact;
	
	
	
	public Customer(String custCode, String type, String name, Address address){
		this.customerCode = custCode;
		this.type = type;
		this.name = name;
		this.address = address;
		
	}
	
	public Customer(String custCode, String type, String name, Address address, Person primaryContact){
		this.customerCode = custCode;
		this.type = type;
		this.name = name;
		this.address = address;
		this.primaryContact = primaryContact;
	}
	
	public void setPrimaryContact(Person primaryContact){
		this.primaryContact = primaryContact;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getCustomerCode() {
		return customerCode;
	}



	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Address getAddress() {
		return address;
	}



	public void setAddress(Address address) {
		this.address = address;
	}



	public Person getPrimaryContact() {
		return primaryContact;
	}



	
}
