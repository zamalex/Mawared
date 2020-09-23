
package creativitysol.com.mawared.sendorder.model.paymentmodel.visa;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("view")
    private String mView;

    public String getView() {
        return mView;
    }

    public void setView(String view) {
        mView = view;
    }

}
