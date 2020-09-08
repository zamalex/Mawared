
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

}
