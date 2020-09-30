
package creativitysol.com.mawared.support.chatlist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class ChatList {

    @SerializedName("data")
    private List<Chat> mChats;


    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;

    public List<Chat> getChats() {
        return mChats;
    }

    public void setChats(List<Chat> chats) {
        mChats = chats;
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
