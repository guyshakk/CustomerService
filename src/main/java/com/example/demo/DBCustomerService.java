package com.example.demo;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DBCustomerService implements CustomerService {

	private CustomerDao customerDao;
	private CountryDao countryDao;
	private final String defaultSortBy = "email";
	
	@Autowired
	public DBCustomerService(CustomerDao customerDao, CountryDao countryDao) {
		this.customerDao = customerDao;
		this.countryDao = countryDao;
	}
	
	@Override
	@Transactional
	public Customer createCustomer(Customer customer) {
		if (!validateCustomer(customer))
			throw new IncompatibleCustomerDetailsException(); //validate all customer details
		Customer cust = null;
		Country country = null;
		String custId = customer.getEmail();
		try {
			cust = getCustomerById(custId); //check if customer already exists
		} catch (CustomerNotFoundException e) {
		}
		if (cust != null)
			throw new CustomerAlreadyExistsException();
		try {
			country = getCountryByCode(customer.getCountryCode()); //check if country exists or should be created
		}
		catch (CountryNotFoundException e) {
			country = createCountry(new Country(customer.getCountryCode(), customer.getCountryName()));
		}
		customer.setCountryName(country.getCountryName()); //override supplied country's name
		Customer newCustomer = this.customerDao.create(customer); //create new customer
		return newCustomer;
	}

	@Override
	@Transactional(readOnly = true)
	public Customer getCustomerById(String email) {
		Optional<Customer> customer = this.customerDao.getCustomerById(email);
		if (customer.isPresent()) {
			return customer.get();
		}
		else {
			throw new CustomerNotFoundException();
		}
	}

	@Override
	@Transactional
	public void updateCustomer(String email, Customer update) {
		Customer currentCustomer = getCustomerById(email);
		if (update.getBirthdate() != null)
			currentCustomer.setBirthdate(update.getBirthdate());
		if (update.getCountryCode() != null) {
			Country country = getCountryByCode(update.getCountryCode()); //This could lead to countryNotFoundException because creation of new country is not allowed here
			if (country != null && !country.getCountryCode().equals(currentCustomer.getCountryCode())) {
				currentCustomer.setCountryCode(country.getCountryCode());
				currentCustomer.setCountryName(country.getCountryName());
			}
		}
		if (update.getFirst() != null && update.getFirst().trim().length() > 0)
			currentCustomer.setFirst(update.getFirst());
		if (update.getLast() != null && update.getLast().trim().length() > 0)
			currentCustomer.setLast(update.getLast());
		this.customerDao.updateCustomer(email, currentCustomer);
	}

	@Override
	@Transactional
	public void updateCountry(String countryCode, Country country) {
		Country currentCountry = getCountryByCode(countryCode);
		if (country.getCountryName() != null && country.getCountryName().trim().length() > 0) {
			currentCountry.setCountryName(country.getCountryName());
		}
		this.countryDao.updateCountry(countryCode, currentCountry);
	}

	@Override
	@Transactional
	public void deleteAllCustomers() {
		this.customerDao
			.deleteAllCustomers();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readCustomersByLastName(String lastName, int size, int page) {
		validatePagination(size, page);
		return this.customerDao
				.readCustomersByLastName(lastName, size, page, this.defaultSortBy);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readCustomersByAge(String age, int size, int page) {
		validatePagination(size, page);
		return this.customerDao
				.readCustomersByAge(age, size, page, this.defaultSortBy);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readCustomersByCountryCode(String countryCode, int size, int page) {
		validatePagination(size, page);
		return this.customerDao
				.readCustomersByCountryCode(countryCode, size, page, this.defaultSortBy);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readAllCustomers(int size, int page) {
		validatePagination(size, page);
		return this.customerDao
				.readAllCustomers(size, page, this.defaultSortBy);
	}

	@Transactional
	private Country createCountry(Country country) {
		return this.countryDao.createCountry(country);
	}
	
	@Transactional(readOnly = true)
	private Country getCountryByCode(String countryCode) {
		Optional<Country> country = this.countryDao
				.getCountryById(countryCode);
		if (country.isPresent()) {
			return country.get();
		}
		else {
			throw new CountryNotFoundException();
		}
	}
	
	private boolean validateCustomer(Customer customer) {
		return customer.getBirthdate() != null &&
				customer.getCountryCode() != null &&
				customer.getCountryCode().matches("^[A-Z][A-Z]$") && //must be two uppercase letters
				customer.getCountryName() != null &&
				customer.getCountryName().trim().length() > 0 &&
				validateEmail(customer.getEmail()) &&
				customer.getFirst() != null &&
				customer.getFirst().trim().length() > 0 &&
				customer.getLast() != null &&
				customer.getLast().trim().length() > 0;
	}

	private boolean validateEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{2,7}$"; 
		Pattern pat = Pattern.compile(emailRegex); 
		if (email == null) 
			return false; 
		return pat.matcher(email).matches();
	}
	
	private void validatePagination(int size, int page) {
		if (page < 0 || size < 1 || page > Integer.MAX_VALUE || size > 100) {
			throw new InvalidPaginationDataException();
		}
		
	}

}
