package CustomerService.dao;

import java.util.Optional;    

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import CustomerService.aop.CountryAlreadyExistsException;
import CustomerService.aop.CountryNotFoundException;
import CustomerService.data.Country;

@Repository
public class RdbCounrty implements CountryDao {
	
	private CountryCrud counrtyCrud;
	
	@Autowired
	public RdbCounrty(CountryCrud counrtyCrud) {
	   super();
	   this.counrtyCrud = counrtyCrud;
	}

	@Override
	@Transactional
	public Country createCountry(Country country) {
		if (!this.counrtyCrud.existsById(country.getCode())) {
	        return this.counrtyCrud.save(country);
	    } else {
	        throw new CountryAlreadyExistsException("Country already exists with key: " + country.getCode());
	    }
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Country> getCountryById(String code) {
		return this.counrtyCrud.findById(code);    
	}
	
	@Override
	@Transactional
	public void updateCountry(String code, Country country) {
		this.counrtyCrud.findById(code).orElseThrow(
			()->new CountryNotFoundException("Country does not exist with key: " + code));	
		this.counrtyCrud.save(country);	
	}
}