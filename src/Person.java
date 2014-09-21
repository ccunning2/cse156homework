
public class Person {
	private String personCode; //Alpha-numeric designation from old system
	private String firstName;
	private String lastName;
	private Address address;
	private String[] emails;
	
	public Person(String personCode, String name, Address address, String email) {
		this.personCode = personCode;
		String[] names = name.split(",");
		this.lastName = names[0];
		this.firstName = names[1];
		this.address = address;
		this.emails = email.split(",");
		
	}

	public String getPersonCode() {
		return personCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Address getAddress() {
		return address;
	}

	public String[] getEmails() {
		return emails;
	}
	
	
}
