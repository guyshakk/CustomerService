package CustomerService.dao;

import org.springframework.data.repository.PagingAndSortingRepository; 

import CustomerService.data.Country;

public interface CountryCrud extends PagingAndSortingRepository<Country, String> {

}
