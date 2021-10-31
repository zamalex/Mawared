package app.mawared.alhayat.mycart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import app.mawared.alhayat.R;
import app.mawared.alhayat.home.model.subproducts.SubProductsItem;



public class CartOfferAdapter extends RecyclerView.Adapter<CartOfferAdapter.OfferViewHolder>{

    ArrayList<SubProductsItem> products = new ArrayList<>();
    public void setProducts(ArrayList<SubProductsItem> products) {
        this.products=products;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public CartOfferAdapter.OfferViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_cart_offer, parent, false);
        return new CartOfferAdapter.OfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartOfferAdapter.OfferViewHolder holder, int position) {
     
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {
        public OfferViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
           
        }
    }
}
