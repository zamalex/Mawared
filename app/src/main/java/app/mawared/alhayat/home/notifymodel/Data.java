
package app.mawared.alhayat.home.notifymodel;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("unread")
    private Long mUnread;

    public Long getUnread() {
        return mUnread;
    }

    public void setUnread(Long unread) {
        mUnread = unread;
    }

}
