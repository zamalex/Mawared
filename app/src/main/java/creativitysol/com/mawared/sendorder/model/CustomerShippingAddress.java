
package creativitysol.com.mawared.sendorder.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CustomerShippingAddress {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("lat")
    private String mLat;
    @SerializedName("lng")
    private String mLng;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
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

}
