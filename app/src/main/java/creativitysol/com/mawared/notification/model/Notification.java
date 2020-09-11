package creativitysol.com.mawared.notification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Notification {


    @Expose
    @SerializedName("notifications_messages")
    private List<Notifications_messages> notifications_messages;
    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("status")
    private int status;

    public List<Notifications_messages> getNotifications_messages() {
        return notifications_messages;
    }

    public void setNotifications_messages(List<Notifications_messages> notifications_messages) {
        this.notifications_messages = notifications_messages;
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
