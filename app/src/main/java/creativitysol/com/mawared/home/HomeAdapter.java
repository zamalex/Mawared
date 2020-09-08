package creativitysol.com.mawared.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.Product;
import io.paperdb.Paper;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Holder> {


    ArrayList<Product> products = new ArrayList<>();
    addListener listener;

    public HomeAdapter(addListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new HomeAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final Product product = products.get(position);
        Double p = Double.parseDouble(product.getPrice())+(Double.parseDouble(product.getVat())/100* Double.parseDouble(product.getPrice()));

        holder.price.setText(p + " " + "ر.س");
        holder.name.setText(product.getTitle());
        holder.total_qty.setText(product.qty + "");
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.get(position).qty++;
                notifyDataSetChanged();

                listener.onAddClick(position,product);
            }
        });

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.get(position).qty++;
                holder.total_qty.setText(products.get(position).qty + "");
                notifyDataSetChanged();

                notifyDataSetChanged();

                listener.onAddClick(position,product);

            }
        });


        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                products.get(position).qty--;
                if (products.get(position).qty != 0) {

                    holder.total_qty.setText(products.get(position).qty + "");
                    notifyDataSetChanged();
                } else {
                    notifyDataSetChanged();
                }

                listener.onDecreaseClick(position,product);

            }
        });

        if (product.qty == 0) {
            holder.add.setVisibility(View.VISIBLE);
            holder.quantityLayout.setVisibility(View.GONE);
        } else {

            holder.add.setVisibility(View.GONE);
            holder.quantityLayout.setVisibility(View.VISIBLE);

        }

        holder.offer_txt.setText("");
        if (product.getOffer().isEmpty()) {
            holder.offer_txt.setVisibility(View.GONE);
            holder.offer_img.setVisibility(View.GONE);

        } else {
            holder.offer_txt.setVisibility(View.VISIBLE);
            holder.offer_img.setVisibility(View.VISIBLE);



            String g = product.getOffer();

            char[] c_arr = g.toCharArray();
            for (int i =0;i<c_arr.length;i++){
                if (c_arr[i]!=' '){
                    holder.offer_txt.append(c_arr[i]+"");
                    holder.offer_txt.append("\n");
                }

            }

        }


        Picasso.get().load(product.getImg()).fit().into(holder.img);
        Log.d("imgg", product.getImg());

    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (products == null)
            return 0;
        return products.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name, price, total_qty, offer_txt;
        ImageView img, offer_img;
        ImageButton add, increase, decrease;
        LinearLayout quantityLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            img = itemView.findViewById(R.id.product_img);
            add = itemView.findViewById(R.id.add);
            quantityLayout = itemView.findViewById(R.id.quantity_layout);
            total_qty = itemView.findViewById(R.id.total_qty);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
            offer_img = itemView.findViewById(R.id.off_img);
            offer_txt = itemView.findViewById(R.id.txt_offer);
        }
    }

    public interface addListener {
        void onAddClick(int pos, Product product);
        void onDecreaseClick(int pos, Product product);



    }
}
