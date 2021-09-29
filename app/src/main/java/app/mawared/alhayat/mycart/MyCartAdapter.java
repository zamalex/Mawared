package app.mawared.alhayat.mycart;

import android.os.SystemClock;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import app.mawared.alhayat.R;

import app.mawared.alhayat.mycart.model.Product;


public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.Holder> {


    ArrayList<Product> products = new ArrayList<>();

    sumListener sumListener;
    UpdateListener updateListener;
    Timer timer;


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

   // long mLastClickTime = 0;

    @Override
    public void onBindViewHolder(@NonNull final MyCartAdapter.Holder holder, final int position) {

        Product item = products.get(position);
        holder.price.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(products.get(position).getPriceWithVat()) + " " + "ر.س");//

        holder.name.setText(products.get(position).getTitle());
        holder.total_qty.setText(products.get(position).getInCartQuantity() + "");


        holder.offerCard.setVisibility(View.GONE);

        sumListener.doSum(calculateTotal());


        item.qty = Integer.parseInt(item.getInCartQuantity().toString());


        holder.total_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListener.setAmount(item);
            }
        });


        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MyCartFragment) updateListener).isLoading)
                    //return;
                    System.out.println("");


                ((MyCartFragment) updateListener).isLoading=true;
                //mLastClickTime = SystemClock.elapsedRealtime();

                if (timer!=null)
                    timer.cancel();
                timer = new Timer();
                item.setInCartQuantity(1+item.getInCartQuantity());
                holder.total_qty.setText(item.getInCartQuantity() + "");
                sumListener.doSum(calculateTotal());
                notifyDataSetChanged();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        updateListener.increase(item, Integer.parseInt(item.getInCartQuantity()+""));

                    }
                },1000);




            }
        });


        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MyCartFragment) updateListener).isLoading)
                    System.out.println("");


                ((MyCartFragment) updateListener).isLoading = true;
                if (timer!=null)
                    timer.cancel();
                timer = new Timer();

                Long amount = Long.parseLong(item.getInCartQuantity().toString());
                if (amount > 1) {
                    item.setInCartQuantity((amount - 1));
                    holder.total_qty.setText(item.qty + "");

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            updateListener.decrease(item,  Integer.parseInt(item.getInCartQuantity()+""));

                        }
                    },1000);
                }
               else {
                    updateListener.decrease(item, 0);
                }


                sumListener.doSum(calculateTotal());
                notifyDataSetChanged();

            }
        });

        Picasso.get().load(item.getImg()).fit().into(holder.img);

        String c = "\\u002B";


        if (item.getHasOffer()) {
            if (item.getOffer() != null)
                if (!item.getOffer().isEmpty()) {
                    String o = item.getOffer().replace(" ", "");
                    String[] parts = o.split(c);
                    int part1 = Integer.parseInt(parts[0]);
                    int part2 = Integer.parseInt(parts[1]);
                    Log.d("oof", part1 + "   " + part2);

                    if (Long.parseLong(item.getInCartQuantity().toString()) >= part1 && part2 > 0) {
                        holder.offerCard.setVisibility(View.VISIBLE);
                        holder.offer_name.setText(item.getTitle());
                        Picasso.get().load(item.getImg()).fit().into(holder.product_img2);

                        int q = Integer.parseInt(item.getInCartQuantity() + "") / part1 * part2;
                        holder.offer_qty.setText(" الكمية " + q);
                    } else
                        holder.offerCard.setVisibility(View.GONE);

                }
        } else {
            holder.offerCard.setVisibility(View.GONE);
        }


    }

    public void setProducts(ArrayList<Product> products) {


        for (Iterator<Product> it = products.iterator(); it.hasNext(); ) {
            Product aDrugStrength = it.next();
            if (aDrugStrength.getInCartQuantity()==0) {
                it.remove();
            }
        }

        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {


        return products.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name, price, total_qty, offer_qty, offer_name;
        ImageView img, product_img2;
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
            offer_qty = itemView.findViewById(R.id.offer_qty);
            offer_name = itemView.findViewById(R.id.product_name2);
            product_img2 = itemView.findViewById(R.id.product_img2);
        }
    }

    Double calculateTotal() {
        Double sum = 0.0;

        for (Product p : products) {
            sum += (Double.parseDouble(p.getInCartQuantity().toString()) * (p.getPriceWithVat()));
        }

        return sum;
    }

    public interface sumListener {
        void doSum(Double sum);
    }

    public interface UpdateListener {

        void increase(Product item, int qty);

        void decrease(Product item, int qty);

        void setAmount(Product item);

    }
}
