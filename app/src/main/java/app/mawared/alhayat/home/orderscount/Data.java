
package app.mawared.alhayat.home.orderscount;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("has_new_updates")
    private Boolean mHasNewUpdates;

    @SerializedName("count")
    private int count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getHasNewUpdates() {
        return mHasNewUpdates;
    }

    public void setHasNewUpdates(Boolean hasNewUpdates) {
        mHasNewUpdates = hasNewUpdates;
    }

}
