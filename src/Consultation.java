
public class Consultation extends Product {
	private double hourlyFee;
	private String consultantPersonCode; //Refers to a particular person in persons.dat
	
	
	public Consultation(String code, String name, String consultantPersonCode, double hourlyFee) {
		super(name, code);
		this.hourlyFee = hourlyFee;
		this.consultantPersonCode = consultantPersonCode;
	}


	public double getHourlyFee() {
		return hourlyFee;
	}


	public void setHourlyFee(double hourlyFee) {
		this.hourlyFee = hourlyFee;
	}


	public String getConsultantPersonCode() {
		return consultantPersonCode;
	}


	public void setConsultantPersonCode(String consultantPersonCode) {
		this.consultantPersonCode = consultantPersonCode;
	}
	
	
	
	

}
