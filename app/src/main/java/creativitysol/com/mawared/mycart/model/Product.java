
package creativitysol.com.mawared.mycart.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Product implements Parcelable {
    public int qty=0;
    @SerializedName("available")
    private String mAvailable;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("final_price_with_vat")
    private String mFinalPriceWithVat;
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
    @SerializedName("price_with_vat")
    private Double mPriceWithVat;
    @SerializedName("quantity")
    private String mQuantity;
    @SerializedName("sku")
    private String mSku;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("updated_at")
    private String mUpdatedAt;

    protected Product(Parcel in) {
        qty = in.readInt();
        mAvailable = in.readString();
        mCreatedAt = in.readString();
        mFinalPriceWithVat = in.readString();
        mHasOffer = in.readString();
        if (in.readByte() == 0) {
            mId = null;
        } else {
            mId = in.readLong();
        }
        mOffer = in.readString();
        mOfferPercentage = in.readString();
        mOfferPrice = in.readString();
        mPhoto = in.readString();
        mPrice = in.readString();
        if (in.readByte() == 0) {
            mPriceWithVat = null;
        } else {
            mPriceWithVat = in.readDouble();
        }
        mQuantity = in.readString();
        mSku = in.readString();
        mTitle = in.readString();
        mUpdatedAt = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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

    public String getFinalPriceWithVat() {
        return mFinalPriceWithVat;
    }

    public void setFinalPriceWithVat(String finalPriceWithVat) {
        mFinalPriceWithVat = finalPriceWithVat;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(qty);
        dest.writeString(mAvailable);
        dest.writeString(mCreatedAt);
        dest.writeString(mFinalPriceWithVat);
        dest.writeString(mHasOffer);
        if (mId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mId);
        }
        dest.writeString(mOffer);
        dest.writeString(mOfferPercentage);
        dest.writeString(mOfferPrice);
        dest.writeString(mPhoto);
        dest.writeString(mPrice);
        if (mPriceWithVat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mPriceWithVat);
        }
        dest.writeString(mQuantity);
        dest.writeString(mSku);
        dest.writeString(mTitle);
        dest.writeString(mUpdatedAt);
    }
}
