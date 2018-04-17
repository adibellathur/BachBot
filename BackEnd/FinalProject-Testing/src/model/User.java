package model;

public class User {
	public String username;
	public String imageUrl;
	public int numFollowers;
	public int userId;
	
	public User(String username, int userId, String imageUrl, int numFollowers) {
		this.username = username;
		this.userId = userId;
		this.imageUrl = imageUrl;
		this.numFollowers = numFollowers;
	}
}
