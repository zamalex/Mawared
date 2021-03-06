package app.mawared.alhayat.home;

import android.content.Context;
import android.os.Bundle;
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

import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.home.model.Product;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Holder> {


    ArrayList<Product> products = new ArrayList<>();
    addListener listener;
    Context context;

    public HomeAdapter(Context context,addListener listener) {

        this.context=context;
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

        if (product.getIncart()!=null)
        if (product.qty==0)
            product.qty=Integer.parseInt(product.getIncart().toString());

        holder.price.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(p) + " " + "ر.س");

        holder.name.setText(product.getTitle());
        holder.total_qty.setText(product.qty + "");

        holder.total_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setAmount(position,products.get(position));
            }
        });



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
                    product.setIncart(0l);
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
        if (product.getHasOffer()==0||product.getOffer().isEmpty()||product.getOffer().replace(" ","").equals("1+0")) {
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

       /* Glide
                .with(context)
                .load(product.getImg())
                .fitCenter()
                .into(holder.img);
*/
        Picasso.get().load(product.getImg()).fit().into(holder.img);
        Log.d("imgg", product.getImg());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("product",product.getId().toString());
                bundle.putString("city",product.getCity_id());
                ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                productDetailsFragment.setArguments(bundle);

                ((MainActivity)context).fragmentStack.push(productDetailsFragment);
            }
        });
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
           // name.setSelected(true);
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
        void setAmount(int pos, Product product);



    }
}
