
package app.mawared.alhayat.sendorder.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PaymentMethod {

    @SerializedName("gateway")
    private String mGateway;
    @SerializedName("id")
    private Long mId;
    @SerializedName("name")
    private String mName;

    public String getGateway() {
        return mGateway;
    }

    public void setGateway(String gateway) {
        mGateway = gateway;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
