package CustomerService.layout;

import CustomerService.data.Country;

public class CountryBoundary {

	private String code;
	private String countryName;
	
	public CountryBoundary() {
	}
	
	public CountryBoundary(String code, String countryName) {
		if (code != null && countryName != null) {
			this.code = code;
			this.countryName = countryName;
		}
	}
	
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

	public Country toEntity() {
		Country entity = new Country();
		if (this.code != null && this.countryName != null) {
			entity.setCode(this.code);
			entity.setCountryName(this.countryName);
		}
		return entity;
	}
}
