
package creativitysol.com.mawared.sendorder.model.copon;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Promocode {

    @SerializedName("amount")
    private String mAmount;
    @SerializedName("code")
    private String mCode;
    @SerializedName("id")
    private Long mId;

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

}
