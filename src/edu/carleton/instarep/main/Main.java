package edu.carleton.instarep.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import edu.carleton.instarep.model.Document;
import edu.carleton.instarep.model.UserPref;
import edu.carleton.instarep.util.HttpRequest;
import edu.carleton.instarep.util.InstarepConstants;

@Path("/")
public class Main {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	
	public static String ACCESS_TOKEN = "1720732637.03ec65d.6117c3a320ec43f4884a90a5d117e31c";
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
//		Scanner scanner  = new Scanner(new URL(InstarepConstants.BASE_URL + InstarepConstants.URL_POPULAR_POSTS+ACCESS_TOKEN).openStream(),"UTF-8").useDelimiter("\\A");
//		String content =  scanner.next();
//		JSONObject json = new JSONObject(content);
//		JSONArray data =  json.getJSONArray("data");
//		for(int i=0; i<data.length(); i++){
//			JSONObject post = data.getJSONObject(i);
//			html+= "<p>Type: " + post.get("type") + "</p>";
//			html+="	<p>ID: " + post.get("id") + "</p>";
//		}
//		scanner.close();
		//ACCESS_TOKEN = "1720708802.03ec65d.dd403a21e0b544aa92f5d9ab0b89e147";
		html += "<h1>" + APIModifyRelationship("200863993", "unfollow") + "</h1>";
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
	
	//Tested: Pass
	public int APILikePost(String mediaId){
		int response = HttpRequest.post(InstarepConstants.BASE_URL +
				replaceKeyWithValue(InstarepConstants.URL_DO_LIKE, "media-id", mediaId) +
				ACCESS_TOKEN).code();
		System.out.println("Attempting to like post: " + mediaId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	//Tested: Pass
	public int APIUnlikePost(String mediaId){
		int response = HttpRequest.delete(InstarepConstants.BASE_URL + 
				replaceKeyWithValue(InstarepConstants.URL_DO_LIKE, "media-id", mediaId) + 
				ACCESS_TOKEN).code();
		System.out.println("Attempting to un-like post: " + mediaId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	//Tested: Passes for follow and unfollow
	public int APIModifyRelationship(String userId,  String action){
		int response = HttpRequest.post(InstarepConstants.BASE_URL + 
				replaceKeyWithValue(InstarepConstants.URL_DO_FOLLOW, "user-id", userId) + 
				ACCESS_TOKEN).send("action=" + action).code();
		System.out.println("Attempting modify relationship \"" + action + "\" with user: " + userId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	//Tested: Fail, app cannot use comment endpoint
	public int APICommentOnPost(String mediaId, String comment){
		int response = HttpRequest.post(InstarepConstants.BASE_URL + 
				replaceKeyWithValue(InstarepConstants.URL_DO_COMMENT, "media-id", mediaId) +
				ACCESS_TOKEN, true, "text", comment).code();
		System.out.println("Attempting to comment \"" + comment + "\" on post: " + mediaId);
		System.out.println("Response code: " + response);
		return response;
	}
	
	public static String replaceKeyWithValue(String beforeString, String key, String value){
		String afterString = "";
		afterString = beforeString;
		
		return afterString.replace("{" + key + "}", value);
	}
}
