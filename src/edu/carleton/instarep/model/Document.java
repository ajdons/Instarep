package edu.carleton.instarep.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.BSONObject;

import com.mongodb.DBObject;

@XmlRootElement
public class Document implements DBObject {
	private Integer id;
	private Integer score;
	private String name;
	private String text;
	private ArrayList<String> tags;
	private ArrayList<String> links;

	public Document() {
		tags = new ArrayList<String>();
		links = new ArrayList<String>();
	}

	public Document(Integer id) {
		this();
		this.id = id;
	}	
	
	public Document(MultivaluedMap<String, String> map) {
		this();
		this.id = Integer.parseInt(map.getFirst("id"));
		this.score = Integer.parseInt(map.getFirst("score"));
		this.name = map.getFirst("name");
		this.text = map.getFirst("text");
		this.tags = new ArrayList<>(Arrays.asList(map.getFirst("tags").split(":")));
		this.links = new ArrayList<>(Arrays.asList(map.getFirst("links").split(":")));
	}

	public Integer getId() {
		return id;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getScore() {
		return score;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public ArrayList<String> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<String> links) {
		this.links = links;
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	public void removeTag(String tag) {
		tags.remove(tag);
	}

	public void addLink(String link) {
		links.add(link);
	}

	public void removeLink(String link) {
		links.remove(link);
	}

	@Override
	public boolean containsField(String s) {
		// TODO Auto-generated method stub
		if(s.equalsIgnoreCase("id")){
			return true;
		} else if(s.equalsIgnoreCase("score")){
			return true;
		}else if(s.equalsIgnoreCase("name")){
			return true;
		}else if(s.equalsIgnoreCase("text")){
			return true;
		}else if(s.equalsIgnoreCase("tags")){
			return true;
		}else if(s.equalsIgnoreCase("links")){
			return true;
		}
		return false;
	}

	@Override
	public boolean containsKey(String s) {
		// TODO Auto-generated method stub
		if(s.equalsIgnoreCase("id")){
			return true;
		}else if(s.equalsIgnoreCase("score")){
			return true;
		}else if(s.equalsIgnoreCase("name")){
			return true;
		}else if(s.equalsIgnoreCase("text")){
			return true;
		}else if(s.equalsIgnoreCase("tags")){
			return true;
		}else if(s.equalsIgnoreCase("links")){
			return true;
		}
		return false;
	}

	@Override
	public Object get(String s) {
		// TODO Auto-generated method stub
		if(s.equalsIgnoreCase("id")){
			return getId();
		}else if(s.equalsIgnoreCase("score")){
			return getScore();
		}else if(s.equalsIgnoreCase("name")){
			return getName();
		}else if(s.equalsIgnoreCase("text")){
			return getText();
		}else if(s.equalsIgnoreCase("tags")){
			return getTags();
		}else if(s.equalsIgnoreCase("links")){
			return getLinks();
		}
		return null;
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		Set<String> set = new HashSet();
		set.add("id");
		set.add("score");
		set.add("name");
		set.add("text");
		set.add("tags");
		set.add("links");
		return set;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(String s, Object obj) {
		// TODO Auto-generated method stub
		if(s.equalsIgnoreCase("id")){
			setId((Integer)obj);
			return getId();
		}else if(s.equalsIgnoreCase("score")){
			setScore((Integer)obj);
			return getScore();
		}else if(s.equalsIgnoreCase("name")){
			setName((String)obj);
			return getName();
		}else if(s.equalsIgnoreCase("text")){
			setText((String)obj);
			return getText();
		}else if(s.equalsIgnoreCase("tags")){
			setTags((ArrayList<String>)obj);
			return getTags();
		}else if(s.equalsIgnoreCase("links")){
			setLinks((ArrayList<String>)obj);
			return getLinks();
		}		
		return null;
	}

	@Override
	public void putAll(BSONObject arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putAll(Map arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object removeField(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> toMap() {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", getId());
		map.put("score", getScore());
		map.put("name", getName());
		map.put("text", getText());
		map.put("tags", getTags());
		map.put("links", getLinks());
		return map;
	}

	@Override
	public boolean isPartialObject() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void markAsPartialObject() {
		// TODO Auto-generated method stub
		
	}
}