package CustomerService.infra;

import java.util.List; 

import CustomerService.data.Country;
import CustomerService.data.Customer;

public interface CustomerService {

	public Customer createCustomer (Customer customer);
	
	public Customer getCustomerById (String email);
	
	public void updateCustomer (String email, Customer update);
	
	public void updateCountry (String countryCode, Country country);

	public void deleteAllCustomers ();
	
	public List<Customer> readCustomersByLastName (String lastName, int size, int page);
	
	public List<Customer> readCustomersByAge (float age, int size, int page);
	
	public List<Customer> readCustomersByCountryCode (String countryCode, int size, int page);

	public List<Customer> readAllCustomers(int size, int page);
}
