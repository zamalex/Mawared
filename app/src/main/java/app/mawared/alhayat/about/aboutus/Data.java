package app.mawared.alhayat.about.aboutus;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("title")
	private String title;

	@SerializedName("content")
	private String content;

	public String getTitle(){
		return title;
	}

	public String getContent(){
		return content;
	}
}