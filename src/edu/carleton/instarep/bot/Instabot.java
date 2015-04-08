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
	final int MAX_FOLLOWS = 60;
	final int MAX_LIKES = 100;
	
	public Instabot(UserPref pref, APIUtil util){
		this.userPref = pref;
		this.setApiUtil(util);
	}
	
	public void startBot() throws JSONException{
		long startTime = System.currentTimeMillis();
		final List<String> listOfAllUsers = new ArrayList<String>();
		List<InstagramPost> listOfPosts = new ArrayList<InstagramPost>();
		List<String> popularUsers = new ArrayList<String>();
		List<String> interactiveUsers = new ArrayList<String>();
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
							System.out.println(apiUtil.APILikePost(usersPosts.get(i).getMediaId()));
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
							System.out.println(apiUtil.APILikePost(usersPosts.get(i).getMediaId()));
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
