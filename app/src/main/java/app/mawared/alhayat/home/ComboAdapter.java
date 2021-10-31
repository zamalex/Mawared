package app.mawared.alhayat.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.TimerModel;
import app.mawared.alhayat.home.model.Product;
import app.mawared.alhayat.home.model.subproducts.SubProductsItem;

public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ComboViewHolder>{

    Context context;
    ArrayList<Product> products = new ArrayList<>();
    HomeAdapter.addListener listener;

    public ComboAdapter(Context context, HomeAdapter.addListener listener) {

        this.context=context;
        this.listener = listener;
    }
    public void setProducts(ArrayList<Product> products) {
        ArrayList<Product> temp= new ArrayList<>();

        for (int i=0;i<products.size();i++){
            if (products.get(i).type.equals("group"))
                temp.add(products.get(i));
        }
        this.products=temp;
        notifyDataSetChanged();
    }


    @NonNull
    @NotNull
    @Override
    public ComboViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.combo_item, parent, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int ww = (int) (displayMetrics.widthPixels * 0.8);
        itemView.getLayoutParams().width = ww;
        return new ComboViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ComboViewHolder holder, int position) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        ComboSubAdapter comboSubAdapter = new ComboSubAdapter();

        holder.items.setLayoutManager(linearLayoutManager);
        holder.items.setAdapter(comboSubAdapter);

        comboSubAdapter.setProducts((ArrayList<SubProductsItem>) products.get(position).getSubProducts());

        ///////////////////////////////////////////////////////////////////////////////////////////////
        final Product product = products.get(position);
        Double p = Double.parseDouble(product.getPrice())+(Double.parseDouble(product.getVat())/100* Double.parseDouble(product.getPrice()));

        if (product.getIncart()!=null)
            if (product.qty==0)
                product.qty=Integer.parseInt(product.getIncart().toString());

        holder.price.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(p) + " " + "ر.س");

        Double pp = Double.parseDouble(product.getOld_price().toString());


        Log.e("offer is ",product.getOffer());
        holder.name.setText(product.getTitle());
        holder.total_qty.setText(product.qty + "");


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

        holder.total_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!product.getAvailable()) {
                    Toast.makeText(context, "المنتج غير متوفر حاليا", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.setAmount(position,products.get(position));
            }
        });



        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!products.get(position).getAvailable()) {
                    Toast.makeText(context, "المنتج غير متوفر حاليا", Toast.LENGTH_SHORT).show();
                    return;
                }
                products.get(position).qty++;
                notifyDataSetChanged();

                listener.onAddClick(position,product);
            }
        });

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!products.get(position).getAvailable()) {
                    Toast.makeText(context, "المنتج غير متوفر حاليا", Toast.LENGTH_SHORT).show();
                    return;
                }
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



        if (!product.getImg().isEmpty())
            Picasso.get().load(product.getImg()).fit().into(holder.img);
        Log.d("imgg", product.getImg());
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ComboViewHolder extends RecyclerView.ViewHolder {

        RecyclerView items;

        TextView name, price, total_qty;
        ImageView img;
        ImageButton add, increase, decrease;
        LinearLayout quantityLayout;


        public ComboViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            items= itemView.findViewById(R.id.items_rv);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            img = itemView.findViewById(R.id.product_img);
            add = itemView.findViewById(R.id.add);
            quantityLayout = itemView.findViewById(R.id.quantity_layout);
            total_qty = itemView.findViewById(R.id.total_qty);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
        }
    }
}
