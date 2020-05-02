package CustomerService.dao;

import java.util.Optional;    

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import CustomerService.aop.CustomerAlreadyExistsException;
import CustomerService.aop.CustomerNotFoundException;
import CustomerService.data.Country;

@Repository
public class RdbCounrty implements CountryDao {
private CountryCrud counrtyCrud;


@Autowired
   public RdbCounrty(CountryCrud counrtyCrud) {
	   super();
this.counrtyCrud= counrtyCrud;
}


@Override
public Country createCountry(Country country) {
	if (!this.counrtyCrud.existsById(country.getCountryCode())) {
        return this.counrtyCrud.save(country);
    } else {
        throw new CustomerAlreadyExistsException("Country already exists with key: " + country.getCountryCode());
    }
}


@Override
public Optional<Country> getCountryById(String countryCode) {
        return this.counrtyCrud.findById(countryCode);
    
}


@Override
public void updateCountry(String countryCode, Country country) {
	Country c=this.counrtyCrud.findById(country.getCountryCode()).orElseThrow(
			()->new CustomerNotFoundException("Country not  exists with key: " + country.getCountryCode()));	
	this.counrtyCrud.save(country);	
}
}
