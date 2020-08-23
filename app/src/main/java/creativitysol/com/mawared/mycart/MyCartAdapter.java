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
import io.paperdb.Paper;


public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.Holder> {


    ArrayList<Product> products = new ArrayList<>();

    sumListener sumListener;

    public MyCartAdapter(MyCartAdapter.sumListener sumListener) {
        this.sumListener = sumListener;
    }

    @NonNull
    @Override
    public MyCartAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_card, parent, false);
        return new MyCartAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCartAdapter.Holder holder, final int position) {
        final Product product = products.get(position);
        holder.price.setText(product.getPrice() + " " + "ر.س");
        holder.name.setText(product.getTitle());
        holder.total_qty.setText(product.qty + "");

        sumListener.doSum(calculateTotal());

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.get(position).qty++;
                holder.total_qty.setText(products.get(position).qty + "");
                notifyDataSetChanged();

                ArrayList<Product> arrayList = Paper.book().read("cart", new ArrayList<Product>());
                for (int i = 0; i < arrayList.size(); i++) {
                    if (product.getId() == arrayList.get(i).getId()) {
                        arrayList.get(i).qty++;
                    }
                }


                Paper.book().write("cart", arrayList);
                sumListener.doSum(calculateTotal());


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
                    products.remove(position);
                    notifyDataSetChanged();
                }

                ArrayList<Product> arrayList = Paper.book().read("cart", new ArrayList<Product>());
                for (int i = 0; i < arrayList.size(); i++) {
                    if (product.getId() == arrayList.get(i).getId()) {
                        if (arrayList.get(i).qty > 0)
                            arrayList.get(i).qty--;
                        else if (arrayList.get(i).qty==0)
                            arrayList.remove(i);
                    }
                }

                sumListener.doSum(calculateTotal());
                Paper.book().write("cart", arrayList);
            }
        });

        Picasso.get().load(product.getImg()).fit().into(holder.img);
        Log.d("imgg", product.getImg());

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
        TextView name, price, total_qty;
        ImageView img;
        ImageButton increase, decrease;
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

    Long calculateTotal(){
        Long sum=0l;

        for (Product p : products){
            sum += (p.qty * Long.parseLong(p.getPrice()));
        }

        return sum;
    }
    public interface sumListener {
        void doSum(Long sum);
    }
}
