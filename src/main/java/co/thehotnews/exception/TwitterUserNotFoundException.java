package co.thehotnews.exception;

public class TwitterUserNotFoundException extends Exception {
	
	private static final long serialVersionUID = 3187584767436313223L;
	
	private String name;

	public TwitterUserNotFoundException(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
