
package app.mawared.alhayat.home.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("amount")
    private Long mAmount;

    @SerializedName("price")
    private double price;

    public Long getAmount() {
        return mAmount;
    }
    public double getPrice() {
        return price;
    }

    public void setAmount(Long amount) {
        mAmount = amount;
    }

}
