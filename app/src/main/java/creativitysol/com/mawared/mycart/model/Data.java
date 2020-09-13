
package creativitysol.com.mawared.mycart.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("cart_id")
    private Long mCartId;
    @SerializedName("items_count")
    private Long mItemsCount;
    @SerializedName("items_sum_final_prices")
    private Long mItemsSumFinalPrices;
    @SerializedName("products")
    private List<Product> mProducts;

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

    public Long getItemsSumFinalPrices() {
        return mItemsSumFinalPrices;
    }

    public void setItemsSumFinalPrices(Long itemsSumFinalPrices) {
        mItemsSumFinalPrices = itemsSumFinalPrices;
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
    }

}
