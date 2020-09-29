
package creativitysol.com.mawared.support.chat.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("agent_name")
    private String mAgentName;
    @SerializedName("channel_id")
    private String mChannelId;
    @SerializedName("conversation_id")
    private Long mConversationId;
    @SerializedName("conversation_status")
    private Long mConversationStatus;
    @SerializedName("conversation_title")
    private String mConversationTitle;

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

    public Long getConversationStatus() {
        return mConversationStatus;
    }

    public void setConversationStatus(Long conversationStatus) {
        mConversationStatus = conversationStatus;
    }

    public String getConversationTitle() {
        return mConversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        mConversationTitle = conversationTitle;
    }

}
