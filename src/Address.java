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
}
