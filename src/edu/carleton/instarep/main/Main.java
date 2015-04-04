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
import edu.carleton.instarep.util.InstarepConstants;

@Path("/")
public class Main {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	
	public static String ACCESS_TOKEN = "";
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
		Scanner scanner  = new Scanner(new URL(InstarepConstants.BASE_URL + InstarepConstants.URL_POPULAR_POSTS+ACCESS_TOKEN).openStream(),"UTF-8").useDelimiter("\\A");
		String content =  scanner.next();
		JSONObject json = new JSONObject(content);
		JSONArray data =  json.getJSONArray("data");
		for(int i=0; i<data.length(); i++){
			JSONObject post = data.getJSONObject(i);
			html+= "<p>Type: " + post.get("type") + "</p>";
			html+="	<p>ID: " + post.get("id") + "</p>";
		}
		scanner.close();
		return html;
	}
	
	@POST
	@Path("userprefs")
	@Produces(MediaType.TEXT_HTML)
	public String setupUserPrefs(@PathParam("comments") String comments, @PathParam("likes") String likes, @PathParam("follows") String follows, @PathParam("audience") int audience){
		String html = "";
		userPref = new UserPref(Boolean.valueOf(comments), Boolean.valueOf(likes), Boolean.valueOf(follows), audience);
		return html;
	}
	
	public String replaceKeyWithValue(String beforeString, String key, String value){
		String afterString = "";
		afterString = beforeString;
		afterString.replace("{" + key + "}", value);
		return afterString;
	}
}
