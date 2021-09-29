package app.mawared.alhayat.defaultaddress;

import com.google.gson.annotations.SerializedName;

public class DefaultAddressResponse{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public Data getData(){
		return data;
	}

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