package CustomerService.aop;

public class CountryAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public CountryAlreadyExistsException() {
	}

	public CountryAlreadyExistsException(String message) {
		super(message);
	}

	public CountryAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public CountryAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
