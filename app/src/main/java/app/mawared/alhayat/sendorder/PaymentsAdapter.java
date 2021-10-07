package app.mawared.alhayat.sendorder;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.mawared.alhayat.R;
import app.mawared.alhayat.helpers.OrderClickListener;
import app.mawared.alhayat.orders.newmodel.DataItem;
import app.mawared.alhayat.sendorder.model.PaymentMethod;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ordersHolder> {

    List<PaymentMethod> methods = new ArrayList<>();
    paymentClickListener mListener;

    public interface paymentClickListener {
        void onClickPressed(PaymentMethod method);
    }

    public PaymentsAdapter(paymentClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public PaymentsAdapter.ordersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment, parent, false);
        return new PaymentsAdapter.ordersHolder(itemView);
    }

    public void setList(List<PaymentMethod> orderList) {
        methods = orderList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentsAdapter.ordersHolder holder, int position) {
        PaymentMethod  paymentMethod = methods.get(position);
        if (paymentMethod.getGateway().equals("apple-pay"))
            return;

        holder.button.setText(paymentMethod.getName());
        if (!paymentMethod.getIcon().isEmpty())
            Picasso.get().load(paymentMethod.getIcon()).into(holder.img);

    }


    @Override
    public int getItemCount() {
        return methods.size();
    }

    public class ordersHolder extends RecyclerView.ViewHolder {


        Button button;
        ImageView img;

        public ordersHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.p_visa);
            img = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickPressed(methods.get(getAdapterPosition()));
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickPressed(methods.get(getAdapterPosition()));
                }
            });
        }
    }
}