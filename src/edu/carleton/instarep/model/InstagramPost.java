package edu.carleton.instarep.model;

public class InstagramPost {
	private String mediaId;
	private String userId;
	
	public InstagramPost(String m, String u){
		this.mediaId = m;
		this.userId = u;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
