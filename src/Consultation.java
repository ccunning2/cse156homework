
public class Consultation extends Product {
	private double hourlyFee;
	private Person consultant; //Refers to a particular person in persons.dat
	
	
	public Consultation(String code, String name, double hourlyFee) {
		super(name, code);
		this.hourlyFee = hourlyFee;
	}


	public double getHourlyFee() {
		return hourlyFee;
	}


	public void setHourlyFee(double hourlyFee) {
		this.hourlyFee = hourlyFee;
	}


	public Person getConsultant(){
		return this.consultant;
	}


	public void setConsultant(Person consultant){
		this.consultant = consultant;
	}
	
	
	
	

}
