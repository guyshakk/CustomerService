package CustomerService.layout;

import java.util.Date; 

import CustomerService.data.Country;
import CustomerService.data.Customer;
import CustomerService.data.FullName;

public class CustomerBoundary {

	private String email;
	private FullName name;
	private Date birthdate;
	private Country country;
	
	public CustomerBoundary() {
	}
	
	public CustomerBoundary(Customer entity) {
		if (entity.getBirthdate() != null)
			this.setBirthdate(entity.getBirthdate());
		if (entity.getEmail() != null)
			this.setEmail(entity.getEmail());
		if (entity.getFirst() != null && entity.getLast() != null)
			this.setName(new FullName(entity.getFirst(), entity.getLast()));
		if (entity.getCountryCode() != null && entity.getCountryName() != null) //should we require conuntryName not to be null??
			this.setCountry(new Country(entity.getCountryCode(), entity.getCountryName()));
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public FullName getName() {
		return name;
	}

	public void setName(FullName name) {
		this.name = name;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
	public Customer toEntity() {
		Customer customer = new Customer();
		customer.setBirthdate(this.getBirthdate());
		if (this.getEmail() != null) {
			customer.setEmail(this.getEmail());
		}
		if (this.getName() != null) {
			customer.setFirst(this.getName().getFirst());
			customer.setLast(this.getName().getLast());
		}
		if (this.getCountry() != null) {
			customer.setCountryCode(this.getCountry().getCountryCode());
			customer.setCountryName(this.getCountry().getCountryName());
		}
		return customer;
	}
}
