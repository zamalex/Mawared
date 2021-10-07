package app.mawared.alhayat.login.model.newlogin;

import com.google.gson.annotations.SerializedName;

public class VerifyLoginResponse{



	@SerializedName("data")
	private Data data;

	@SerializedName("status")
	private int status;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;



	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public boolean isNewUser(){
		return data.newUser;
	}

	public String getAccessToken(){
		return data.accessToken;
	}
	public User getUser(){
		return data.user;
	}


	public Data getData() {
		return data;
	}

	public int getStatus(){
		return status;
	}

	class Data{
		@SerializedName("access_token")
		private String accessToken;

		@SerializedName("new_user")
		private boolean newUser;



		@SerializedName("user")
		private User user;

		public String getAccessToken(){
			return accessToken;
		}

		public boolean isNewUser(){
			return newUser;
		}
		public User getUser(){
			return user;
		}
	}
}