
import java.util.HashMap;
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
	public HashMap<Product, Integer> Products;  
	
	public Invoice(String code) {
		this.code = code;
		this.Products = new HashMap<Product, Integer>();
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
	
	public void addProduct(Product product, int quantity) {
		//quantity is quantity if equipment, billable hours if consultation, period if license
		this.Products.put(product, quantity); 
	}
	
	

	
}
