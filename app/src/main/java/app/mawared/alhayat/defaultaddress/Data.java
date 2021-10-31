package app.mawared.alhayat.defaultaddress;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("address")
	private String address;

	@SerializedName("lng")
	private String lng;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("id")
	private int id;

	@SerializedName("lat")
	private String lat;

	@SerializedName("title")
	private String title;

	@SerializedName("type")
	private String type;

	@SerializedName("set_default")
	private int setDefault;

	@SerializedName("city_id")
	private int cityId;

	@SerializedName("username")
	private String username;

	public String getAddress(){
		return address;
	}

	public String getLng(){
		return lng;
	}

	public String getMobile(){
		return mobile;
	}

	public int getId(){
		return id;
	}

	public String getLat(){
		return lat;
	}

	public int getSetDefault(){
		return setDefault;
	}

	public int getCityId(){
		return cityId;
	}

	public String getUsername(){
		return username;
	}


	public String getTitle() {
		if (title==null)
			title="home";
		return title;
	}

	public String getType() {
		if (type==null)
			type="personal";
		return type;
	}


}