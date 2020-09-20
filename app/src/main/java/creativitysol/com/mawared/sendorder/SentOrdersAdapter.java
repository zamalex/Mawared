package creativitysol.com.mawared.sendorder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.mycart.model.Product;


public class SentOrdersAdapter extends RecyclerView.Adapter<SentOrdersAdapter.Holder> {


    ArrayList<Product> products = new ArrayList<>();




    @NonNull
    @Override
    public SentOrdersAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_done, parent, false);
        return new SentOrdersAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SentOrdersAdapter.Holder holder, final int position) {

        Product p = products.get(position);

        holder.count.setText("X"+p.getInCartQuantity());
        holder.name.setText(p.getTitle());
        holder.peice.setText(p.getPriceWithVat().toString()+" ر.س");



        String c = "\\u002B";
        if (p.getHasOffer() == 1) {
            if (p.getOffer() != null)
                if (!p.getOffer().isEmpty()) {
                    String o = p.getOffer().replace(" ", "");
                    String[] parts = o.split(c);
                    int part1 = Integer.parseInt(parts[0]);
                    int part2 = Integer.parseInt(parts[1]);

                    if (p.getInCartQuantity() >= part1 && part2 > 0) {
                        holder.offer_c.setVisibility(View.VISIBLE);
                        holder.name2.setText(p.getTitle());
                        holder.count2.setText("x"+p.getInCartQuantity());
                    } else
                        holder.offer_c.setVisibility(View.GONE);

                }
        } else {
            holder.offer_c.setVisibility(View.GONE);
        }


    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {


        return products.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView count,name,peice;
        TextView count2,name2;
        ConstraintLayout offer_c;
        public Holder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.i_count);
            name = itemView.findViewById(R.id.i_name);

            count2 = itemView.findViewById(R.id.i_count2);
            name2 = itemView.findViewById(R.id.i_name2);
            peice = itemView.findViewById(R.id.i_price);
            offer_c = itemView.findViewById(R.id.offer_c);

        }
    }


}