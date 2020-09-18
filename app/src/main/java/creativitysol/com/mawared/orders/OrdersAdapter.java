package creativitysol.com.mawared.orders;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.helpers.OrderClickListener;
import creativitysol.com.mawared.orders.model.AllOrder;
import creativitysol.com.mawared.orders.model.Order;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ordersHolder> {

    List<Order> allOrderList = new ArrayList<>();
    OrderClickListener mListener;

    public OrdersAdapter(OrderClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ordersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_items, parent, false);
        return new ordersHolder(itemView);
    }

    public void setList(List<Order> orderList) {
        allOrderList.addAll(orderList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ordersHolder holder, int position) {
        Order allOrderModel = allOrderList.get(position);
        if(allOrderModel != null){
            holder.tv_orderNumber.setText(allOrderModel.getId()+"#");
            holder.tv_orderStatus.setText(allOrderModel.getStatus());
            holder.tv_orderDate.setText("بتاريخ: " + allOrderModel.getCreatedAt());
           /* Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.order_states_bg);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);*/
            if (allOrderModel.getStatus().equals("تم استلام الطلب")){
                //DrawableCompat.setTint(wrappedDrawable, Color.BLUE);
                holder.cl_orderStatus.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.light_green_bg));
            }else if(allOrderModel.getStatus().equals("ملغي")){
                holder.cl_orderStatus.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.malghi_bg));
                //DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#de666b"));
            }
            else {
                holder.cl_orderStatus.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.order_states_bg));

            }
        }
    }

    @Override
    public int getItemCount() {
        return allOrderList.size();
    }

    public class ordersHolder extends RecyclerView.ViewHolder {

        TextView tv_orderNumber,tv_orderStatus,tv_orderDate;
        ConstraintLayout cl_orderStatus;

        public ordersHolder(@NonNull View itemView) {
            super(itemView);
            tv_orderNumber = itemView.findViewById(R.id.tv_orderNumber);
            tv_orderStatus = itemView.findViewById(R.id.tv_orderStatus);
            tv_orderDate = itemView.findViewById(R.id.tv_orderDate);
            cl_orderStatus = itemView.findViewById(R.id.cl_orderStatus);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int OrderId = allOrderList.get(getAdapterPosition()).getId();
                    mListener.onClickPressed(OrderId);
                }
            });
        }
    }
}
