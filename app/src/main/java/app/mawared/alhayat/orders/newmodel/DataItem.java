package app.mawared.alhayat.orders.newmodel;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("can_cancel")
	private boolean canCancel;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("route_key")
	private int routeKey;

	@SerializedName("statusLabel")
	private String statusLabel;

	@SerializedName("new_updates")
	private boolean newUpdates;

	@SerializedName("show_rating")
	private boolean showRating;

	@SerializedName("driver_rating_stars")
	private int driverRatingStars;

	@SerializedName("formated_number")
	private int formatedNumber;

	@SerializedName("delivery_date")
	private String deliveryDate;

	@SerializedName("is_rated")
	private boolean isRated;

	@SerializedName("rating_stars")
	private int ratingStars;

	@SerializedName("id")
	private int id;

	@SerializedName("status")
	private int status;

	public boolean isCanCancel(){
		return canCancel;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getRouteKey(){
		return routeKey;
	}

	public String getStatusLabel(){
		return statusLabel;
	}

	public boolean isNewUpdates(){
		return newUpdates;
	}

	public boolean isShowRating(){
		return showRating;
	}

	public int getDriverRatingStars(){
		return driverRatingStars;
	}

	public int getFormatedNumber(){
		return formatedNumber;
	}

	public String getDeliveryDate(){
		return deliveryDate;
	}

	public boolean isIsRated(){
		return isRated;
	}

	public int getRatingStars(){
		return ratingStars;
	}

	public int getId(){
		return id;
	}

	public int getStatus(){
		return status;
	}
}