
package creativitysol.com.mawared.home.model.addmodel;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AddCardModel {

    @SerializedName("data")
    private CardData mData;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;

    public CardData getData() {
        return mData;
    }

    public void setData(CardData data) {
        mData = data;
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