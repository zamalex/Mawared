
package app.mawared.alhayat.login.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LoginResponse {

    @SerializedName("expire_points")
    private Long mExpirePoints;
    @SerializedName("message")
    private Message mMessage;
    @SerializedName("redirect")
    private String mRedirect;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;
    @SerializedName("user")
    private User mUser;

    public Long getExpirePoints() {
        return mExpirePoints;
    }

    public void setExpirePoints(Long expirePoints) {
        mExpirePoints = expirePoints;
    }

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

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

}
