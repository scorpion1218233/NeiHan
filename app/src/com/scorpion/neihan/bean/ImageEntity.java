package com.scorpion.neihan.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageEntity extends TextEntity{
	private ImageUrlList largeList;
	private ImageUrlList middleList;
	
	public ImageUrlList getLargeList() {
		return largeList;
	}

	public ImageUrlList getMiddleList() {
		return middleList;
	}

	public void parseJson(JSONObject item)throws JSONException{
		super.parseJson(item);
		JSONObject group = item.getJSONObject("group");
		JSONObject largeImage = group.getJSONObject("large_image");
		JSONObject middleImage = group.getJSONObject("middle_image");
		
		largeList = new ImageUrlList();
		largeList.parseJson(largeImage);
		
		middleList = new ImageUrlList();
		middleList.parseJson(middleImage);
	}
	
}
