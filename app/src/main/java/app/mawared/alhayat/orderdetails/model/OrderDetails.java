
package app.mawared.alhayat.orderdetails.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetails implements Parcelable
{

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("order")
    @Expose
    private Order order;
    public final static Creator<OrderDetails> CREATOR = new Creator<OrderDetails>() {


        @SuppressWarnings({
            "unchecked"
        })
        public OrderDetails createFromParcel(Parcel in) {
            return new OrderDetails(in);
        }

        public OrderDetails[] newArray(int size) {
            return (new OrderDetails[size]);
        }

    }
    ;

    protected OrderDetails(Parcel in) {
        this.status = ((int) in.readValue((int.class.getClassLoader())));
        this.success = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.order = ((Order) in.readValue((Order.class.getClassLoader())));
    }

    public OrderDetails() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(success);
        dest.writeValue(order);
    }

    public int describeContents() {
        return  0;
    }

}
