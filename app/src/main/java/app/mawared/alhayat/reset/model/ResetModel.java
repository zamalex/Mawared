
package app.mawared.alhayat.reset.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ResetModel {

    @SerializedName("message")
    private Message mMessage;
    @SerializedName("redirect")
    private String mRedirect;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;

    public Message getMessage() {
        return mMessage;
    }

    public void setMessage(Message message) {
        mMessage = message;
    }

    public String getRedirect() {
        return mRedirect;
    }

    public void setRedirect(String redirect) {
        mRedirect = redirect;
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
