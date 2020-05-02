package CustomerService.layout;

import java.util.Collections;  
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import CustomerService.aop.CountryNotFoundException;
import CustomerService.aop.CustomerAlreadyExistsException;
import CustomerService.aop.CustomerNotFoundException;
import CustomerService.aop.IncompatibleCountryDetailsException;
import CustomerService.aop.IncompatibleCustomerDetailsException;
import CustomerService.aop.IncompatibleSearchInputException;
import CustomerService.aop.InvalidPaginationDataException;
import CustomerService. aop.TooManyRequestParametersException;
import CustomerService.data.Country;
import CustomerService.data.CriteriaType;
import CustomerService.data.Customer;
import CustomerService.infra.CustomerService;

@RestController
public class CustomerController {

	private final String baseUrl="/customers";
	private CustomerService customerService;
	
	@Autowired
	public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}
	
	@RequestMapping(path = baseUrl, 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomerBoundary createCustomer (@RequestBody CustomerBoundary customer) {
		return new CustomerBoundary(
				this.customerService
					.createCustomer(customer.toEntity()));
	}

	@RequestMapping(
			path = baseUrl+"/{email}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomerBoundary getCustomerById(@PathVariable("email") String email) {
		return new CustomerBoundary(
				this.customerService
					.getCustomerById(email));
	}
	@RequestMapping(
			path = baseUrl+"/{email}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateCustomer (
			@PathVariable("email") String email, 
			@RequestBody Customer update) {
		this.customerService
			.updateCustomer(email, update);
	}

	
	@RequestMapping(
			path = baseUrl,
			method = RequestMethod.DELETE)
	public void deleteAllCustomers () {
		this.customerService
			.deleteAllCustomers();
	}

	@RequestMapping(
			path = baseUrl, 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public  CustomerBoundary[] getCustomersBy (@RequestParam(name="byLastName", required = false, defaultValue = "") String byLastName,
			@RequestParam(name="byAgeGreaterThan", required = false, defaultValue = "0") float byAgeGreaterThan,
			@RequestParam(name="byCountryCode", required = false, defaultValue = "") String byCountryCode,
			@RequestParam(name="size", required = false, defaultValue = "10") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page)  {
 		String s;
		if(byAgeGreaterThan==0)
 			s="";
		else s=byAgeGreaterThan+"";
		CriteriaType criteria = checkRequestValidity(s, byCountryCode, byLastName);
		List<Customer> customers;
		
		if (criteria.equals(CriteriaType.byLastName)) {
			customers = this.customerService.readCustomersByLastName(byLastName, size, page);
		}
		else if (criteria.equals(CriteriaType.byAgeGreaterThan)) {
			customers = this.customerService.readCustomersByAge(byAgeGreaterThan, size, page);
		}
		else if (criteria.equals(CriteriaType.byCountryCode)) {
			customers = this.customerService.readCustomersByCountryCode(byCountryCode, size, page);
		}
		else {
			customers = this.customerService.readAllCustomers(size, page);
		}
		return customers
				.stream()
				.map(cust -> (new CustomerBoundary(cust)))
				.collect(Collectors.toList())
				.toArray(new CustomerBoundary[0]);
	}
	
	@RequestMapping(
			path = "/countries",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateCountry (
			@PathVariable("countryCode") String countryCode, 
			@RequestBody Country update) {
		this.customerService
			.updateCountry(countryCode, update);
	}

	private CriteriaType checkRequestValidity(String byAgeGreaterThan, String byCountryCode, String byLastName) {
		CriteriaType selectedCriteria = null;
		if (byAgeGreaterThan.length() > 0) {
			selectedCriteria = CriteriaType.byAgeGreaterThan;
			if (byAgeGreaterThan.trim().length() == 0)
				throw new IncompatibleSearchInputException();
		}
		if (byCountryCode.length() > 0) {
			if (selectedCriteria != null)
				throw new TooManyRequestParametersException();
			else
				selectedCriteria = CriteriaType.byCountryCode;
			if (byCountryCode.trim().length() == 0)
				throw new IncompatibleSearchInputException();
		}
		if (byLastName.length() > 0) {
			if (selectedCriteria != null)
				throw new TooManyRequestParametersException();
			else
				selectedCriteria = CriteriaType.byLastName;
			if (byLastName.trim().length() == 0)
				throw new IncompatibleSearchInputException();
		}
		if (selectedCriteria == null)
			selectedCriteria = CriteriaType.NONE;
		return selectedCriteria;
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, Object> handleError (CustomerNotFoundException e){
		String message = e.getMessage();
		if (message == null || message.trim().length() == 0) {
			message = "Customer not found";
		}
		return Collections.singletonMap("error", message);
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, Object> handleError (CountryNotFoundException e){
		String message = e.getMessage();
		if (message == null || message.trim().length() == 0) {
			message = "Country not found";
		}
		return Collections.singletonMap("error", message);
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> handleError (CustomerAlreadyExistsException e){
		String message = e.getMessage();
		if (message == null || message.trim().length() == 0) {
			message = "Customer already exists";
		}
		return Collections.singletonMap("error", message);
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleError (IncompatibleCountryDetailsException e){
		String message = e.getMessage();
		if (message == null || message.trim().length() == 0) {
			message = "Country details supplied are incorrect";
		}
		return Collections.singletonMap("error", message);
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleError (IncompatibleCustomerDetailsException e){
		String message = e.getMessage();
		if (message == null || message.trim().length() == 0) {
			message = "Customers details supplied are incorrect";
		}
		return Collections.singletonMap("error", message);
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleError (IncompatibleSearchInputException e){
		String message = e.getMessage();
		if (message == null || message.trim().length() == 0) {
			message = "Search details supplied are incorrect";
		}
		return Collections.singletonMap("error", message);
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleError (TooManyRequestParametersException e){
		String message = e.getMessage();
		if (message == null || message.trim().length() == 0) {
			message = "Too many request parameters supplied";
		}
		return Collections.singletonMap("error", message);
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleError (InvalidPaginationDataException e){
		String message = e.getMessage();
		if (message == null || message.trim().length() == 0) {
			message = "Invalid pagination data. Please supply page bigger than 0 and size between 0 and 100";
		}
		return Collections.singletonMap("error", message);
	}
}
