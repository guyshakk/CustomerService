package com.example.demo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Customers")
public class Customer {

	private String email;
	private String first;
	private String last;
	private Date birthdate;
	private String countryCode;
	private String countryName;
	
	public Customer() {
	}

	public Customer(String email, String first, String last, Date birthdate, String countryCode,
			String countryName) {
		this.email = email;
		this.first = first;
		this.last = last;
		this.birthdate = birthdate;
		this.countryCode = countryCode;
		this.countryName = countryName;
	}

	@Id
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
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
}
