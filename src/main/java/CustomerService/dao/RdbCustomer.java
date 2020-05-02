package CustomerService.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import CustomerService.aop.CustomerAlreadyExistsException;
import CustomerService.aop.CustomerNotFoundException;
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
    @Transactional
	public Optional<Customer> getCustomerById(String email) {
            return this.customerCrud.findById(email);
	}

	@Override
    @Transactional
	public void updateCustomer(String email, Customer update) {
Customer c=this.customerCrud.findById(email).orElseThrow(
		()->new CustomerNotFoundException("customer not found exists with key: " + email));	
this.customerCrud.save(update);
	}

	@Override
	public void deleteAllCustomers() {
this.customerCrud.deleteAll();		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readCustomersByLastName(String lastName, int size, int page, String sortBy) {
		
	return this.customerCrud.findAll(PageRequest.of(page, size,Direction.ASC, sortBy)).getContent()
			.stream().filter(m -> (m.getLast()!=null&& !m.getLast().trim().isEmpty()))
			.filter(m ->m.getLast().toLowerCase().contains(lastName.toLowerCase())).collect(Collectors.toList());
	
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readCustomersByAge(float age, int size, int page, String sortBy) {
		int year =LocalDate.now().getYear();
		
		int y= (int) ( year-age);
  		List<Customer>x =this.customerCrud.findAll(PageRequest.of(page, size,Direction.ASC, sortBy)).getContent();
		List<Customer>r = new ArrayList<Customer>() ;
 
		for (Customer customer : x) {
			String []a = customer.getBirthdate().toInstant().toString().split("-");
int custYear=Integer.parseInt(a[0]);
		if(	custYear <= y)
			r.add(customer);
		}
		return r;
		}

	@Override	
	@Transactional(readOnly = true)
	public List<Customer> readCustomersByCountryCode(String countryCode, int size, int page, String sortBy) {
		
		return this.customerCrud.findAll(PageRequest.of(page, size,Direction.ASC, sortBy)).getContent()
				.stream().filter(m -> (m.getCountryCode()!=null&& !m.getCountryCode().trim().isEmpty()))
				.filter(m ->m.getCountryCode().toLowerCase().contains(countryCode.toLowerCase())).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readAllCustomers(int size, int page, String sortBy) {

		return this.customerCrud.findAll(PageRequest.of(page, size,Direction.ASC, sortBy)).getContent();
	}

	
	

}
