
package app.mawared.alhayat.mycart.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Product implements Parcelable {

    public int qty=0;
    @SerializedName("available")
    private Long mAvailable;
    @SerializedName("cart_item_id")
    private Long mCartItemId;
    @SerializedName("has_offer")
    private Long mHasOffer;
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
    @SerializedName("price_with_vat")
    private Double mPriceWithVat;
    @SerializedName("quantity")
    private Long mQuantity;
    @SerializedName("sku")
    private String mSku;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("vat")
    private Long mVat;

    protected Product(Parcel in) {
        if (in.readByte() == 0) {
            mAvailable = null;
        } else {
            mAvailable = in.readLong();
        }
        if (in.readByte() == 0) {
            mCartItemId = null;
        } else {
            mCartItemId = in.readLong();
        }
        if (in.readByte() == 0) {
            mHasOffer = null;
        } else {
            mHasOffer = in.readLong();
        }
        if (in.readByte() == 0) {
            mId = null;
        } else {
            mId = in.readLong();
        }
        mImg = in.readString();
        if (in.readByte() == 0) {
            mInCartQuantity = null;
        } else {
            mInCartQuantity = in.readLong();
        }
        mOffer = in.readString();
        if (in.readByte() == 0) {
            mOfferPrice = null;
        } else {
            mOfferPrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            mPrice = null;
        } else {
            mPrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            mPriceWithVat = null;
        } else {
            mPriceWithVat = in.readDouble();
        }
        if (in.readByte() == 0) {
            mQuantity = null;
        } else {
            mQuantity = in.readLong();
        }
        mSku = in.readString();
        mTitle = in.readString();
        if (in.readByte() == 0) {
            mVat = null;
        } else {
            mVat = in.readLong();
        }
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

    public Long getAvailable() {
        return mAvailable;
    }

    public void setAvailable(Long available) {
        mAvailable = available;
    }

    public Long getCartItemId() {
        return mCartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        mCartItemId = cartItemId;
    }

    public Long getHasOffer() {
        return mHasOffer;
    }

    public void setHasOffer(Long hasOffer) {
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

    public Double getPriceWithVat() {
        return mPriceWithVat;
    }

    public void setPriceWithVat(Double priceWithVat) {
        mPriceWithVat = priceWithVat;
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

    public Long getVat() {
        return mVat;
    }

    public void setVat(Long vat) {
        mVat = vat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mAvailable == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mAvailable);
        }
        if (mCartItemId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mCartItemId);
        }
        if (mHasOffer == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mHasOffer);
        }
        if (mId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mId);
        }
        dest.writeString(mImg);
        if (mInCartQuantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mInCartQuantity);
        }
        dest.writeString(mOffer);
        if (mOfferPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mOfferPrice);
        }
        if (mPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mPrice);
        }
        if (mPriceWithVat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mPriceWithVat);
        }
        if (mQuantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mQuantity);
        }
        dest.writeString(mSku);
        dest.writeString(mTitle);
        if (mVat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mVat);
        }
    }
}
