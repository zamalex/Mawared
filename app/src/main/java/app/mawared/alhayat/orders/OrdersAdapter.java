package app.mawared.alhayat.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.mawared.alhayat.R;
import app.mawared.alhayat.helpers.OrderClickListener;
import app.mawared.alhayat.orders.model.Order;
import app.mawared.alhayat.orders.newmodel.DataItem;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ordersHolder> {

    List<DataItem> allOrderList = new ArrayList<>();
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

    public void setList(List<DataItem> orderList) {
        allOrderList.addAll(orderList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ordersHolder holder, int position) {
        DataItem allOrderModel = allOrderList.get(position);

       // holder.unread_count.setText(allOrderModel.unread_count.toString());
        if (allOrderModel.isNewUpdates()) {
            holder.unread_count.setVisibility(View.VISIBLE);
        } else {
            holder.unread_count.setVisibility(View.INVISIBLE);

        }


        if (allOrderModel != null) {
            holder.tv_orderStatus.setText(OrdersStatus.STATUS.get(allOrderModel.getStatus()));
            holder.tv_orderNumber.setText(allOrderModel.getId() + "#");
           /* if (allOrderModel.getStatus().equals("تم استلام الطلب") || allOrderModel.getStatus().equals("جاري تجهيز طلبك")) {
                holder.tv_orderStatus.setText("جاري تجهيز طلبك");

            } else
                holder.tv_orderStatus.setText(allOrderModel.getStatus());*/
            holder.tv_orderDate.setText("بتاريخ: " + allOrderModel.getCreatedAt());
             if (allOrderModel.getStatus()==0||allOrderModel.getStatus()==1||allOrderModel.getStatus()==3||allOrderModel.getStatus()==4) {
                holder.cl_orderStatus.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.light_green_bg));
            } else if (allOrderModel.getStatus()==5||allOrderModel.getStatus()==6||allOrderModel.getStatus()==8) {
                holder.cl_orderStatus.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.malghi_bg));
            } else {
                holder.cl_orderStatus.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.order_states_bg));

            }
        }
    }

    @Override
    public int getItemCount() {
        return allOrderList.size();
    }

    public class ordersHolder extends RecyclerView.ViewHolder {

        TextView tv_orderNumber, tv_orderStatus, tv_orderDate, unread_count;
        ConstraintLayout cl_orderStatus;

        public ordersHolder(@NonNull View itemView) {
            super(itemView);
            tv_orderNumber = itemView.findViewById(R.id.tv_orderNumber);
            unread_count = itemView.findViewById(R.id.unread_count);
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
