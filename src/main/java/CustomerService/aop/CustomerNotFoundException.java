package CustomerService.aop;

public class CustomerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public CustomerNotFoundException() {
	}

	public CustomerNotFoundException(String message) {
		super(message);
	}

	public CustomerNotFoundException(Throwable cause) {
		super(cause);
	}

	public CustomerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
