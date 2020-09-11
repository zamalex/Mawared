package creativitysol.com.mawared.contactus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUsResponse {


    @Expose
    @SerializedName("message")
    private Message message;
    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("status")
    private int status;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
