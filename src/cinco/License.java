package cinco;

public class License extends Product {
	private double annualLicenseFee;
	private double serviceFee;
	
	
	public License(String code, String name, double annualLicenseFee, double serviceFee) {
		super(name, code);
		this.annualLicenseFee = annualLicenseFee;
		this.serviceFee = serviceFee;
	}


	public double getAnnualLicenseFee() {
		return annualLicenseFee;
	}


	public void setAnnualLicenseFee(double annualLicenseFee) {
		this.annualLicenseFee = annualLicenseFee;
	}


	public double getServiceFee() {
		return serviceFee;
	}


	public void setServiceFee(double serviceFee) {
		this.serviceFee = serviceFee;
	}
	
	
	
	
}
