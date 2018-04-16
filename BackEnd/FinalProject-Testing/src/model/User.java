package model;

public class User {
	public String username;
	public String imageUrl;
	public int numFollowers;
	
	public User(String username, String imageUrl, int numFollowers) {
		this.username = username;
		this.imageUrl = imageUrl;
		this.numFollowers = numFollowers;
	}
}
