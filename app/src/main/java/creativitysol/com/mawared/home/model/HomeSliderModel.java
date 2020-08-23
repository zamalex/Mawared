
package creativitysol.com.mawared.home.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HomeSliderModel {

    @SerializedName("data")
    private List<String> mData;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;

    public List<String> getData() {
        return mData;
    }

    public void setData(List<String> data) {
        mData = data;
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

}
