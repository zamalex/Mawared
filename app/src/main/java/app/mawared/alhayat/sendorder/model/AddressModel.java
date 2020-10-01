
package app.mawared.alhayat.sendorder.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class AddressModel {

    @SerializedName("order_shipping_addresses")
    private List<OrderShippingAddress> mOrderShippingAddresses;

    public List<OrderShippingAddress> getOrderShippingAddresses() {
        return mOrderShippingAddresses;
    }

    public void setOrderShippingAddresses(List<OrderShippingAddress> orderShippingAddresses) {
        mOrderShippingAddresses = orderShippingAddresses;
    }
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;


    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean success) {
        mSuccess = success;
    }

}
