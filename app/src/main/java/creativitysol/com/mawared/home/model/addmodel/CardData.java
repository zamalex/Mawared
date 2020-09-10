
package creativitysol.com.mawared.home.model.addmodel;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CardData {

    @SerializedName("cart_id")
    private Long mCartId;
    @SerializedName("device_id")
    private Object mDeviceId;
    @SerializedName("isRegistered")
    private Boolean mIsRegistered;
    @SerializedName("message")
    private String mMessage;

    @SerializedName("items_count")
    private int items_count;

    @SerializedName("items_sum_final_prices")
    private double items_sum_final_prices;

    public Long getCartId() {
        return mCartId;
    }

    public void setCartId(Long cartId) {
        mCartId = cartId;
    }

    public Object getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(Object deviceId) {
        mDeviceId = deviceId;
    }

    public Boolean getIsRegistered() {
        return mIsRegistered;
    }

    public void setIsRegistered(Boolean isRegistered) {
        mIsRegistered = isRegistered;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public int getItems_count() {
        return items_count;
    }

    public void setItems_count(int items_count) {
        this.items_count = items_count;
    }

    public double getItems_sum_final_prices() {
        return items_sum_final_prices;
    }

    public void setItems_sum_final_prices(double items_sum_final_prices) {
        this.items_sum_final_prices = items_sum_final_prices;
    }
}
