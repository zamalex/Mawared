package app.mawared.alhayat.notification.newmodel;

import com.google.gson.annotations.SerializedName;

public class ListItem{

	@SerializedName("options")
	private Options options;

	@SerializedName("photo")
	private String photo;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("message")
	private String message;

	public Options getOptions(){
		return options;
	}

	public String getPhoto(){
		return photo;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public String getMessage(){
		return message;
	}
}