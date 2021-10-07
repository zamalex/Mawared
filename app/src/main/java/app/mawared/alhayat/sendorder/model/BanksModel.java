
package app.mawared.alhayat.sendorder.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BanksModel {

    @SerializedName("data")
    private List<Bank> mBanks;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;

    public List<Bank> getBanks() {
        return mBanks;
    }

    public void setBanks(List<Bank> banks) {
        mBanks = banks;
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
