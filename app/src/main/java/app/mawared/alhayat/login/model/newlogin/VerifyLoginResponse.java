package app.mawared.alhayat.login.model.newlogin;

import com.google.gson.annotations.SerializedName;

public class VerifyLoginResponse{

	@SerializedName("access_token")
	private String accessToken;

	@SerializedName("new_user")
	private boolean newUser;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("user")
	private User user;

	@SerializedName("status")
	private int status;

	public String getAccessToken(){
		return accessToken;
	}

	public boolean isNewUser(){
		return newUser;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public User getUser(){
		return user;
	}

	public int getStatus(){
		return status;
	}
}