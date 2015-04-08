package edu.carleton.instarep.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import edu.carleton.instarep.bot.Instabot;
import edu.carleton.instarep.model.InstagramPost;
import edu.carleton.instarep.model.InstagramUser;
import edu.carleton.instarep.model.UserPref;
import edu.carleton.instarep.util.APIUtil;
import edu.carleton.instarep.util.HttpRequest;
import edu.carleton.instarep.util.InstarepConstants;

@Path("/")
public class Main {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	
	public static String ACCESS_TOKEN = "";
	public static String CODE = "";
	
	public UserPref userPref;
	public APIUtil  apiUtil;
	public Instabot bot;

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	private String name;

	public Main() throws MalformedURLException, UnknownHostException {
		name = "Instarep";
		bot = Instabot.getInstance();
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
	@Path("authenticate/{code}")
	@Produces(MediaType.TEXT_HTML)
	public String authenticateUser(@PathParam("code") String code) throws MalformedURLException, JSONException {
		if (code != null){		
			ACCESS_TOKEN = getTokenWithUserCode(code);
			return "hey buddy, welcome to instarep. ur token is: " + ACCESS_TOKEN;
		}
		
		
		return "missing sum info";	
	}
	
	public String getTokenWithUserCode(String code) throws JSONException{
		CODE = code;
		System.out.println(CODE);
		
		// Get actual token nao
		HashMap<String, Object> postArgs = new HashMap<String, Object>();
		postArgs.put("client_id", InstarepConstants.CLIENT_ID2);
		postArgs.put("client_secret", InstarepConstants.CLIENT_SECRET2);
		postArgs.put("grant_type", "authorization_code");
		postArgs.put("redirect_uri", InstarepConstants.URI);
		postArgs.put("code", code);
		
		String response = HttpRequest.post(InstarepConstants.TOKEN_URL).form(postArgs).body();		
		JSONObject JSONResponse = new JSONObject(response);
	    String token = JSONResponse.get("access_token").toString();
	    
		return token;	
	}
	
	@GET
	@Path("testapi")
	@Produces(MediaType.TEXT_HTML)
	public String testApi() throws JSONException, MalformedURLException, IOException {
		String html = "";

		ACCESS_TOKEN = "1720708802.03ec65d.dd403a21e0b544aa92f5d9ab0b89e147";
		userPref = new UserPref(0, 1, 1, 1, 10);
		apiUtil = new APIUtil(userPref, ACCESS_TOKEN);
//			Instabot bot = new Instabot(userPref, apiUtil);
//			bot.startBot();
		for(InstagramPost post : apiUtil.getRecentPostsByUser("507257020")){
			html+= "<p>" + post.getMediaId() + "</p>";
		}
		return html;
	}
	
	@GET
	@Path("startbot")
	@Produces(MediaType.TEXT_HTML)
	public String startBot() throws JSONException{	
		bot = new Instabot(userPref, apiUtil);
		
		System.out.println("user prefs " + userPref);
		bot.startBot();
		
		System.out.println("Bot has started.");
		return "bot started";
	}
	
	@GET
	@Path("stopbot")
	@Produces(MediaType.TEXT_HTML)
	public String stopBot() throws JSONException{
		
		bot.setStop(true);
		System.out.println("Bot has stopped.");
		return "bot stopped";
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
	@Path("instagramuser")
	@Produces(MediaType.APPLICATION_JSON)
	public InstagramUser getUserInfo() throws MalformedURLException, IOException, JSONException{
		return APIUserInfo(ACCESS_TOKEN);
	}
	
	@SuppressWarnings("resource")
	public InstagramUser APIUserInfo(String token) throws MalformedURLException, IOException, JSONException{		
		Scanner scanner  = new Scanner(new URL(InstarepConstants.BASE_URL + InstarepConstants.URL_GET_USER_INFO+token).openStream(),"UTF-8").useDelimiter("\\A");
		String content =  scanner.next();
		JSONObject json = new JSONObject(content);
		System.out.println(json);
		
		JSONObject data =  json.getJSONObject("data");	
		JSONObject counts =  data.getJSONObject("counts");
			
		String username = data.get("username").toString();
		String profilePicture = data.get("profile_picture").toString();
		String bio = data.get("bio").toString();
		String fullName = data.get("full_name").toString();
		
		int following = (int)counts.get("follows");
		int followers = (int)counts.get("followed_by");
		int posts = (int)counts.get("media");
		
		InstagramUser user = new InstagramUser(username, profilePicture, fullName, bio, followers, following, posts);
		scanner.close();
		
		return user;
	}
	
	public static void main(String[] args) {
		Random r = new Random();
		int test = r.nextInt(5)+10;
		System.out.println(test);
	}
}
