package com.example.demo;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

	public Customer create (Customer customer);
	
	public Optional<Customer> getCustomerById (String email);
	
	public void updateCustomer (String email, Customer update);

	public void deleteAllCustomers ();
	
	public List<Customer> readCustomersByLastName (String lastName, int size, int page, String sortBy);
	
	public List<Customer> readCustomersByAge (String age, int size, int page, String sortBy);
	
	public List<Customer> readCustomersByCountryCode (String countryCode, int size, int page, String sortBy);

	public List<Customer> readAllCustomers(int size, int page, String sortBy);
}
