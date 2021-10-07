package app.mawared.alhayat.support.ordermodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ChatOrdersModel{

	@SerializedName("data")
	private List<Integer> data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	public List<Integer> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}
}