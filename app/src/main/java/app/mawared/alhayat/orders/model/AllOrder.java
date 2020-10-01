
package app.mawared.alhayat.orders.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllOrder implements Parcelable
{

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    public final static Creator<AllOrder> CREATOR = new Creator<AllOrder>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AllOrder createFromParcel(Parcel in) {
            return new AllOrder(in);
        }

        public AllOrder[] newArray(int size) {
            return (new AllOrder[size]);
        }

    }
    ;

    protected AllOrder(Parcel in) {
        this.status = ((int) in.readValue((int.class.getClassLoader())));
        this.success = ((boolean) in.readValue((boolean.class.getClassLoader())));
        in.readList(this.orders, (Order.class.getClassLoader()));
        this.pagination = ((Pagination) in.readValue((Pagination.class.getClassLoader())));
    }

    public AllOrder() {
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(success);
        dest.writeList(orders);
        dest.writeValue(pagination);
    }

    public int describeContents() {
        return  0;
    }

}
