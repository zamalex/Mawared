
package app.mawared.alhayat.home.model.prodetails;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ProductDetails {

    @SerializedName("product")
    private Product mProduct;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("success")
    private Boolean mSuccess;

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean success) {
        mSuccess = success;
    }

}
