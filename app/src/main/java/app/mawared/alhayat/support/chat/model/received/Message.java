
package app.mawared.alhayat.support.chat.model.received;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Message {

    @SerializedName("conversation_id")
    private String mConversationId;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("deleted_from_receiver")
    private String mDeletedFromReceiver;
    @SerializedName("deleted_from_sender")
    private String mDeletedFromSender;
    @SerializedName("id")
    private Long mId;
    @SerializedName("is_seen")
    private String mIsSeen;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("sender")
    private Sender mSender;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_id")
    private String mUserId;

    public String getConversationId() {
        return mConversationId;
    }

    public void setConversationId(String conversationId) {
        mConversationId = conversationId;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getDeletedFromReceiver() {
        return mDeletedFromReceiver;
    }

    public void setDeletedFromReceiver(String deletedFromReceiver) {
        mDeletedFromReceiver = deletedFromReceiver;
    }

    public String getDeletedFromSender() {
        return mDeletedFromSender;
    }

    public void setDeletedFromSender(String deletedFromSender) {
        mDeletedFromSender = deletedFromSender;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getIsSeen() {
        return mIsSeen;
    }

    public void setIsSeen(String isSeen) {
        mIsSeen = isSeen;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Sender getSender() {
        return mSender;
    }

    public void setSender(Sender sender) {
        mSender = sender;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

}
