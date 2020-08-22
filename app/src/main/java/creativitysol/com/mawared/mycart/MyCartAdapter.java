package creativitysol.com.mawared.mycart;

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

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.Product;


public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.Holder> {


    ArrayList<Product> products = new ArrayList<>();

    @NonNull
    @Override
    public MyCartAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_card, parent, false);
        return new MyCartAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCartAdapter.Holder holder, final int position) {
    /*    final Product product = products.get(position);
        holder.price.setText(product.getPrice()+" "+"ر.س");
        holder.name.setText(product.getTitle());
        holder.total_qty.setText(product.qty+"");


        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.get(position).qty++;
                holder.total_qty.setText(products.get(position).qty+"");
                notifyDataSetChanged();
            }
        });
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                products.get(position).qty--;
                if (products.get(position).qty!=0){

                    holder.total_qty.setText(products.get(position).qty+"");
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetChanged();
                }
            }
        });




        Picasso.get().load(product.getImg()).fit().into(holder.img);
        Log.d("imgg",product.getImg());
*/
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return 5;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name,price,total_qty;
        ImageView img;
        ImageButton increase,decrease;
        LinearLayout quantityLayout;
        public Holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            img = itemView.findViewById(R.id.product_img);
            quantityLayout = itemView.findViewById(R.id.quantity_layout);
            total_qty = itemView.findViewById(R.id.total_qty);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
        }
    }

    public interface addListener{
        void onAddClick(int pos,Product product);
    }
}
