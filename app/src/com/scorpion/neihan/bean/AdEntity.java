package com.scorpion.neihan.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class AdEntity {

	private int type;
	private long diaplayTime;
	private long onlineTime;
	private int displayImageHeight;
	private String source;
	private long adId;
	private int displayImageWidth;
	private String packageName;
	private String title;
	private String openUrl;
	private String downloadUrl;
	private int isAd;
	private String displayInfo;
	private String webUrl;
	private int displayType;
	private String buttonText;
	private String appleid;
	private String trackUrl;
	private String label;
	private String AdType;
	private int id;
	private String ipaUrl;
	private String displayImage;
	

	public void parseJson(JSONObject json)throws JSONException{
		if(json!=null){
			type = json.getInt("type");
			diaplayTime = json.getLong("display_time");
			onlineTime = json.getLong("online_time");
			
			JSONObject ad = json.getJSONObject("ad");
			displayImageHeight = ad.getInt("display_image_height");
			adId = ad.getLong("ad_id");
			displayImageWidth = ad.getInt("display_image_width");
			source = ad.getString("source");
			packageName = ad.getString("package");
			title = ad.getString("title");
			openUrl = ad.getString("open_url");
			downloadUrl = ad.getString("download_url");
			isAd = ad.getInt("is_ad");
			displayInfo = ad.getString("display_info");
			webUrl = ad.getString("web_url");
			displayType = ad.getInt("display_type");
			buttonText = ad.getString("button_text");
			appleid = ad.getString("appleid");
			trackUrl = ad.getString("track_url");
			label = ad.getString("label");
			AdType = ad.getString("type");
			id = ad.getInt("id");
			ipaUrl = ad.getString("ipa_url");
			displayImage = ad.getString("display_image");
			
			
		}
	}


	public int getType() {
		return type;
	}


	public long getDiaplayTime() {
		return diaplayTime;
	}


	public long getOnlineTime() {
		return onlineTime;
	}


	public int getDisplayImageHeight() {
		return displayImageHeight;
	}


	public String getSource() {
		return source;
	}


	public long getAdId() {
		return adId;
	}


	public int getDisplayImageWidth() {
		return displayImageWidth;
	}

	public String getPackageName() {
		return packageName;
	}


	public String getTitle() {
		return title;
	}


	public String getOpenUrl() {
		return openUrl;
	}


	public String getDownloadUrl() {
		return downloadUrl;
	}


	public int getIsAd() {
		return isAd;
	}


	public String getDisplayInfo() {
		return displayInfo;
	}


	public String getWebUrl() {
		return webUrl;
	}


	public int getDisplayType() {
		return displayType;
	}


	public String getButtonText() {
		return buttonText;
	}


	public String getAppleid() {
		return appleid;
	}


	public String getTrackUrl() {
		return trackUrl;
	}


	public String getLabel() {
		return label;
	}


	public String getAdType() {
		return AdType;
	}


	public int getId() {
		return id;
	}


	public String getIpaUrl() {
		return ipaUrl;
	}


	public String getDisplayImage() {
		return displayImage;
	}
	
	
}
