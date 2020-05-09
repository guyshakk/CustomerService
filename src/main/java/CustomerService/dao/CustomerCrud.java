package CustomerService.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import CustomerService.data.Country;
import CustomerService.data.Customer;

public interface CustomerCrud extends PagingAndSortingRepository<Customer,String> {

	public List<Customer> findByLastLike (@Param("last") String last, Pageable page);
	
	public List<Customer> findByBirthdateLessThan (@Param("birthdate") LocalDate birthdate, Pageable page);
	
	public List<Customer> findByCountry (@Param("country") Country country, Pageable page);
}
