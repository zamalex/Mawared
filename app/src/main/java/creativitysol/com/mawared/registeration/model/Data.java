package creativitysol.com.mawared.registeration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @Expose
    @SerializedName("token")
    private String token;
    @Expose
    @SerializedName("exists")
    private boolean exists;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
