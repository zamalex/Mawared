
package app.mawared.alhayat.home.model.checkrate;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("count")
    private Long mCount;
    @SerializedName("has_new_updates")
    private Boolean mHasNewUpdates;

    public Long getCount() {
        return mCount;
    }

    public void setCount(Long count) {
        mCount = count;
    }

    public Boolean getHasNewUpdates() {
        return mHasNewUpdates;
    }

    public void setHasNewUpdates(Boolean hasNewUpdates) {
        mHasNewUpdates = hasNewUpdates;
    }

}
