package com.example.demo;

public class IncompatibleCountryDetailsException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public IncompatibleCountryDetailsException() {
	}

	public IncompatibleCountryDetailsException(String message) {
		super(message);
	}

	public IncompatibleCountryDetailsException(Throwable cause) {
		super(cause);
	}

	public IncompatibleCountryDetailsException(String message, Throwable cause) {
		super(message, cause);
	}
}
