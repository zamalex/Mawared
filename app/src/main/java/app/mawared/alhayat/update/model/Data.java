
package app.mawared.alhayat.update.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("token")
    private Long mToken;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public Long getToken() {
        return mToken;
    }

    public void setToken(Long token) {
        mToken = token;
    }

}
