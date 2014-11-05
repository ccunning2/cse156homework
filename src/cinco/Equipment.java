package cinco;

public class Equipment extends Product {
	private double pricePerUnit;
	
	
	
	
	
	public Equipment(String code, String name, double pricePerUnit){
		super(name,code);
		this.pricePerUnit = pricePerUnit;
	}

	public double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
}

	

