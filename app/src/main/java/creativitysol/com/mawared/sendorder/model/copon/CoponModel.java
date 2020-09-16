
package creativitysol.com.mawared.sendorder.model.copon;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CoponModel {

    @SerializedName("promocode")
    private Promocode mPromocode;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;

    public Promocode getPromocode() {
        return mPromocode;
    }

    public void setPromocode(Promocode promocode) {
        mPromocode = promocode;
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
