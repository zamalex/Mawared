package app.mawared.alhayat.sendorder.newaddress;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressNewResponse{

	@SerializedName("data")
	private ParentData data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private Long status;


	public ParentData getData(){
		return data;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(Long status){
		this.status = status;
	}

	public Long getStatus(){
		return status;
	}

	public AddressNewResponse(Long mStatus) {
		this.status = mStatus;
	}

	public class ParentData{
		@SerializedName("data")
		private List<DataItem> data;

		public List<DataItem> getData() {
			return data;
		}
	}

}