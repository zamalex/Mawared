
package app.mawared.alhayat.home.notifymodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {
    @SerializedName("has_new_updates")
    @Expose
    private Boolean hasNewUpdates;
    @SerializedName("count")
    @Expose
    private Integer count;

    public Boolean getHasNewUpdates() {
        return hasNewUpdates;
    }

    public void setHasNewUpdates(Boolean hasNewUpdates) {
        this.hasNewUpdates = hasNewUpdates;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


}
