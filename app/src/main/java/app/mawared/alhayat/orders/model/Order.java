
package app.mawared.alhayat.orders.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order implements Parcelable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("formated_number")
    @Expose
    private int formatedNumber;
    @SerializedName("route_key")
    @Expose
    private int routeKey;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<Order> CREATOR = new Creator<Order>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return (new Order[size]);
        }

    }
    ;

    protected Order(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.formatedNumber = ((int) in.readValue((int.class.getClassLoader())));
        this.routeKey = ((int) in.readValue((int.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFormatedNumber() {
        return formatedNumber;
    }

    public void setFormatedNumber(int formatedNumber) {
        this.formatedNumber = formatedNumber;
    }

    public int getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(int routeKey) {
        this.routeKey = routeKey;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(formatedNumber);
        dest.writeValue(routeKey);
        dest.writeValue(createdAt);
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}
