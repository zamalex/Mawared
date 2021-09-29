package app.mawared.alhayat.sendorder.newaddress;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("address")
	private String address;

	@SerializedName("lng")
	private String lng;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("city_id")
	private int city_id;

	@SerializedName("id")
	private int id;

	@SerializedName("lat")
	private String lat;

	@SerializedName("set_default")
	private int setDefault;

	@SerializedName("username")
	private String username;

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setLng(String lng){
		this.lng = lng;
	}

	public String getLng(){
		return lng;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLat(String lat){
		this.lat = lat;
	}

	public String getLat(){
		return lat;
	}

	public void setSetDefault(int setDefault){
		this.setDefault = setDefault;
	}

	public int getSetDefault(){
		return setDefault;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
}