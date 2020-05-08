package CustomerService.dao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import CustomerService.aop.CustomerAlreadyExistsException;
import CustomerService.aop.CustomerNotFoundException;
import CustomerService.data.Country;
import CustomerService.data.Customer;

@Repository
public class RdbCustomer implements CustomerDao{

	private CustomerCrud customerCrud;
	
	@Autowired
	public RdbCustomer(CustomerCrud customerCrud) {
		super();
		this.customerCrud=customerCrud;
	}
	
	@Override
	public Customer create(Customer customer) {
		if (!this.customerCrud.existsById(customer.getEmail())) {
            return this.customerCrud.save(customer);
        } else {
            throw new CustomerAlreadyExistsException("Country already exists with key: " + customer.getEmail());
        }
	}

	@Override
    @Transactional(readOnly = true)
	public Optional<Customer> getCustomerById(String email) {
		return this.customerCrud.findById(email);
	}

	@Override
    @Transactional
	public void updateCustomer(String email, Customer update) {
		this.customerCrud.findById(email).orElseThrow(
				()->new CustomerNotFoundException("Customer not found exists with key: " + email));	
		this.customerCrud.save(update);
	}

	@Override
	@Transactional
	public void deleteAllCustomers() {
		this.customerCrud.deleteAll();		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readCustomersByLastName(String lastName, int size, int page, String sortBy) {
		return this.customerCrud
				.findByLastLike(lastName, PageRequest.of(page, size ,Direction.ASC, sortBy));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readCustomersByAge(int age, int size, int page, String sortBy) {
		LocalDate d = LocalDate.now().minus(age, ChronoUnit.YEARS);
		return this.customerCrud
				.findByBirthdateLessThan(d, PageRequest.of(page, size, Direction.ASC, sortBy));
	}

	@Override	
	@Transactional(readOnly = true)
	public List<Customer> readCustomersByCountry(Country country, int size, int page, String sortBy) {
		
		return this.customerCrud
				.findByCountry(country, PageRequest.of(page, size, Direction.ASC, sortBy));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readAllCustomers(int size, int page, String sortBy) {
		return this.customerCrud.findAll(PageRequest.of(page, size,Direction.ASC, sortBy)).getContent();
	}
}
