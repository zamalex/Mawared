package app.mawared.alhayat.login.model.error;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ErrorResponse{

	@SerializedName("success")
	private boolean success;

	@SerializedName("error")
	private List<ErrorItem> error;

	@SerializedName("status")
	private int status;

	public boolean isSuccess(){
		return success;
	}

	public List<ErrorItem> getError(){
		return error;
	}

	public int getStatus(){
		return status;
	}
}