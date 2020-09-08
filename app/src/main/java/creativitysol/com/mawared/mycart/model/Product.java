
package creativitysol.com.mawared.mycart.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Product {
    public int qty=0;
    @SerializedName("available")
    private String mAvailable;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("has_offer")
    private String mHasOffer;
    @SerializedName("id")
    private Long mId;
    @SerializedName("offer")
    private String mOffer;
    @SerializedName("offer_percentage")
    private String mOfferPercentage;
    @SerializedName("offer_price")
    private String mOfferPrice;
    @SerializedName("photo")
    private String mPhoto;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("Price_with_vat")
    private Double mPriceWithVat;
    @SerializedName("quantity")
    private String mQuantity;
    @SerializedName("sku")
    private String mSku;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("updated_at")
    private String mUpdatedAt;

    public String getAvailable() {
        return mAvailable;
    }

    public void setAvailable(String available) {
        mAvailable = available;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getHasOffer() {
        return mHasOffer;
    }

    public void setHasOffer(String hasOffer) {
        mHasOffer = hasOffer;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getOffer() {
        return mOffer;
    }

    public void setOffer(String offer) {
        mOffer = offer;
    }

    public String getOfferPercentage() {
        return mOfferPercentage;
    }

    public void setOfferPercentage(String offerPercentage) {
        mOfferPercentage = offerPercentage;
    }

    public String getOfferPrice() {
        return mOfferPrice;
    }

    public void setOfferPrice(String offerPrice) {
        mOfferPrice = offerPrice;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public Double getPriceWithVat() {
        return mPriceWithVat;
    }

    public void setPriceWithVat(Double priceWithVat) {
        mPriceWithVat = priceWithVat;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }

    public String getSku() {
        return mSku;
    }

    public void setSku(String sku) {
        mSku = sku;
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

}
