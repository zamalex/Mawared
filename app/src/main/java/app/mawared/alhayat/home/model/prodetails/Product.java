
package app.mawared.alhayat.home.model.prodetails;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.mawared.alhayat.home.model.subproducts.SubProductsItem;

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
    @SerializedName("offer_text")
    private String mOffer;
    @SerializedName("final_price")
    private Double mOfferPrice;
    @SerializedName("price")
    private Double mPrice;
    @SerializedName("old_price")
    private Double old_price;
    @SerializedName("quantity")
    private Long mQuantity;
    @SerializedName("sku")
    private String mSku;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("description")
    public String description;
    @SerializedName("vat")
    private Double mVat;
    @SerializedName("offer_expiry_date")
    public String offer_expiry_date;
    @SerializedName("offer_take")
    int take;
    @SerializedName("offer_get")
    int get;
    @SerializedName("sub_products")
    private List<SubProductsItem> subProducts;

    public List<SubProductsItem> getSubProducts() {
        return subProducts;
    }

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
        String p = "\u002B";
        return take+p+get+"";
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

    public Double getOld_price() {
        return old_price;
    }
}
