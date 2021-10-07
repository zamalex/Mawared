package app.mawared.alhayat.notification.newmodel;

import com.google.gson.annotations.SerializedName;

public class Options{

	@SerializedName("id")
	private int id;

	@SerializedName("type")
	private String type;

	public int getId(){
		return id;
	}

	public String getType(){
		return type;
	}
}