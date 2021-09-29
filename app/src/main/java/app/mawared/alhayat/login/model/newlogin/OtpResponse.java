package app.mawared.alhayat.login.model.newlogin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.mawared.alhayat.login.model.error.ErrorItem;

public class OtpResponse{

	@SerializedName("success")
	private boolean success;

	@SerializedName("error")
	private List<ErrorItem> error;

	@SerializedName("show_otp")
	private boolean showOtp;



	@SerializedName("message")
	private String message;

	@SerializedName("otp_code")
	private int otpCode;

	@SerializedName("status")
	private int status;

	public boolean isSuccess(){
		return success;
	}

	public boolean isShowOtp(){
		return showOtp;
	}

	public String getMessage(){
		return message;
	}
	public List<ErrorItem> getError(){
		return error;
	}


	public int getOtpCode(){
		return otpCode;
	}

	public int getStatus(){
		return status;
	}
}