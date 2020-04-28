package com.example.demo;

import java.util.Optional;

public interface CountryDao {

	public Country createCountry (Country country);
	
	public Optional<Country> getCountryById (String countryCode);
	
	public void updateCountry (String countryCode, Country country);
}
