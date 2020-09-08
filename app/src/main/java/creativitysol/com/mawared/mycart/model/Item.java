
package creativitysol.com.mawared.mycart.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Item implements Cloneable{

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

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
