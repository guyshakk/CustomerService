package com.example.demo;

public class CountryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public CountryNotFoundException() {
	}

	public CountryNotFoundException(String message) {
		super(message);
	}

	public CountryNotFoundException(Throwable cause) {
		super(cause);
	}

	public CountryNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
