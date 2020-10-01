
package app.mawared.alhayat.orderdetails.model;

import java.util.List;
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
    @SerializedName("show_rating")
    @Expose
    private boolean showRating;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_phone")
    @Expose
    private String customerPhone;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("pricing")
    @Expose
    private Pricing pricing;
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
        this.showRating = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.customerName = ((String) in.readValue((String.class.getClassLoader())));
        this.customerPhone = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryDate = ((String) in.readValue((String.class.getClassLoader())));
        this.paymentMethod = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.products, (app.mawared.alhayat.orderdetails.model.Product.class.getClassLoader()));
        this.pricing = ((Pricing) in.readValue((Pricing.class.getClassLoader())));
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

    public boolean isShowRating() {
        return showRating;
    }

    public void setShowRating(boolean showRating) {
        this.showRating = showRating;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Pricing getPricing() {
        return pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(formatedNumber);
        dest.writeValue(routeKey);
        dest.writeValue(showRating);
        dest.writeValue(createdAt);
        dest.writeValue(status);
        dest.writeValue(customerName);
        dest.writeValue(customerPhone);
        dest.writeValue(deliveryDate);
        dest.writeValue(paymentMethod);
        dest.writeValue(address);
        dest.writeList(products);
        dest.writeValue(pricing);
    }

    public int describeContents() {
        return  0;
    }

}
