package app.mawared.alhayat.login.model.newlogin;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.mawared.alhayat.login.model.error.ErrorItem;

public class OtpResponse{

	@SerializedName("data")
	private Data data;


	@SerializedName("success")
	private boolean success;

	@SerializedName("error")
	private List<ErrorItem> error;



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
	public List<ErrorItem> getError(){
		return error;
	}

	public int getOtpCode(){
		return data.otpCode;
	}


	public Data getData() {
		return data;
	}

	public int getStatus(){
		return status;
	}

		class Data{
			@SerializedName("show_otp")
			private boolean showOtp;

			@SerializedName("otp_code")
			private int otpCode;


			public boolean isShowOtp(){
				return showOtp;
			}
			public int getOtpCode(){
				return otpCode;
			}
		}

}