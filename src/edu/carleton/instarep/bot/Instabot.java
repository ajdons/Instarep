package edu.carleton.instarep.bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.codehaus.jettison.json.JSONException;

import edu.carleton.instarep.model.InstagramPost;
import edu.carleton.instarep.model.UserPref;
import edu.carleton.instarep.util.APIUtil;
import edu.carleton.instarep.util.InstarepConstants;

public class Instabot {
	private UserPref userPref;
	private APIUtil apiUtil;
	final int MAX_FOLLOWS = 20;
	final int MAX_LIKES = 30;
	private long startTime;
	private int no_likes;
	private static Instabot instance;
	
	public static Instabot getInstance(){
		if (instance == null){
			instance = new Instabot();
		}
		return instance;
	}
	
	public Instabot(){
		// default using like, follows, general audience, 1 hr
		this.userPref =  new UserPref(0,1,1,0,1);
	}
	
	public Instabot(UserPref pref, APIUtil util){
		this.userPref = pref;
		this.apiUtil = util;
	}
	
	@SuppressWarnings("unused")
	public void startBot() throws JSONException{
		startTime = System.currentTimeMillis();
		final List<String> listOfAllUsers = new ArrayList<String>();
		List<InstagramPost> listOfPosts = new ArrayList<InstagramPost>();
		List<String> popularUsers = new ArrayList<String>();
		List<String> interactiveUsers = new ArrayList<String>();
		final int response = -1;
		int likesSoFar = 0;
		int followsSoFar = 0;
		if(userPref.getTargetAudience() == InstarepConstants.GROUP_GENERAL){
			listOfPosts = apiUtil.getPopularPosts();
			for(InstagramPost ip : listOfPosts){
				popularUsers.add(ip.getUserId());
				interactiveUsers.addAll(apiUtil.getUsersWhoCommented(ip.getMediaId()));
			}
			
			Collections.shuffle(interactiveUsers);
			Random r = new Random();
			int rand = r.nextInt(5)+10;
			for(int i=0; i<rand; i++){
				listOfAllUsers.add(popularUsers.get(i));
			}
			listOfAllUsers.addAll(interactiveUsers);
			
		    Runnable runnable = new Runnable() {
		        public void run() {
			        	Random r = new Random();
			        	int randomDelay = r.nextInt(30);
			        	System.out.println(randomDelay);
			        	try {
							Thread.sleep(randomDelay*1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
			        	String user = listOfAllUsers.remove(0);
			        	System.out.println(apiUtil.APIModifyRelationship(user, "follow"));
						System.out.println("I AM A BOT AND I JUST FOLLOWED: " + user);
	
			        	try {
							List<InstagramPost> usersPosts = apiUtil.getRecentPostsByUser(user);
							
							for(int i=0; i<r.nextInt(3); i++){
								if(usersPosts.get(i) != null)
									
								if (apiUtil.APILikePost(usersPosts.get(i).getMediaId()) == 200){
									no_likes++;									
								}
								//System.out.println(apiUtil.APILikePost(usersPosts.get(i).getMediaId()));
								System.out.println("I AM A BOT AND I JUST LIKED: " + usersPosts.get(i).getMediaId());
								
								
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	}
		      };
		      
		      ScheduledExecutorService service = Executors
		                      .newSingleThreadScheduledExecutor();
		      service.scheduleAtFixedRate(runnable, 0, 60, TimeUnit.SECONDS);
		}
		else if(userPref.getTargetAudience() == InstarepConstants.GROUP_VEGAN){
			listOfPosts = apiUtil.getRecentPostsByTag("vegan");
			for(InstagramPost ip : listOfPosts){
				listOfAllUsers.add(ip.getUserId());
			}
		    Runnable runnable = new Runnable() {
		        public void run() {
			        	Random r = new Random();
			        	int randomDelay = r.nextInt(30);
			        	System.out.println(randomDelay);
			        	try {
							Thread.sleep(randomDelay*1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
			        	String user = listOfAllUsers.remove(0);
			        	System.out.println(apiUtil.APIModifyRelationship(user, "follow"));
						System.out.println("I AM A BOT AND I JUST FOLLOWED: " + user);
	
			        	try {
							List<InstagramPost> usersPosts = apiUtil.getRecentPostsByUser(user);
							
							for(int i=0; i<r.nextInt(3); i++){
								if(usersPosts.get(i) != null)
									if (apiUtil.APILikePost(usersPosts.get(i).getMediaId()) == 200){
										no_likes++;									
									}
								System.out.println("I AM A BOT AND I JUST LIKED: " + usersPosts.get(i).getMediaId());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	}
		      };
		      
		      ScheduledExecutorService service = Executors
		                      .newSingleThreadScheduledExecutor();
		      service.scheduleAtFixedRate(runnable, 0, 60, TimeUnit.SECONDS);
		} 
	}
	
	

	public int getNo_likes() {
		return no_likes;
	}

	public void setNo_likes(int no_likes) {
		this.no_likes = no_likes;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public UserPref getUserPreference() {
		return userPref;
	}

	public void setUserPreference(UserPref userPreference) {
		this.userPref = userPreference;
	}

	public APIUtil getApiUtil() {
		return apiUtil;
	}

	public void setApiUtil(APIUtil apiUtil) {
		this.apiUtil = apiUtil;
	}
}
