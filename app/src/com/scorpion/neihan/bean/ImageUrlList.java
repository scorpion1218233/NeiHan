package com.scorpion.neihan.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageUrlList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7697226731407980201L;
	private List<String> largeImageUrls;
	private String uri;
	private int width;
	private int height;

	public List<String> getLargeImageUrls() {
		return largeImageUrls;
	}

	public String getUri() {
		return uri;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void parseJson(JSONObject json){
		try {
			largeImageUrls = parseImageUrlList(json);
			uri = json.getString("uri");
			width = json.getInt("width");
			height = json.getInt("height");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<String> parseImageUrlList(JSONObject largeImage) throws JSONException {
		JSONArray urls = largeImage.getJSONArray("url_list");
		
		//图片的网址全部在这里
		List<String> largeImageUrls = new LinkedList<String>();
		
		int ulen = urls.length();
		for (int j = 0; j < ulen; j++) {
			JSONObject uobj = urls.getJSONObject(j);
			String url = uobj.getString("url");
			largeImageUrls.add(url);
		}
		
		return largeImageUrls;
	}
}
