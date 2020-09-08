
package creativitysol.com.mawared.mycart.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("items")
    private List<Item> mItems;

    public List<Item> getItems() {
        return mItems;
    }

    public void setItems(List<Item> items) {
        mItems = items;
    }

}
