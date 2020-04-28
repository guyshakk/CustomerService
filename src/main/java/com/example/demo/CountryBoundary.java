package com.example.demo;

public class CountryBoundary {

	private String countryCode;
	private String countryName;
	
	public CountryBoundary() {
	}
	
	public CountryBoundary(String countryCode, String countryName) {
		if (countryCode != null && countryName != null) {
			this.countryCode = countryCode;
			this.countryName = countryName;
		}
	}
	
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

	public Country toEntity() {
		Country entity = new Country();
		if (this.countryCode != null && this.countryName != null) { //Do we need both of them to not be null or just countryCode?
			entity.setCountryCode(this.countryCode);
			entity.setCountryName(this.countryName);
		}
		return entity;
	}
}
