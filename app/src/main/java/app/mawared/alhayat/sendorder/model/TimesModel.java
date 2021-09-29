
package app.mawared.alhayat.sendorder.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TimesModel {

    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;
    @SerializedName("data")
    private List<Time> mTimes;

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

    public List<Time> getTimes() {
        return mTimes;
    }

    public void setTimes(List<Time> times) {
        mTimes = times;
    }

    public TimesModel(Long mStatus) {
        this.mStatus = mStatus;
    }
}
