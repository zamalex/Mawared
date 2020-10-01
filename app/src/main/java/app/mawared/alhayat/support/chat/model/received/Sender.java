
package app.mawared.alhayat.support.chat.model.received;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Sender {

    @SerializedName("active")
    private String mActive;
    @SerializedName("chat_status")
    private String mChatStatus;
    @SerializedName("city_id")
    private String mCityId;
    @SerializedName("country_code")
    private String mCountryCode;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("device_token")
    private String mDeviceToken;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("expire_points")
    private String mExpirePoints;
    @SerializedName("id")
    private Long mId;
    @SerializedName("last_exchanged_points")
    private String mLastExchangedPoints;
    @SerializedName("last_login")
    private String mLastLogin;
    @SerializedName("last_order_file_name")
    private String mLastOrderFileName;
    @SerializedName("logo")
    private String mLogo;
    @SerializedName("media")
    private List<Object> mMedia;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("name")
    private String mName;
    @SerializedName("permissions")
    private List<Object> mPermissions;
    @SerializedName("region_id")
    private Object mRegionId;
    @SerializedName("settings")
    private List<Object> mSettings;
    @SerializedName("support_chat_status")
    private String mSupportChatStatus;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("url")
    private String mUrl;

    public String getActive() {
        return mActive;
    }

    public void setActive(String active) {
        mActive = active;
    }

    public String getChatStatus() {
        return mChatStatus;
    }

    public void setChatStatus(String chatStatus) {
        mChatStatus = chatStatus;
    }

    public String getCityId() {
        return mCityId;
    }

    public void setCityId(String cityId) {
        mCityId = cityId;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getDeviceToken() {
        return mDeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        mDeviceToken = deviceToken;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getExpirePoints() {
        return mExpirePoints;
    }

    public void setExpirePoints(String expirePoints) {
        mExpirePoints = expirePoints;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getLastExchangedPoints() {
        return mLastExchangedPoints;
    }

    public void setLastExchangedPoints(String lastExchangedPoints) {
        mLastExchangedPoints = lastExchangedPoints;
    }

    public String getLastLogin() {
        return mLastLogin;
    }

    public void setLastLogin(String lastLogin) {
        mLastLogin = lastLogin;
    }

    public String getLastOrderFileName() {
        return mLastOrderFileName;
    }

    public void setLastOrderFileName(String lastOrderFileName) {
        mLastOrderFileName = lastOrderFileName;
    }

    public String getLogo() {
        return mLogo;
    }

    public void setLogo(String logo) {
        mLogo = logo;
    }

    public List<Object> getMedia() {
        return mMedia;
    }

    public void setMedia(List<Object> media) {
        mMedia = media;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<Object> getPermissions() {
        return mPermissions;
    }

    public void setPermissions(List<Object> permissions) {
        mPermissions = permissions;
    }

    public Object getRegionId() {
        return mRegionId;
    }

    public void setRegionId(Object regionId) {
        mRegionId = regionId;
    }

    public List<Object> getSettings() {
        return mSettings;
    }

    public void setSettings(List<Object> settings) {
        mSettings = settings;
    }

    public String getSupportChatStatus() {
        return mSupportChatStatus;
    }

    public void setSupportChatStatus(String supportChatStatus) {
        mSupportChatStatus = supportChatStatus;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
