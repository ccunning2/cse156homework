
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



	public String getPrimaryContact() {
		return primaryContact;
	}



	public void setPrimaryContact(String primaryContact) {
		this.primaryContact = primaryContact;
	}
	
}
