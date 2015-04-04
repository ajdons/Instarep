package edu.carleton.instarep.model;

public class UserPref {
	private boolean allowComments;
	private boolean allowLikes;
	private boolean allowFollows;
	private int targetAudience;
	
	public UserPref(boolean comments, boolean likes, boolean follows, int audience){
		this.allowComments = comments;
		this.allowLikes = likes;
		this.allowFollows = follows;
		this.targetAudience = audience;
	}

	public boolean isAllowComments() {
		return allowComments;
	}

	public void setAllowComments(boolean allowComments) {
		this.allowComments = allowComments;
	}

	public boolean isAllowLikes() {
		return allowLikes;
	}

	public void setAllowLikes(boolean allowLikes) {
		this.allowLikes = allowLikes;
	}

	public boolean isAllowFollows() {
		return allowFollows;
	}

	public void setAllowFollows(boolean allowFollows) {
		this.allowFollows = allowFollows;
	}

	public int getTargetAudience() {
		return targetAudience;
	}

	public void setTargetAudience(int targetAudience) {
		this.targetAudience = targetAudience;
	}
	
}
