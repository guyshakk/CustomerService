package CustomerService.dao;

import java.util.List; 
import java.util.Optional;

import CustomerService.data.Country;
import CustomerService.data.Customer;

public interface CustomerDao {

	public Customer create (Customer customer);
	
	public Optional<Customer> getCustomerById (String email);
	
	public void updateCustomer (String email, Customer update);

	public void deleteAllCustomers ();
	
	public List<Customer> readCustomersByLastName (String lastName, int size, int page, String sortBy);
	
	public List<Customer> readCustomersByAge (int age, int size, int page, String sortBy);
	
	public List<Customer> readCustomersByCountry (Country country, int size, int page, String sortBy);

	public List<Customer> readAllCustomers(int size, int page, String sortBy);
}
