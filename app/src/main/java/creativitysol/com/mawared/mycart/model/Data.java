
package creativitysol.com.mawared.mycart.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data implements Parcelable {

    @SerializedName("cart_id")
    private Long mCartId;
    @SerializedName("items_count")
    private Long mItemsCount;
    @SerializedName("items_sum_final_prices")
    private Double mItemsSumFinalPrices;
    @SerializedName("products")
    private List<Product> mProducts;

    protected Data(Parcel in) {
        if (in.readByte() == 0) {
            mCartId = null;
        } else {
            mCartId = in.readLong();
        }
        if (in.readByte() == 0) {
            mItemsCount = null;
        } else {
            mItemsCount = in.readLong();
        }
        if (in.readByte() == 0) {
            mItemsSumFinalPrices = null;
        } else {
            mItemsSumFinalPrices = in.readDouble();
        }
        mProducts = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public Long getCartId() {
        return mCartId;
    }

    public void setCartId(Long cartId) {
        mCartId = cartId;
    }

    public Long getItemsCount() {
        return mItemsCount;
    }

    public void setItemsCount(Long itemsCount) {
        mItemsCount = itemsCount;
    }

    public Double getItemsSumFinalPrices() {
        return mItemsSumFinalPrices;
    }

    public void setItemsSumFinalPrices(Double itemsSumFinalPrices) {
        mItemsSumFinalPrices = itemsSumFinalPrices;
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mCartId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mCartId);
        }
        if (mItemsCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mItemsCount);
        }
        if (mItemsSumFinalPrices == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mItemsSumFinalPrices);
        }
        dest.writeTypedList(mProducts);
    }
}
