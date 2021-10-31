package app.mawared.alhayat.home.model.subproducts;

import com.google.gson.annotations.SerializedName;

public class SubProductsItem{

	@SerializedName("thumbnail")
	private String thumbnail;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("price")
	private double price;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	public String getThumbnail(){
		return thumbnail;
	}

	public int getQuantity(){
		return quantity;
	}

	public double getPrice(){
		return price;
	}

	public int getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}
}