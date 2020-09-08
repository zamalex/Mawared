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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.Product;
import creativitysol.com.mawared.mycart.model.CardModel;
import creativitysol.com.mawared.mycart.model.Item;
import io.paperdb.Paper;


public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.Holder> {


    ArrayList<Item> products = new ArrayList<>();

    sumListener sumListener;
    UpdateListener updateListener;


    public MyCartAdapter(MyCartAdapter.sumListener sumListener, UpdateListener updateListener) {
        this.sumListener = sumListener;
        this.updateListener = updateListener;
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

        holder.price.setText(products.get(position).getProduct().getPrice() + " " + "ر.س");
        holder.name.setText(products.get(position).getProduct().getTitle());
        holder.total_qty.setText(products.get(position).getAmount() + "");

        if (Integer.parseInt(products.get(position).getAmount())>5){
            holder.offerCard.setVisibility(View.VISIBLE);
        }else
            holder.offerCard.setVisibility(View.GONE);

        sumListener.doSum(calculateTotal());


        products.get(position).getProduct().qty = Integer.parseInt(products.get(position).getAmount());

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.get(position).getProduct().qty++;
                holder.total_qty.setText(products.get(position).getProduct().qty + "");

                int amount = Integer.parseInt(products.get(position).getAmount());
                products.get(position).setAmount((amount+1)+"");
                sumListener.doSum(calculateTotal());
                updateListener.increase(products.get(position), products.get(position).getProduct().qty);

                notifyDataSetChanged();


            }
        });


        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int amount = Integer.parseInt(products.get(position).getAmount());
                products.get(position).setAmount((amount-1)+"");
                products.get(position).getProduct().setQuantity(amount+"");

                if (products.get(position).getProduct().qty != 1) {

                    holder.total_qty.setText(products.get(position).getProduct().qty + "");
                    updateListener.decrease(products.get(position), products.get(position).getProduct().qty);

                    notifyDataSetChanged();
                } else {
                    updateListener.decrease(products.get(position),0);
                    notifyDataSetChanged();
                }



                sumListener.doSum(calculateTotal());
            }
        });

        // Picasso.get().load(item.getProduct().getPhoto()).fit().into(holder.img);

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
        TextView name, price, total_qty;
        ImageView img;
        ImageButton increase, decrease;
        LinearLayout quantityLayout;
        CardView offerCard;
        public Holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            img = itemView.findViewById(R.id.product_img);
            quantityLayout = itemView.findViewById(R.id.quantity_layout);
            total_qty = itemView.findViewById(R.id.total_qty);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
            offerCard = itemView.findViewById(R.id.cardView2);
        }
    }

    Double calculateTotal() {
        Double sum =0.0 ;

        for (Item p : products) {
            sum += (Double.parseDouble(p.getAmount()) * Double.parseDouble(p.getProduct().getPrice()));
        }

        return sum;
    }

    public interface sumListener {
        void doSum(Double sum);
    }

    public interface UpdateListener {

        void increase(Item item, int qty);

        void decrease(Item item, int qty);

    }
}
