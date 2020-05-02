package CustomerService.dao;

import java.util.Date; 
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import CustomerService.data.Customer;

public interface CustomerCrud extends PagingAndSortingRepository<Customer,String> {

	public List<Customer> findCustomersByLast (@Param("pattern") String lastName,Pageable page);
	
	public List<Customer> findCustomersByBirthdate (@Param("pattern") Date birthdate, Pageable page);
	
	public List<Customer> findCustomersByCountryCode (@Param("pattern") String countryCode, Pageable page);
}
