package com.scorpion.neihan.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
	
	private long uid;
	private String platform;
	private String text;
	private int diggCount;
	private long userDigg;
	private boolean userVerified;
	private int buryCount;
	private String userProfileUrl;
	private long id;
	private String userName;
	private int userBury;
	private long createTime;
	private long userId;
	private String description;
	private String userProfileImageUrl;

	public void parseJson(JSONObject json)throws JSONException{
		if(json!=null){
			uid = json.getLong("uid");
			platform = json.getString("platform");
			text = json.getString("text");
			diggCount = json.getInt("digg_count");
			userDigg = json.getLong("user_digg");
			userVerified = json.getBoolean("user_verified");
			buryCount = json.getInt("bury_count");
			userProfileUrl = json.getString("user_profile_url");
			id = json.getLong("id");
			userName = json.getString("user_name");
			userBury = json.getInt("user_bury");
			createTime = json.getLong("create_time");
			userId = json.getLong("user_id");
			description = json.optString("description");
			userProfileImageUrl = json.getString("user_profile_image_url");
		}
	}

	public long getUid() {
		return uid;
	}

	public String getPlatform() {
		return platform;
	}

	public String getText() {
		return text;
	}

	public int getDiggCount() {
		return diggCount;
	}

	public long getUserDigg() {
		return userDigg;
	}

	public boolean isUserVerified() {
		return userVerified;
	}

	public int getBuryCount() {
		return buryCount;
	}

	public String getUserProfileUrl() {
		return userProfileUrl;
	}

	public long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public int getUserBury() {
		return userBury;
	}

	public long getCreateTime() {
		return createTime;
	}

	public long getUserId() {
		return userId;
	}

	public String getDescription() {
		return description;
	}

	public String getUserProfileImageUrl() {
		return userProfileImageUrl;
	}
	
	

}
