package CustomerService.data;

import org.springframework.data.annotation.Id;  
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Countries")
public class Country {

	private String countryCode;
	private String countryName;
	
	public Country() {
	}

	public Country(String countryCode, String countryName) {
		this.countryCode = countryCode;
		this.countryName = countryName;
	}

	@Id
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
}
