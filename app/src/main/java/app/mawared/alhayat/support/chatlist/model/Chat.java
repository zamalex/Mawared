
package app.mawared.alhayat.support.chatlist.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Chat implements Serializable {

    @SerializedName("channel_id")
    private String mChannelId;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("id")
    private Long mId;
    @SerializedName("order_id")
    private Object mOrderId;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_one")
    private String mUserOne;
    @SerializedName("user_two")
    private String mUserTwo;
    @SerializedName("last_message")
    public LastMessage lastMessage;

    @SerializedName("unread_count")
    public Long unread_count=0l;

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String channelId) {
        mChannelId = channelId;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Object getOrderId() {
        return mOrderId;
    }

    public void setOrderId(Object orderId) {
        mOrderId = orderId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getUserOne() {
        return mUserOne;
    }

    public void setUserOne(String userOne) {
        mUserOne = userOne;
    }

    public String getUserTwo() {
        return mUserTwo;
    }

    public void setUserTwo(String userTwo) {
        mUserTwo = userTwo;
    }


    public class LastMessage{
        @SerializedName("message")
        public String message;
    }
}
