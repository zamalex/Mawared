
package app.mawared.alhayat.sendorder.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class PaymentModel {

    @SerializedName("data")
    private List<PaymentMethod> mPaymentMethods;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;



    public List<PaymentMethod> getPaymentMethods() {
        if (mPaymentMethods!=null)
            if (mPaymentMethods.size()>0)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    mPaymentMethods.removeIf(new Predicate<PaymentMethod>() {
                        @Override
                        public boolean test(PaymentMethod method) {
                            return method.getGateway().equals("apple-pay");
                        }
                    });
                }
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
