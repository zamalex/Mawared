
package app.mawared.alhayat.home.model.prodetails;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Product {

    public int qty = 0;
    @SerializedName("available")
    private boolean mAvailable;
    @SerializedName("city_id")
    private String mCityId;
    @SerializedName("has_offer")
    private boolean mHasOffer;
    @SerializedName("id")
    private Long mId;
    @SerializedName("img")
    private String mImg;
    @SerializedName("in_cart_quantity")
    private Long mInCartQuantity;
    @SerializedName("offer")
    private String mOffer;
    @SerializedName("offer_price")
    private Double mOfferPrice;
    @SerializedName("price")
    private Double mPrice;
    @SerializedName("quantity")
    private Long mQuantity;
    @SerializedName("sku")
    private String mSku;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("vat")
    private Double mVat;

    public boolean getAvailable() {
        return mAvailable;
    }

    public void setAvailable(boolean available) {
        mAvailable = available;
    }

    public String getCityId() {
        return mCityId;
    }

    public void setCityId(String cityId) {
        mCityId = cityId;
    }

    public boolean getHasOffer() {
        return mHasOffer;
    }

    public void setHasOffer(boolean hasOffer) {
        mHasOffer = hasOffer;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getImg() {
        return mImg;
    }

    public void setImg(String img) {
        mImg = img;
    }

    public Long getInCartQuantity() {
        return mInCartQuantity;
    }

    public void setInCartQuantity(Long inCartQuantity) {
        mInCartQuantity = inCartQuantity;
    }

    public String getOffer() {
        return mOffer;
    }

    public void setOffer(String offer) {
        mOffer = offer;
    }

    public Double getOfferPrice() {
        return mOfferPrice;
    }

    public void setOfferPrice(Double offerPrice) {
        mOfferPrice = offerPrice;
    }

    public Double getPrice() {
        return mPrice;
    }

    public void setPrice(Double price) {
        mPrice = price;
    }

    public Long getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Long quantity) {
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

    public Double getVat() {
        return mVat;
    }

    public void setVat(Double vat) {
        mVat = vat;
    }

}
