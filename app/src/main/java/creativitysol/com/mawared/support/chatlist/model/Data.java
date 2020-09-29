
package creativitysol.com.mawared.support.chatlist.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("chats")
    private List<Chat> mChats;

    public List<Chat> getChats() {
        return mChats;
    }

    public void setChats(List<Chat> chats) {
        mChats = chats;
    }

}
