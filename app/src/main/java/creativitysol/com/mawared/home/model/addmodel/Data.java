
package creativitysol.com.mawared.home.model.addmodel;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("cart_id")
    private Long mCartId;
    @SerializedName("device_id")
    private Object mDeviceId;
    @SerializedName("isRegistered")
    private Boolean mIsRegistered;
    @SerializedName("items_count")
    private Long mItemsCount;
    @SerializedName("items_sum_final_prices")
    private Double mItemsSumFinalPrices;

    @SerializedName("items_sum_prices")
    private Double mItemsSumPrices;
    @SerializedName("message")
    private String mMessage;

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

    public Long getItemsCount() {
        return mItemsCount;
    }

    public void setItemsCount(Long itemsCount) {
        mItemsCount = itemsCount;
    }

    public Double getItemsSumFinalPrices() {
        return mItemsSumFinalPrices;
    }

    public void setItemsSumFinalPrices(Double itemsSumFinalPrices) {
        mItemsSumFinalPrices = itemsSumFinalPrices;
    }

    public Double getmItemsSumFinalPrices() {
        return mItemsSumFinalPrices;
    }

    public Double getmItemsSumPrices() {
        return mItemsSumPrices;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

}
