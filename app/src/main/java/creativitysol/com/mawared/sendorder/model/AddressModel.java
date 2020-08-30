
package creativitysol.com.mawared.sendorder.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AddressModel {

    @SerializedName("customer_shipping_addresses")
    private List<CustomerShippingAddress> mCustomerShippingAddresses;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;

    public List<CustomerShippingAddress> getCustomerShippingAddresses() {
        return mCustomerShippingAddresses;
    }

    public void setCustomerShippingAddresses(List<CustomerShippingAddress> customerShippingAddresses) {
        mCustomerShippingAddresses = customerShippingAddresses;
    }

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
