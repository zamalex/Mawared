
package app.mawared.alhayat.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.mawared.alhayat.home.model.subproducts.SubProductsItem;

@SuppressWarnings("unused")
public class Product {

    public int qty=0;
    @SerializedName("available")
    private boolean mAvailable;
    @SerializedName("has_offer")
    private boolean mHasOffer;
    @SerializedName("id")
    private Long mId;
    @SerializedName("img")
    private String mImg;
    @SerializedName("type")
    public String type;
    @SerializedName("offer_text")
    private String mOffer;
    @SerializedName("offer_price")
    private String mOfferPrice;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("old_price")
    private String old_price;
    @SerializedName("offer_expiry_date")
    public String offer_expiry_date;
    @SerializedName("quantity")
    private Long mQuantity;

    @SerializedName("in_cart_quantity")
    private Long incart;

    @SerializedName("city_id")
    private String city_id;

    @SerializedName("sku")
    private String mSku;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("vat")
    private String mVat;
    @SerializedName("Price_with_vat")
    private Double Price_with_vat;
    @SerializedName("photo")
    private String photo;
    @SerializedName("offerDetails")
    private OfferDetails offerDetails;
    @SerializedName("offer_take")
    int take;
    @SerializedName("offer_get")
    int get;
    @SerializedName("sub_products")
    private List<SubProductsItem> subProducts;

    public List<SubProductsItem> getSubProducts(){
        return subProducts;
    }

    public String getOffer_expiry_date() {
        return offer_expiry_date;
    }

    public boolean getAvailable() {
        return mAvailable;
    }

    public void setAvailable(boolean available) {
        mAvailable = available;
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

    public String getOffer() {
        String p = "\u002B";
        return take+p+get+"";
    }

    public void setOffer(String offer) {
        mOffer = offer;
    }

    public String getOld_price() {
        return old_price;
    }

    public String getOfferPrice() {
        return mOfferPrice;
    }

    public void setOfferPrice(String offerPrice) {
        mOfferPrice = offerPrice;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
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

    public String getVat() {
        return mVat;
    }

    public void setVat(String vat) {
        mVat = vat;
    }

    public Double getPrice_with_vat() {
        return Price_with_vat;
    }

    public void setPrice_with_vat(Double price_with_vat) {
        Price_with_vat = price_with_vat;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getIncart() {
        return incart;
    }

    public void setIncart(Long incart) {
        this.incart = incart;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    static class OfferDetails{


    }
}
