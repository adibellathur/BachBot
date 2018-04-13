package model;

public class Song {
	public String title;
	public String path;
	public String username;
	public int favorites;
	
	public Song(String title, String path, String username, int favorites) {
		this.title = title;
		this.path = path;
		this.username = username;
		this.favorites = favorites;
	}
}
