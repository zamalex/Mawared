
package creativitysol.com.mawared.orderdetails.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable
{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("total")
    @Expose
    private double total;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("has_offer")
    @Expose
    private int hasOffer;
    @SerializedName("offer")
    @Expose
    private String offer;
    public final static Creator<Product> CREATOR = new Creator<Product>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return (new Product[size]);
        }

    }
    ;

    protected Product(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.quantity = ((int) in.readValue((int.class.getClassLoader())));
        this.total = ((int) in.readValue((int.class.getClassLoader())));
        this.img = ((String) in.readValue((String.class.getClassLoader())));
        this.hasOffer = ((int) in.readValue((int.class.getClassLoader())));
        this.offer = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Product() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int isHasOffer() {
        return hasOffer;
    }

    public void setHasOffer(int hasOffer) {
        this.hasOffer = hasOffer;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(quantity);
        dest.writeValue(total);
        dest.writeValue(img);
        dest.writeValue(hasOffer);
        dest.writeValue(offer);
    }

    public int describeContents() {
        return  0;
    }

}
