
package app.mawared.alhayat.mycart.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Product implements Parcelable {

    public int qty=0;
    @SerializedName("available")
    private boolean mAvailable;
    @SerializedName("cart_item_id")
    private Long mCartItemId;
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

    @SerializedName("city_id")
    private String city_id;


    protected Product(Parcel in) {
        qty = in.readInt();
        mAvailable = in.readByte() != 0;
        if (in.readByte() == 0) {
            mCartItemId = null;
        } else {
            mCartItemId = in.readLong();
        }
        mHasOffer = in.readByte() != 0;
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
        city_id = in.readString();
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

    public boolean getAvailable() {
        return mAvailable;
    }

    public void setAvailable(boolean available) {
        mAvailable = available;
    }

    public Long getCartItemId() {
        return mCartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        mCartItemId = cartItemId;
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

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(qty);
        parcel.writeByte((byte) (mAvailable ? 1 : 0));
        if (mCartItemId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(mCartItemId);
        }
        parcel.writeByte((byte) (mHasOffer ? 1 : 0));
        if (mId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(mId);
        }
        parcel.writeString(mImg);
        if (mInCartQuantity == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(mInCartQuantity);
        }
        parcel.writeString(mOffer);
        if (mOfferPrice == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(mOfferPrice);
        }
        if (mPrice == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(mPrice);
        }
        if (mPriceWithVat == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(mPriceWithVat);
        }
        if (mQuantity == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(mQuantity);
        }
        parcel.writeString(mSku);
        parcel.writeString(mTitle);
        if (mVat == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(mVat);
        }
        parcel.writeString(city_id);
    }
}
