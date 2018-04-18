package model;

public class ProfileDetails {
	public int followers, following, songs;
	public String imageUrl;
	
	public ProfileDetails(int followers, int following, int songs, String imageUrl) {
		this.followers = followers;
		this.following = following;
		this.songs = songs;
		this.imageUrl = imageUrl;
	}
}
