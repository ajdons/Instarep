package edu.carleton.instarep.util;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import edu.carleton.instarep.model.InstagramPost;
import edu.carleton.instarep.model.UserPref;

public class APIUtil {
	private UserPref userPref;
	private String accessToken;
	
	public APIUtil(UserPref up, String token){
		this.userPref = up;
		this.accessToken = token;
	}

	public UserPref getUserPref() {
		return userPref;
	}
	
	public List<String> getUsersWhoCommented(String mediaId) throws JSONException{
		
		List<String> responseList = new ArrayList<String>();
		String APIurl = InstarepConstants.BASE_URL + replaceKeyWithValue(InstarepConstants.URL_COMMENTS_FOR_POST, "media-id", mediaId) + accessToken;
		String response = HttpRequest.get(APIurl).body();
		System.out.println(response);
		JSONObject JSONResponse = new JSONObject(response);
		JSONArray data = JSONResponse.getJSONArray("data");
		
		for(int i=0; i<data.length(); i++){
			JSONObject comment = data.getJSONObject(i);
			JSONObject user = comment.getJSONObject("from");
			
			responseList.add((String) user.get("id"));
		}
		return responseList;
	}
	
	public List<InstagramPost> getPopularPosts() throws JSONException{

		List<InstagramPost> responseList = new ArrayList<InstagramPost>();
		String APIurl = InstarepConstants.BASE_URL + InstarepConstants.URL_POPULAR_POSTS + accessToken;
		String response = HttpRequest.get(APIurl).body();
		
		JSONObject JSONResponse = new JSONObject(response);
		JSONArray data = JSONResponse.getJSONArray("data");
		
		for(int i=0; i<data.length(); i++){
			JSONObject post = data.getJSONObject(i);
			JSONObject user = post.getJSONObject("user");
			String postId = (String)post.get("id");
			String userId = (String)user.get("id");
			responseList.add(new InstagramPost(postId, userId));
		}
		return responseList;
	}
	
	public List<InstagramPost> getRecentPostsByUser(String userId) throws JSONException{
		List<InstagramPost> responseList = new ArrayList<InstagramPost>();
		String APIurl = InstarepConstants.BASE_URL + replaceKeyWithValue(InstarepConstants.URL_RECENT_POSTS_BY_USER, "user-id", userId) + accessToken;
		String response = HttpRequest.get(APIurl).body();
		System.out.println(response);
		JSONObject JSONResponse = new JSONObject(response);
		JSONArray data = JSONResponse.getJSONArray("data");
		
		//Next url is provided by api if we want to go to the next page of results
		JSONObject pagination = JSONResponse.getJSONObject("pagination");
		String nextUrl = "";
		if(!pagination.isNull("next_url")){
			nextUrl = (String) pagination.get("next_url");
		}
		
		for(int i=0; i<data.length(); i++){
			JSONObject post = data.getJSONObject(i);
			JSONObject user = post.getJSONObject("user");
			String postId = (String)post.get("id");
			String uId = (String)user.get("id");
			responseList.add(new InstagramPost(postId, uId));
		}
		if(!nextUrl.isEmpty())
		responseList.addAll(getNextPagePosts(nextUrl));
		return responseList;
	}
	
	public List<InstagramPost> getRecentPostsByTag(String tag) throws JSONException{
		List<InstagramPost> responseList = new ArrayList<InstagramPost>();
		String APIurl = InstarepConstants.BASE_URL + replaceKeyWithValue(InstarepConstants.URL_RECENT_POSTS_BY_TAG, "tag-name", tag) + accessToken;
		String response = HttpRequest.get(APIurl).body();
		System.out.println(response);
		JSONObject JSONResponse = new JSONObject(response);
		JSONArray data = JSONResponse.getJSONArray("data");
		JSONObject pagination = JSONResponse.getJSONObject("pagination");
		String nextUrl = "";
		if(!pagination.isNull("next_url")){
			nextUrl = (String) pagination.get("next_url");
		}
		
		for(int i=0; i<data.length(); i++){
			JSONObject post = data.getJSONObject(i);
			JSONObject user = post.getJSONObject("user");
			String postId = (String)post.get("id");
			String userId = (String)user.get("id");
			responseList.add(new InstagramPost(postId, userId));
		}
		if(!nextUrl.isEmpty())
		responseList.addAll(getNextPagePosts(nextUrl));
		
		return responseList;
	}
	
