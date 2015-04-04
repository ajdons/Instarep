package edu.carleton.instarep.model;

public class UserPref {
	private int allowComments;
	private int allowLikes;
	private int allowFollows;
	private int targetAudience;
	private int botTime;
	
	public UserPref(int comments, int likes, int follows, int audience, int botTime){
		this.allowComments = comments;
		this.allowLikes = likes;
		this.allowFollows = follows;
		this.targetAudience = audience;
		this.botTime = botTime;
	}

	public int isAllowComments() {
		return allowComments;
	}

	public void setAllowComments(int allowComments) {
		this.allowComments = allowComments;
	}

	public int isAllowLikes() {
		return allowLikes;
	}

	public void setAllowLikes(int allowLikes) {
		this.allowLikes = allowLikes;
	}

	public int isAllowFollows() {
		return allowFollows;
	}

	public void setAllowFollows(int allowFollows) {
		this.allowFollows = allowFollows;
	}

	public int getTargetAudience() {
		return targetAudience;
	}

	public void setTargetAudience(int targetAudience) {
		this.targetAudience = targetAudience;
	}

	public int getBotTime() {
		return botTime;
	}

	public void setBotTime(int botTime) {
		this.botTime = botTime;
	}

	@Override
	public String toString() {
		return "UserPref [allowComments=" + allowComments + ", allowLikes="
				+ allowLikes + ", allowFollows=" + allowFollows
				+ ", targetAudience=" + targetAudience + ", botTime=" + botTime
				+ "]";
	}
	
	
	
}
