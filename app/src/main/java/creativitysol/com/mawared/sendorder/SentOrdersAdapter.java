package creativitysol.com.mawared.sendorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.Product;
import creativitysol.com.mawared.mycart.model.Item;


public class SentOrdersAdapter extends RecyclerView.Adapter<SentOrdersAdapter.Holder> {


    ArrayList<Item> products = new ArrayList<>();




    @NonNull
    @Override
    public SentOrdersAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_done, parent, false);
        return new SentOrdersAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SentOrdersAdapter.Holder holder, final int position) {

        Item p = products.get(position);

        holder.count.setText("X"+p.getAmount());
        holder.name.setText(p.getProduct().getTitle());
        holder.peice.setText(p.getProduct().getPriceWithVat().toString()+" ر.س");

    }

    public void setProducts(ArrayList<Item> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {


        return products.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView count,name,peice;
        public Holder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.i_count);
            name = itemView.findViewById(R.id.i_name);
            peice = itemView.findViewById(R.id.i_price);

        }
    }


}