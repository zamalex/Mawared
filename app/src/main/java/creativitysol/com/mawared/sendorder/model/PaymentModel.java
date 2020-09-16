
package creativitysol.com.mawared.sendorder.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class PaymentModel {

    @SerializedName("payment_methods")
    private List<PaymentMethod> mPaymentMethods;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;

    public List<PaymentMethod> getPaymentMethods() {
        return mPaymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        mPaymentMethods = paymentMethods;
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
