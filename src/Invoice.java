

import java.util.LinkedHashMap;
import java.util.Map;
/*
 * Container class for invoices. ?
 */

public class Invoice {
	/*An alpha-numeric identifier used by the old system that uniquely
	identifies the invoice*/
	private String code; 
	//Customer buying the product(s)
	private Customer customer;
	//Salesperson that made the sale
	private Person salesPerson;
	//Keys will be products(Strings), values will be a cost multiplier (Time period for License or Consultation, quantity for equipment)
	public LinkedHashMap<Product, Integer> Products;  
	
	public Invoice(String code) {
		this.code = code;
		this.Products = new LinkedHashMap<Product, Integer>();
	}
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Person getSalesPerson() {
		return salesPerson;
	}
	public void setSalesPerson(Person salesPerson) {
		this.salesPerson = salesPerson;
	}
	/*
	 * Returns the total for a given product and quantity
	 */
	public double getTotal(Product product, int quantity) {
		double total;
		if (product instanceof Equipment) {
			Equipment p = (Equipment) product;
			total = quantity * p.getPricePerUnit();
			return total;
		} else if (product instanceof Consultation) {
			Consultation p = (Consultation) product;
			total = quantity * p.getHourlyFee();
			return total;
		} else {
			License p = (License) product;
			total = quantity/365 * p.getAnnualLicenseFee();
			return total;
		}
	}
	
	/*
	 * Returns complete subtotal for a particular invoice
	 */
	public double getSubTotal(){
		double subTotal = 0;
		for (Map.Entry<Product, Integer> entry: this.Products.entrySet()) {
			if (entry.getKey() instanceof License) {
				License L = (License) entry.getKey();
				subTotal += L.getAnnualLicenseFee() * entry.getValue() / 365;
			} else if(entry.getKey() instanceof Equipment) {
				Equipment E = (Equipment) entry.getKey();
				subTotal += E.getPricePerUnit() * entry.getValue();
			} else {
				Consultation C = (Consultation) entry.getKey();
				subTotal += C.getHourlyFee() * entry.getValue();
			}
		}
		return subTotal;
	}
	/*
	 * Returns the Fees associated with a given product
	 */
	public double getFees(Product product) {
		double fees;
		if (product instanceof Equipment) {
			fees = 0;
			return fees;
		} else if (product instanceof Consultation) {
			fees = 150;
			return fees;
		} else { //Product must be license
			License p = (License) product;
			fees = p.getServiceFee();
			return fees;
		}
	}
	/*
	 * Returns the total fees for a particular invoice
	 */
	public double getInvoiceFees() {
		double invoiceFees = 0;
		for (Map.Entry<Product, Integer> entry: this.Products.entrySet()) {
			if (entry.getKey() instanceof License) {
				License L = (License) entry.getKey();
				invoiceFees += getFees(L);
			} else if(entry.getKey() instanceof Equipment) {
				Equipment E = (Equipment) entry.getKey();
				invoiceFees += getFees(E); //Will be zero anyways
			} else {
				Consultation C = (Consultation) entry.getKey();
				invoiceFees += getFees(C);
			}
		}
		return invoiceFees;
		
	}
	
	public double getTaxes() {
		double taxes = 0;
		if(this.customer.getType().equalsIgnoreCase("G")) {
			taxes = 0;
			return taxes;
		} else {
			for (Map.Entry<Product, Integer> entry: this.Products.entrySet()) {
				if (entry.getKey() instanceof License) {
					License L = (License) entry.getKey();
					taxes += ((L.getAnnualLicenseFee() * entry.getValue() / 365) * .0425);
				} else if(entry.getKey() instanceof Equipment) {
					Equipment E = (Equipment) entry.getKey();
					taxes += (E.getPricePerUnit() * entry.getValue() * .07);
				} else {
					Consultation C = (Consultation) entry.getKey();
					taxes += (C.getHourlyFee() * entry.getValue() * .0425);
				}
			}
		} return taxes;
	}
	
	public double getComplianceFee(){
		double fee = 0.00;
		if (this.getCustomer().getType().equalsIgnoreCase("G")){
			fee = 150.00;
		}
		return fee;
	}
	
	/*
	 * Returns the total charges for a particular invoice
	 */
	public double getTotal() {
		double total = 0;
		total = this.getSubTotal() + this.getInvoiceFees() + this.getTaxes();
		return total;
	}
	
	public void addProduct(Product product, int quantity) {
		//quantity is quantity if equipment, billable hours if consultation, period if license
		this.Products.put(product, quantity); 
	}
	
	public void printInvoice() {
		
	}
	

	
}
