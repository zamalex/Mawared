
package creativitysol.com.mawared.home.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Datum {

    @SerializedName("active")
    private Long mActive;
    @SerializedName("code")
    private String mCode;
    @SerializedName("country_code")
    private String mCountryCode;
    @SerializedName("deleted_at")
    private Object mDeletedAt;
    @SerializedName("id")
    private Long mId;
    @SerializedName("latitude")
    private Double mLatitude;
    @SerializedName("longitude")
    private Double mLongitude;
    @SerializedName("name")
    private String mName;
    @SerializedName("region_id")
    private Long mRegionId;
    @SerializedName("slug")
    private String mSlug;

    public Long getActive() {
        return mActive;
    }

    public void setActive(Long active) {
        mActive = active;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public Object getDeletedAt() {
        return mDeletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        mDeletedAt = deletedAt;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Long getRegionId() {
        return mRegionId;
    }

    public void setRegionId(Long regionId) {
        mRegionId = regionId;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        mSlug = slug;
    }

}
