package CustomerService.data;

import org.springframework.data.annotation.Id;  
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Countries")
public class Country {

	private String code;
	private String countryName;
	
	public Country() {
	}

	public Country(String code, String countryName) {
		this.code = code;
		this.countryName = countryName;
	}

	@Id
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
}
