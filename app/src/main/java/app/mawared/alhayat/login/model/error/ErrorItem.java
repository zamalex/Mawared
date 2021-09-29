package app.mawared.alhayat.login.model.error;

import com.google.gson.annotations.SerializedName;

public class ErrorItem{

	@SerializedName("code")
	private String code;

	@SerializedName("message")
	private String message;

	public String getCode(){
		return code;
	}

	public String getMessage(){
		return message;
	}
}