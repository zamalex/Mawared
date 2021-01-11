
package app.mawared.alhayat.home.orderscount;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("has_new_updates")
    private Boolean mHasNewUpdates;

    public Boolean getHasNewUpdates() {
        return mHasNewUpdates;
    }

    public void setHasNewUpdates(Boolean hasNewUpdates) {
        mHasNewUpdates = hasNewUpdates;
    }

}
