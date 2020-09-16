
package creativitysol.com.mawared.sendorder.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class OrderShippingAddress {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("delivery_type")
    private String mDeliveryType;
    @SerializedName("id")
    private Long mId;
    @SerializedName("lat")
    private String mLat;
    @SerializedName("lng")
    private String mLng;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("order_id")
    private String mOrderId;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("username")
    private String mUsername;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getDeliveryType() {
        return mDeliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        mDeliveryType = deliveryType;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getLat() {
        return mLat;
    }

    public void setLat(String lat) {
        mLat = lat;
    }

    public String getLng() {
        return mLng;
    }

    public void setLng(String lng) {
        mLng = lng;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getOrderId() {
        return mOrderId;
    }

    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

}