	public List<InstagramPost> getNextPagePosts(String nextUrl) throws JSONException{
		List<InstagramPost> nextPagePosts = new ArrayList<InstagramPost>();
		
		for(int i=0; i<2; i++){
			String response = HttpRequest.get(nextUrl).body();
			System.out.println(response);
			JSONObject JSONResponse = new JSONObject(response);
			JSONArray data = JSONResponse.getJSONArray("data");
			
			for(int j=0; j<data.length(); j++){
				JSONObject post = data.getJSONObject(j);
				JSONObject user = post.getJSONObject("user");
				String postId = (String)post.get("id");
				String userId = (String)user.get("id");
				nextPagePosts.add(new InstagramPost(postId, userId));
			}
			JSONObject pagination = JSONResponse.getJSONObject("pagination");
			
			if(!pagination.isNull("next_url")){
				nextUrl = (String) pagination.get("next_url");
			}
			else
				break;
		}
		return nextPagePosts;
	}
	
	//Tested: Pass
	public int APILikePost(String mediaId){
		int response = HttpRequest.post(InstarepConstants.BASE_URL +
				replaceKeyWithValue(InstarepConstants.URL_DO_LIKE, "media-id", mediaId) +
				accessToken).header(InstarepConstants.INSTA_SECRET_HEADER, InstarepConstants.CLIENT_SECRET1).code();
		System.out.println("Attempting to like post: " + mediaId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	//Tested: Pass
	public int APIUnlikePost(String mediaId){
		int response = HttpRequest.delete(InstarepConstants.BASE_URL + 
				replaceKeyWithValue(InstarepConstants.URL_DO_LIKE, "media-id", mediaId) + 
				accessToken).header(InstarepConstants.INSTA_SECRET_HEADER, InstarepConstants.CLIENT_SECRET1).code();
		System.out.println("Attempting to un-like post: " + mediaId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	//Tested: Passes for follow and unfollow
	public int APIModifyRelationship(String userId,  String action){
		String secret = InstarepConstants.CLIENT_SECRET2;
	    String message = InstarepConstants.DEFAULT_IP;
	    int response = -1;
		try{
			     Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			     SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			     sha256_HMAC.init(secret_key);
		
			     String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
				response = HttpRequest.post(InstarepConstants.BASE_URL + 
						replaceKeyWithValue(InstarepConstants.URL_DO_FOLLOW, "user-id", userId) + 
						accessToken).header(InstarepConstants.INSTA_SECRET_HEADER, InstarepConstants.DEFAULT_IP + "|" +hash).send("action=" + action).code();
				System.out.println("Attempting modify relationship \"" + action + "\" with user: " + userId);
				System.out.println("Response code: " + response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	//Tested: Fail, app cannot use comment endpoint
	public int APICommentOnPost(String mediaId, String comment){
		int response = HttpRequest.post(InstarepConstants.BASE_URL + 
				replaceKeyWithValue(InstarepConstants.URL_DO_COMMENT, "media-id", mediaId) +
				accessToken, true, "text", comment).header(InstarepConstants.INSTA_SECRET_HEADER, InstarepConstants.CLIENT_SECRET1).code();
		System.out.println("Attempting to comment \"" + comment + "\" on post: " + mediaId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	public List<String> getUsersWhoLiked(String mediaId) throws JSONException{
		List<String> responseList = new ArrayList<String>();
		String APIurl = InstarepConstants.BASE_URL + replaceKeyWithValue(InstarepConstants.URL_LIKERS_FOR_POST, "media-id", mediaId) + accessToken;
		String response = HttpRequest.get(APIurl).body();
		System.out.println(response);
		JSONObject JSONResponse = new JSONObject(response);
		JSONArray data = JSONResponse.getJSONArray("data");
		
		for(int i=0; i<data.length(); i++){
			JSONObject user = data.getJSONObject(i);
			responseList.add((String) user.get("id"));
		}
		return responseList;
	}
	
	public static String replaceKeyWithValue(String beforeString, String key, String value){
		String afterString = "";
		afterString = beforeString;
		
		return afterString.replace("{" + key + "}", value);
	}

	public void setUserPref(UserPref userPref) {
		this.userPref = userPref;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}
