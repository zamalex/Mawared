
package creativitysol.com.mawared.orderdetails.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pricing implements Parcelable
{

    @SerializedName("total_with_coupon_vat")
    @Expose
    private double totalWithCouponVat;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("copoun_amount")
    @Expose
    private double copounAmount;
    @SerializedName("vat_amount")
    @Expose
    private double vatAmount;
    public final static Creator<Pricing> CREATOR = new Creator<Pricing>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Pricing createFromParcel(Parcel in) {
            return new Pricing(in);
        }

        public Pricing[] newArray(int size) {
            return (new Pricing[size]);
        }

    }
    ;

    protected Pricing(Parcel in) {
        this.totalWithCouponVat = ((double) in.readValue((double.class.getClassLoader())));
        this.total = ((int) in.readValue((int.class.getClassLoader())));
        this.copounAmount = ((double) in.readValue((double.class.getClassLoader())));
        this.vatAmount = ((double) in.readValue((double.class.getClassLoader())));
    }

    public Pricing() {
    }

    public double getTotalWithCouponVat() {
        return totalWithCouponVat;
    }

    public void setTotalWithCouponVat(double totalWithCouponVat) {
        this.totalWithCouponVat = totalWithCouponVat;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getCopounAmount() {
        return copounAmount;
    }

    public void setCopounAmount(double copounAmount) {
        this.copounAmount = copounAmount;
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(totalWithCouponVat);
        dest.writeValue(total);
        dest.writeValue(copounAmount);
        dest.writeValue(vatAmount);
    }

    public int describeContents() {
        return  0;
    }

}
