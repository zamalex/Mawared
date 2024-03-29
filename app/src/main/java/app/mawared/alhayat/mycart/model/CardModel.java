
package app.mawared.alhayat.mycart.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CardModel {

    @SerializedName("data")
    private Data mData;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
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
