package edu.carleton.instarep.util;

public final class InstarepConstants {
	//DEV KEYS
	public static final String CLIENT_ID1 = "54c3915fd188411ba9dd42214a79dfd2";
	public static final String CLIENT_SECRET1 = "b6bbc45fefef401b9bd264448c60eb59";
	
	//GROUP CONSTANTS
	public static final int GROUP_GENERAL = 0;
	public static final int GROUP_VEGAN   = 1;
	public static final int GROUP_FITNESS = 2;
	
	
	//API URLs
	public static final String BASE_URL = "https://api.instagram.com/v1";
	
	/* Action       : Get a list of posts/comments/users
	 * Params       : none
	 * Request Type : GET will return the list as JSON
	 */
	public static final String URL_POPULAR_POSTS = "/media/popular?access_token=";
	public static final String URL_RECENT_POSTS_BY_TAG = "/tags/{tag-name}/media/recent?access_token=";
	public static final String URL_RECENT_POSTS_BY_USER = "/users/{user-id}/media/recent?access_token=";
	public static final String URL_COMMENTS_FOR_POST = "/media/{media-id}/comments?access_token=";
	public static final String URL_LIKERS_FOR_POST = "/media/{media-id}/likes?access_token=";
	public static final String URL_GET_USER_INFO = "/users/self?access_token=";
	
	//API Actions
	
	/* Action       : Like/Un-like a post
	 * Params       : none
	 * Request Type : POST will 'like' the post with authenticated user's account
	 * 				  DELETE will 'unlike' the post with authenticated user's account
	 */
	public static final String URL_DO_LIKE = "/media/{media-id}/likes?access_token=";
	
	/* Action       : Add a comment
	 * Params       : ?text - the text of the comment
	 * Request Type : POST will post the comment with authenticated user's account
	 *	---RULES FOR COMMENT API---
		Comments must not be automated. (LOL!!!!!!!!)
		The total length of the comment cannot exceeed 300 characters.
		The comment cannot contain more than 4 hashtags.
		The comment cannot contain more than 1 URL.
		The comment cannot consist of all capital letters.
	 */
	public static final String URL_DO_COMMENT = "/media/{media-id}/comments?access_token=";
	
	/* Action       : Delete a comment
	 * Params       : none
	 * Request Type : DELETE will delete the comment
	 */
	public static final String URL_REMOVE_COMMENT = "/media/{media-id}/comments/{comment-id}?access_token=";
	
	
	/* Action       : Follow/Unfollow a user
	 * Params       : ?action - the action to perform 'follow', 'unfollow' etc.
	 * Request Type : POST will modify the relationship based on the ?action parameter
	 */
	public static final String URL_DO_FOLLOW = "/users/{user-id}/relationship?access_token=";	
}
