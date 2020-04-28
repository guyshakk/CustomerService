package com.example.demo;

public class CustomerAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public CustomerAlreadyExistsException() {
	}

	public CustomerAlreadyExistsException(String message) {
		super(message);
	}

	public CustomerAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public CustomerAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
