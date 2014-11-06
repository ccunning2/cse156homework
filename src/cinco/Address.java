package cinco;
//Container for address information
public class Address {
	private String Street;
	private String City;
	private String State;
	private String Zip;
	private String Country;
	
	public Address(String address) {
		String[] fullAddress = address.split(",");
		this.Street = fullAddress[0];
		this.City = fullAddress[1];
		this.State = fullAddress[2];
		this.Zip = fullAddress[3];
		this.Country = fullAddress[4];
	}
	
	public Address(String street, String city, String state, String zip, String country) {
		this.Street=street;
		this.City=city;
		this.State=state;
		this.Zip=zip;
		this.Country=country;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getZip() {
		return Zip;
	}

	public void setZip(String zip) {
		Zip = zip;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}
	
}
