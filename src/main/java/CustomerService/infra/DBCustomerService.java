package CustomerService.infra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List; 
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import CustomerService.aop.CountryNotFoundException;
import CustomerService.aop.CustomerAlreadyExistsException;
import CustomerService.aop.CustomerNotFoundException;
import CustomerService.aop.IncompatibleCustomerDetailsException;
import CustomerService.aop.InvalidPaginationDataException;
import CustomerService.dao.CountryDao;
import CustomerService.dao.CustomerDao;
import CustomerService.data.Country;
import CustomerService.data.Customer;

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
		Country country = null;
		String custId = customer.getEmail();
		try {
			getCustomerById(custId);//check if customer already exists
			throw new CustomerAlreadyExistsException();
		} catch (CustomerNotFoundException e) {
		}
		try {
			country = getCountryByCode(customer.getCountry().getCode()); //check if country exists or should be created
		}
		catch (CountryNotFoundException e) {
			country = createCountry(customer.getCountry());
		}
		customer.setCountry(country);
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
		if (update.getCountry() != null) {
			Country country;
			try {
				country = getCountryByCode(update.getCountry().getCode());
				if (!country.getCode().equals(currentCustomer.getCountry().getCode())) {
					currentCustomer.setCountry(country);
				}
			} catch (CountryNotFoundException e) {
			}
		}
		if (update.getFirst() != null && !update.getFirst().trim().isEmpty())
			currentCustomer.setFirst(update.getFirst());
		if (update.getLast() != null && !update.getLast().trim().isEmpty())
			currentCustomer.setLast(update.getLast());
		this.customerDao.updateCustomer(email, currentCustomer);
	}

	@Override
	@Transactional
	public void updateCountry(String code, Country country) {
		Country currentCountry = getCountryByCode(code);
		if (country.getCountryName() != null && !country.getCountryName().trim().isEmpty()) {
			currentCountry.setCountryName(country.getCountryName());
		}
		this.countryDao.updateCountry(code, currentCountry);
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
	public List<Customer> readCustomersByAge(int age, int size, int page) {
		validatePagination(size, page);
		return this.customerDao
				.readCustomersByAge(age, size, page, this.defaultSortBy);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Customer> readCustomersByCountryCode(String code, int size, int page) {
		validatePagination(size, page);
		Country country;
		try {
			country = getCountryByCode(code);
		} catch (CountryNotFoundException e) {
			return new ArrayList<Customer>();
		}
		return this.customerDao
				.readCustomersByCountry(country, size, page, this.defaultSortBy);
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
	private Country getCountryByCode(String code) {
		Optional<Country> country = this.countryDao
				.getCountryById(code);
		if (country.isPresent()) {
			return country.get();
		}
		else {
			throw new CountryNotFoundException();
		}
	}
	
	private boolean validateCustomer(Customer customer) {
		return customer.getBirthdate() != null &&
				customer.getBirthdate().isBefore(LocalDate.now()) &&
				customer.getBirthdate().isAfter(LocalDate.now().minusYears(150)) && //we assume no person is older than 150
				customer.getCountry() != null &&
				customer.getCountry().getCode() != null &&
				customer.getCountry().getCode().matches("^[A-Z][A-Z]$") && //must be two uppercase letters
				customer.getCountry().getCountryName() != null &&
				!customer.getCountry().getCountryName().trim().isEmpty() &&
				validateEmail(customer.getEmail()) &&
				customer.getFirst() != null &&
				!customer.getFirst().trim().isEmpty() &&
				customer.getLast() != null &&
				!customer.getLast().trim().isEmpty();
	}

	private boolean validateEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{1,}$"; 
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
