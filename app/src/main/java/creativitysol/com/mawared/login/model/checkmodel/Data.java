
package creativitysol.com.mawared.login.model.checkmodel;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("cart_id")
    private Long mCartId;
    @SerializedName("has_cart")
    private Boolean mHasCart;

    public Object getCartId() {
        return mCartId;
    }

    public void setCartId(Long cartId) {
        mCartId = cartId;
    }

    public Boolean getHasCart() {
        return mHasCart;
    }

    public void setHasCart(Boolean hasCart) {
        mHasCart = hasCart;
    }

}
