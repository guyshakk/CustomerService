package CustomerService.dao;

import java.util.Optional; 

import CustomerService.data.Country;

public interface CountryDao {

	public Country createCountry (Country country);
	
	public Optional<Country> getCountryById (String countryCode);
	
	public void updateCountry (String countryCode, Country country);
}
