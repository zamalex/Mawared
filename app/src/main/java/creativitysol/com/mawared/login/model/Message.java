
package creativitysol.com.mawared.login.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Message {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("title")
    private Object mTitle;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Object getTitle() {
        return mTitle;
    }

    public void setTitle(Object title) {
        mTitle = title;
    }

}
