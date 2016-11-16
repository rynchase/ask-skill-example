package co.thehotnews.twitter;

public class TwitterUser {
	String name;
	String handle;

	public TwitterUser(String name, String handle) {
		this.name = name;
		this.handle = handle;
	}

	public String getName() {
		return name;
	}

	public String getHandle() {
		return handle;
	}

}