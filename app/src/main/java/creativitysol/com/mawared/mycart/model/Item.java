
package creativitysol.com.mawared.mycart.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Item implements Parcelable {

    @SerializedName("amount")
    private String mAmount;
    @SerializedName("cart_id")
    private String mCartId;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("id")
    private Long mId;
    @SerializedName("product")
    private Product mProduct;
    @SerializedName("product_id")
    private String mProductId;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("updated_at")
    private String mUpdatedAt;

    protected Item(Parcel in) {
        mAmount = in.readString();
        mCartId = in.readString();
        mCreatedAt = in.readString();
        if (in.readByte() == 0) {
            mId = null;
        } else {
            mId = in.readLong();
        }
        mProductId = in.readString();
        mStatus = in.readString();
        mUpdatedAt = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getCartId() {
        return mCartId;
    }

    public void setCartId(String cartId) {
        mCartId = cartId;
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

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }

    public String getProductId() {
        return mProductId;
    }

    public void setProductId(String productId) {
        mProductId = productId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
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
        dest.writeString(mAmount);
        dest.writeString(mCartId);
        dest.writeString(mCreatedAt);
        if (mId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mId);
        }
        dest.writeString(mProductId);
        dest.writeString(mStatus);
        dest.writeString(mUpdatedAt);
    }
}
