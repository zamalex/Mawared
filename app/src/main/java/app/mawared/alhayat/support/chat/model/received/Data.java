
package app.mawared.alhayat.support.chat.model.received;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("agent_name")
    private String mAgentName;
    @SerializedName("status")
    private String status;
    @SerializedName("channel_id")
    private String mChannelId;
    @SerializedName("conversation_id")
    private Long mConversationId;
    @SerializedName("conversation_status")
    private String mConversationStatus;
    @SerializedName("conversation_title")
    private String mConversationTitle;
    @SerializedName("messages")
    private List<Message> mMessages;

    public String getAgentName() {
        return mAgentName;
    }

    public void setAgentName(String agentName) {
        mAgentName = agentName;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String channelId) {
        mChannelId = channelId;
    }

    public Long getConversationId() {
        return mConversationId;
    }

    public void setConversationId(Long conversationId) {
        mConversationId = conversationId;
    }

    public String getConversationStatus() {
        return mConversationStatus;
    }

    public void setConversationStatus(String conversationStatus) {
        mConversationStatus = conversationStatus;
    }

    public String getConversationTitle() {
        return mConversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        mConversationTitle = conversationTitle;
    }

    public List<Message> getMessages() {
        return mMessages;
    }

    public void setMessages(List<Message> messages) {
        mMessages = messages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
