package app.mawared.alhayat.home.model;

import com.google.gson.annotations.SerializedName;

public class NotifyAvailable{

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}
}