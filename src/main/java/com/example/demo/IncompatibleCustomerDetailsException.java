package com.example.demo;

public class IncompatibleCustomerDetailsException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public IncompatibleCustomerDetailsException() {
	}

	public IncompatibleCustomerDetailsException(String message) {
		super(message);
	}

	public IncompatibleCustomerDetailsException(Throwable cause) {
		super(cause);
	}

	public IncompatibleCustomerDetailsException(String message, Throwable cause) {
		super(message, cause);
	}
}
