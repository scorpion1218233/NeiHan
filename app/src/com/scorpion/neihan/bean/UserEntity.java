package com.scorpion.neihan.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class UserEntity {
	
	/**
	 * 头像网址
	 */
	private String avatarUrl;
	
	/**
	 * 用户ID
	 */
	private long userId;
	
	/**
	 * 用户昵称
	 */
	private String name;
	
	/**
	 * 用户是否认证、是否加"V"了
	 */
	private boolean userVerified;
	
	public void parseJson(JSONObject json)throws JSONException{
		if(json!=null){
			avatarUrl = json.getString("avatar_url");
			userId = json.getLong("user_id");
			name = json.getString("name");
			userVerified = json.getBoolean("user_verified");
		}
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public boolean isUserVerified() {
		return userVerified;
	}

	
}
