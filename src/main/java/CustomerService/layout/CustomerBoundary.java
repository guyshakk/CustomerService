package CustomerService.layout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import CustomerService.aop.IncompatibleCustomerDetailsException;
import CustomerService.data.Country;
import CustomerService.data.Customer;
import CustomerService.data.FullName;

public class CustomerBoundary {

	private String email;
	private FullName name;
	private String birthdate;
	private Country country;
	
	public CustomerBoundary() {
	}
	
	public CustomerBoundary(Customer entity) {
		if (entity.getBirthdate() != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String formattedString = entity.getBirthdate().format(formatter);
			this.setBirthdate(formattedString);
		}
		if (entity.getEmail() != null)
			this.setEmail(entity.getEmail());
		if (entity.getFirst() != null && entity.getLast() != null)
			this.setName(new FullName(entity.getFirst(), entity.getLast()));
		if (entity.getCountry() != null)
			this.setCountry(entity.getCountry());
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

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
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
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate dt;
		try {
			dt = LocalDate.parse(getBirthdate(), dtf);
			customer.setBirthdate(dt);
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
			throw new IncompatibleCustomerDetailsException();
		}
		if (this.getEmail() != null) {
			customer.setEmail(this.getEmail());
		}
		if (this.getName() != null) {
			customer.setFirst(this.getName().getFirst());
			customer.setLast(this.getName().getLast());
		}
		if (this.getCountry() != null) {
			customer.setCountry(this.getCountry());
		}
		return customer;
	}
}
