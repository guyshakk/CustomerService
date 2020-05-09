package CustomerService.data;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection ="Customers")
public class Customer {
	@Id
	private String email;
	private String first;
	private String last;
	private LocalDate birthdate;
	@DBRef
	private Country country;
	
	public Customer() {
	}

	public Customer(String email, String first, String last, LocalDate birthdate, Country country) {
		this.email = email;
		this.first = first;
		this.last = last;
		this.birthdate = birthdate;
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	@Temporal(TemporalType.DATE)
	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}	
}
