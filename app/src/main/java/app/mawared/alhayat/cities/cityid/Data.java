package app.mawared.alhayat.cities.cityid;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("city_id")
	private int cityId;

	public int getCityId(){
		return cityId;
	}
}