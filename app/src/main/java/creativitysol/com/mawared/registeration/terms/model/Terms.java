package creativitysol.com.mawared.registeration.terms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Terms {


    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("status")
    private int status;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
