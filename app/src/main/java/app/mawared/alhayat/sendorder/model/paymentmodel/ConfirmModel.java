
package app.mawared.alhayat.sendorder.model.paymentmodel;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ConfirmModel {

    @SerializedName("data")
    private Data mData;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;
    @SerializedName("message")
    public String message;

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
