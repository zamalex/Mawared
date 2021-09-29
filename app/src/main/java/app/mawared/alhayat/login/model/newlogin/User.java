package app.mawared.alhayat.login.model.newlogin;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("city_name")
	private String cityName;

	@SerializedName("balance")
	private int balance;

	@SerializedName("mobile")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("email")
	private String email;

	@SerializedName("points")
	private int points;

	public String getCountryCode(){
		return countryCode;
	}

	public String getCityName(){
		return cityName;
	}

	public int getBalance(){
		return balance;
	}

	public String getPhone(){
		return phone;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public String getEmail(){
		return email;
	}

	public int getPoints(){
		return points;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}