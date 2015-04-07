package edu.carleton.instarep.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class InstagramUser {
	
	private String username;
	private String profilePicture;
	private String fullName;
	private String bio;
	private int followedBy;
	private int followers;
	private int posts;
	
	public InstagramUser(){
		
	}
	
	public InstagramUser(String u, String pp, String fn, String b, int fb, int f, int p){
		this.username = u;
		this.profilePicture = pp;
		this.fullName = fn;
		this.bio = b;
		this.followedBy = fb;
		this.followers = f;
		this.posts = p;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public int getFollowedBy() {
		return followedBy;
	}

	public void setFollowedBy(int followedBy) {
		this.followedBy = followedBy;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getPosts() {
		return posts;
	}

	public void setPosts(int posts) {
		this.posts = posts;
	}
	
	
	
	

}
