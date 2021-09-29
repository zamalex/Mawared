
package app.mawared.alhayat.sendorder.model.points;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("expire_date")
    private Long mExpireDate=1l;
    @SerializedName("to_exchange")
    private List<Long> mToExchange;
    @SerializedName("balance")
    private double mTotalPoints;

    public Long getExpireDate() {
        return mExpireDate;
    }

    public void setExpireDate(Long expireDate) {
        mExpireDate = expireDate;
    }

    public List<Long> getToExchange() {
        return mToExchange;
    }

    public void setToExchange(List<Long> toExchange) {
        mToExchange = toExchange;
    }

    public double getTotalPoints() {
        return mTotalPoints;
    }

    public void setTotalPoints(Long totalPoints) {
        mTotalPoints = totalPoints;
    }

}
