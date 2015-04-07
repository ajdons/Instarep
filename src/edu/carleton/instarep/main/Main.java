package edu.carleton.instarep.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import edu.carleton.instarep.model.InstagramUser;
import edu.carleton.instarep.model.UserPref;
import edu.carleton.instarep.util.HttpRequest;
import edu.carleton.instarep.util.InstarepConstants;

@Path("/")
public class Main {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	
	public static String ACCESS_TOKEN = "1720732637.03ec65d.6117c3a320ec43f4884a90a5d117e31cg";
	public UserPref userPref;
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	private String name;

	public Main() throws MalformedURLException, UnknownHostException {
		name = "Instarep";
	}

	@GET
	public String printName() {
		return name;
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXML() {
		return "<?xml version=\"1.0\"?>" + "<sda> " + name + " </sda>";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtml() {
		return "<html> " + "<title>" + name + "</title>" + "<body><h1>" + name
				+ "</body></h1>" + "</html> ";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String sayJSON() {
		return "{" + name + "}";
	}
	
	@GET
	@Path("authenticate/{token}")
	@Produces(MediaType.TEXT_HTML)
	public String authenticateUser(@PathParam("token") String token) throws MalformedURLException {
		if (token != null){
			
			ACCESS_TOKEN = token;
			System.out.println("yaaaa: " + ACCESS_TOKEN);
			return "hey buddy, welcome to instarep. ur token is: " + ACCESS_TOKEN;
		}
		
		
		return "missing sum info";	
	}
	public String getDocumentsXML() throws MalformedURLException {
		return "<html> " + "<title>AUTHENTICATED</title>" + "<body><a href1></body>" + "</html>";
	}
	
	@GET
	@Path("testapi")
	@Produces(MediaType.TEXT_HTML)
	public String testApi() throws JSONException, MalformedURLException, IOException {
		String html = "";

		ACCESS_TOKEN = "1720708802.03ec65d.dd403a21e0b544aa92f5d9ab0b89e147";
		//html += "<h1>" + APIUnlikePost("869905215199799551_200863993") + "</h1>";

		int response = APILikePost("951057184307702584_200863993");
		//List<String> list = getUsersWhoLiked("951057184307702584_200863993");
		
			html+= "<p>" + response + "</p>";
		
		return html;
	}
	
	@GET
	@Path("userprefs/{likes}/{comments}/{follows}/{audience}/{time}")
	@Produces(MediaType.TEXT_HTML)
	public String setupUserPrefs(@PathParam("comments") int comments, 
								 @PathParam("likes") int likes, 
								 @PathParam("follows") int follows,
								 @PathParam("audience")int audience,
								 @PathParam("time")int botTime){
		String html = "";
		userPref = new UserPref(comments, likes, follows, audience, botTime);
		
		html += userPref;
		System.out.println(userPref);
		return html;
	}
	
	@GET
	@Path("instagramuser/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public InstagramUser getUserInfo(@PathParam("token")String token) throws MalformedURLException, IOException, JSONException{
		return APIUserInfo(token);
	}
	

	
	public List<String> getUsersWhoLiked(String mediaId) throws JSONException{
		List<String> responseList = new ArrayList<String>();
		String APIurl = InstarepConstants.BASE_URL + replaceKeyWithValue(InstarepConstants.URL_LIKERS_FOR_POST, "media-id", mediaId) + ACCESS_TOKEN;
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
	
	public List<String> getUsersWhoCommented(String mediaId) throws JSONException{
		
		List<String> responseList = new ArrayList<String>();
		String APIurl = InstarepConstants.BASE_URL + replaceKeyWithValue(InstarepConstants.URL_COMMENTS_FOR_POST, "media-id", mediaId) + ACCESS_TOKEN;
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
	
	public List<String> getPopularPosts() throws JSONException{

		List<String> responseList = new ArrayList<String>();
		String APIurl = InstarepConstants.BASE_URL + InstarepConstants.URL_POPULAR_POSTS + ACCESS_TOKEN;
		String response = HttpRequest.get(APIurl).body();
		
		JSONObject JSONResponse = new JSONObject(response);
		JSONArray data = JSONResponse.getJSONArray("data");
		
		for(int i=0; i<data.length(); i++){
			JSONObject post = data.getJSONObject(i);
			responseList.add((String) post.get("id"));
		}
		return responseList;
	}
	
	public List<String> getRecentPostsByUser(String userId) throws JSONException{
		List<String> responseList = new ArrayList<String>();
		String APIurl = InstarepConstants.BASE_URL + replaceKeyWithValue(InstarepConstants.URL_RECENT_POSTS_BY_USER, "user-id", userId) + ACCESS_TOKEN;
		String response = HttpRequest.get(APIurl).body();
		System.out.println(response);
		JSONObject JSONResponse = new JSONObject(response);
		JSONArray data = JSONResponse.getJSONArray("data");
		
		//Next url is provided by api if we want to go to the next page of results
		JSONObject pagination = JSONResponse.getJSONObject("pagination");
		if(!pagination.isNull("next_url")){
			String nextURL = (String) pagination.get("next_url");
			System.out.println(nextURL);
		}
		
		for(int i=0; i<data.length(); i++){
			JSONObject post = data.getJSONObject(i);
			responseList.add((String) post.get("id"));
		}
		return responseList;
	}
	
	public List<String> getRecentPostsByTag(String tag) throws JSONException{
		List<String> responseList = new ArrayList<String>();
		String APIurl = InstarepConstants.BASE_URL + replaceKeyWithValue(InstarepConstants.URL_RECENT_POSTS_BY_TAG, "tag-name", tag) + ACCESS_TOKEN;
		String response = HttpRequest.get(APIurl).body();
		System.out.println(response);
		JSONObject JSONResponse = new JSONObject(response);
		JSONArray data = JSONResponse.getJSONArray("data");
		
		//Next url is provided by api if we want to go to the next page of results
		JSONObject pagination = JSONResponse.getJSONObject("pagination");
		if(!pagination.isNull("next_url")){
			String nextURL = (String) pagination.get("next_url");
			System.out.println(nextURL);
		}
		
		for(int i=0; i<data.length(); i++){
			JSONObject post = data.getJSONObject(i);
			responseList.add((String) post.get("id"));
		}
		return responseList;
	}
	
	//Tested: Pass
	public int APILikePost(String mediaId){
		int response = HttpRequest.post(InstarepConstants.BASE_URL +
				replaceKeyWithValue(InstarepConstants.URL_DO_LIKE, "media-id", mediaId) +
				ACCESS_TOKEN).header(InstarepConstants.INSTA_SECRET_HEADER, InstarepConstants.CLIENT_SECRET1).code();
		System.out.println("Attempting to like post: " + mediaId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	//Tested: Pass
	public int APIUnlikePost(String mediaId){
		int response = HttpRequest.delete(InstarepConstants.BASE_URL + 
				replaceKeyWithValue(InstarepConstants.URL_DO_LIKE, "media-id", mediaId) + 
				ACCESS_TOKEN).header(InstarepConstants.INSTA_SECRET_HEADER, InstarepConstants.CLIENT_SECRET1).code();
		System.out.println("Attempting to un-like post: " + mediaId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	//Tested: Passes for follow and unfollow
	public int APIModifyRelationship(String userId,  String action){
		int response = HttpRequest.post(InstarepConstants.BASE_URL + 
				replaceKeyWithValue(InstarepConstants.URL_DO_FOLLOW, "user-id", userId) + 
				ACCESS_TOKEN).header(InstarepConstants.INSTA_SECRET_HEADER, InstarepConstants.CLIENT_SECRET1).send("action=" + action).code();
		System.out.println("Attempting modify relationship \"" + action + "\" with user: " + userId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	//Tested: Fail, app cannot use comment endpoint
	public int APICommentOnPost(String mediaId, String comment){
		int response = HttpRequest.post(InstarepConstants.BASE_URL + 
				replaceKeyWithValue(InstarepConstants.URL_DO_COMMENT, "media-id", mediaId) +
				ACCESS_TOKEN, true, "text", comment).header(InstarepConstants.INSTA_SECRET_HEADER, InstarepConstants.CLIENT_SECRET1).code();
		System.out.println("Attempting to comment \"" + comment + "\" on post: " + mediaId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	@SuppressWarnings("resource")
	public InstagramUser APIUserInfo(String token) throws MalformedURLException, IOException, JSONException{
		//int response = HttpRequest.get(InstarepConstants.BASE_URL +replaceKeyWithValue());
		
		Scanner scanner  = new Scanner(new URL(InstarepConstants.BASE_URL + InstarepConstants.URL_GET_USER_INFO+token).openStream(),"UTF-8").useDelimiter("\\A");
		String content =  scanner.next();
		JSONObject json = new JSONObject(content);
		System.out.println(json);
		
		JSONObject data =  json.getJSONObject("data");	
		JSONObject counts = data.getJSONObject("counts");
		
		
		String username = data.get("username").toString();
		String profilePicture = data.get("profile_picture").toString();
		String bio = data.get("bio").toString();
		String fullName = data.get("full_name").toString();
		
		int following = (int)counts.get("follows");
		int followers = (int)counts.get("followed_by");
		int posts = (int)counts.get("media");
		
		
		InstagramUser user = new InstagramUser(username, profilePicture, fullName, bio, following, followers, posts);
		
		// Just testing if we get the data
		System.out.println(data.get("username"));
		System.out.println(data.get("bio"));
		System.out.println(data.get("full_name"));
			
		
		System.out.println(counts.get("media"));
		System.out.println(counts.get("follows"));
		System.out.println(counts.get("followed_by"));

		scanner.close();
		
		return user;
	}
	
	public static String replaceKeyWithValue(String beforeString, String key, String value){
		String afterString = "";
		afterString = beforeString;
		
		return afterString.replace("{" + key + "}", value);
	}
}
