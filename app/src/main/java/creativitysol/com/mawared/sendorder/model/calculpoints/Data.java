
package creativitysol.com.mawared.sendorder.model.calculpoints;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("new_price")
    private Double mNewPrice;

    public Double getNewPrice() {
        return mNewPrice;
    }

    public void setNewPrice(Double newPrice) {
        mNewPrice = newPrice;
    }

}
