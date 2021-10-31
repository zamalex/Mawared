package app.mawared.alhayat.home;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.recyclerview.widget.RecyclerView;

import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.TimerModel;
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

        Double pp = Double.parseDouble(product.getOld_price().toString());
        holder.offer_price.setPaintFlags(holder.offer_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (pp!=0)
            holder.offer_price.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(pp) + " " + "ر.س");

        Log.e("offer is ",product.getOffer());
        holder.name.setText(product.getTitle());
        holder.total_qty.setText(product.qty + "");
        if (product.getOffer_expiry_date()!=null) {
            TimerModel timerModel = TimerModel.findDifference("2021-10-28 00:00:00",product.getOffer_expiry_date()+" 23:59:59");
            if (timerModel!=null){
                if (timerModel.days==0){

                    if (holder.countDownTimer!=null)
                        holder.countDownTimer.cancel();
                    holder.countDownTimer = new CountDownTimer(timerModel.milliseconds, 1000) {

                        public void onTick(long millisUntilFinished) {
                            holder.product_offer_time.setText(String.format("%02d:%02d:%02d",
                                    millisUntilFinished/((1000 * 60 * 60)), ((millisUntilFinished/1000) % 3600) / 60, ((millisUntilFinished/1000) % 60)));

                        }

                        public void onFinish() {
                            holder.product_offer_time.setText("00:00");

                        }
                    }.start();
                }else {
                    if (timerModel.days>=0)
                    holder.product_offer_time.setText(timerModel.days+" ايام ");
                }

            }else
                holder.product_offer_time.setText("");
        }else
            holder.product_offer_time.setText("");


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

        holder.offer_txt.setText("");
        if (!product.getHasOffer()||product.getOffer().isEmpty()||product.getOffer().replace(" ","").equals("1+0")) {
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
        if (!product.getImg().isEmpty())
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
        ArrayList<Product> temp= new ArrayList<>();

        for (int i=0;i<products.size();i++){
            if (products.get(i).type.equals("single"))
                temp.add(products.get(i));
        }
        this.products=temp;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (products == null)
            return 0;
        return products.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class Holder extends RecyclerView.ViewHolder {
        CountDownTimer countDownTimer;
        TextView name, price, total_qty, offer_txt,offer_price,product_offer_time;
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
            offer_price = itemView.findViewById(R.id.product_offer_price);
            offer_txt = itemView.findViewById(R.id.txt_offer);
            product_offer_time = itemView.findViewById(R.id.product_offer_time);
        }
    }

    public interface addListener {
        void onAddClick(int pos, Product product);
        void onDecreaseClick(int pos, Product product);
        void setAmount(int pos, Product product);



    }
}
